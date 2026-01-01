package me.nhom8.blogapp.features.new_blog.submit_blog_info

import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import me.nhom8.blogapp.R
import me.nhom8.blogapp.core.BaseFragment
import me.nhom8.blogapp.databinding.FragmentSubmitBlogInfoBinding
import me.nhom8.blogapp.features.helpers.LoadingStatus
import me.nhom8.blogapp.features.image.ImageFragment
import me.nhom8.blogapp.features.new_blog.NewBlogState
import me.nhom8.blogapp.features.new_blog.NewBlogViewModel
import me.nhom8.blogapp.models.Category
import me.nhom8.blogapp.utils.extensions.getLocalizedName
import me.nhom8.blogapp.utils.extensions.snack

@AndroidEntryPoint
class SubmitBlogInfoFragment : BaseFragment(R.layout.fragment_submit_blog_info) {

    private val binding: FragmentSubmitBlogInfoBinding by viewBinding()

    private val viewModel: NewBlogViewModel by activityViewModels()

    private val availableCategories: List<Category> =
        Category.entries.filter { it != Category.ALL }

    override fun setupView() {
        super.setupView()

        binding.btnBack.setOnClickListener { requireActivity().finish() }

        // Embed ImageFragment (child) để tái sử dụng logic pick + upload
        if (childFragmentManager.findFragmentById(R.id.imageUploaderContainer) == null) {
            childFragmentManager
                .beginTransaction()
                .replace(
                    R.id.imageUploaderContainer,
                    ImageFragment.newInstance(showButton = false),
                )
                .commit()
        }

        // Lắng nghe URL ảnh trả về từ ImageFragment
        childFragmentManager.setFragmentResultListener(
            ImageFragment.RESULT_KEY_IMAGE,
            viewLifecycleOwner,
        ) { _, bundle ->
            val url = bundle.getString(ImageFragment.BUNDLE_KEY_IMAGE_URL) ?: return@setFragmentResultListener
            viewModel.setImageUrl(url)
            binding.ivSelectedImage.visibility = View.VISIBLE
            binding.ivSelectedImage.load(url) { crossfade(true) }
        }

        setupCategoryDropdown()

        // Prefill khi edit
        val state = viewModel.state.value
        binding.etTitle.setText(state.title)
        setCategoryText(state.category)

        binding.btnAddImage.setOnClickListener {
            (childFragmentManager.findFragmentById(R.id.imageUploaderContainer) as? ImageFragment)
                ?.openImagePicker()
        }

        binding.btnSubmit.setOnClickListener {
            val title = binding.etTitle.text?.toString()?.trim().orEmpty()
            val content = viewModel.state.value.content.trim()

            if (title.isEmpty()) {
                binding.root.snack(getString(R.string.title_must_not_be_empty))
                return@setOnClickListener
            }
            if (content.isEmpty()) {
                binding.root.snack(getString(R.string.content_must_not_be_empty))
                return@setOnClickListener
            }

            viewModel.setTitle(title)
            viewModel.submit()
        }

        // Đổi label nút theo mode
        updateSubmitButtonText(state)
    }

    private fun setupCategoryDropdown() {
        val categoryNames = availableCategories.map { getString(it.getLocalizedName()) }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, categoryNames)
        binding.actCategory.setAdapter(adapter)

        binding.actCategory.setOnItemClickListener { _, _, position, _ ->
            val selected = availableCategories.getOrNull(position) ?: return@setOnItemClickListener
            viewModel.setCategory(selected)
        }
    }

    private fun setCategoryText(category: Category) {
        if (category == Category.ALL) return
        binding.actCategory.setText(getString(category.getLocalizedName()), false)
    }

    private fun updateSubmitButtonText(state: NewBlogState) {
        binding.btnSubmit.text =
            if (state.mode == NewBlogState.Mode.EDIT) {
                getString(R.string.update_blog)
            } else {
                getString(R.string.post_blog)
            }
    }

    override fun collectState() {
        super.collectState()
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.state.collect { state ->
                        updateSubmitButtonText(state)

                        when (state.submitStatus) {
                            LoadingStatus.LOADING -> {
                                binding.progressSubmit.visibility = View.VISIBLE
                                binding.btnSubmit.isEnabled = false
                            }

                            LoadingStatus.DONE -> {
                                binding.progressSubmit.visibility = View.GONE
                                binding.btnSubmit.isEnabled = true
                                binding.root.snack(getString(R.string.blog_submit_success))
                                viewModel.resetSubmitStatus()
                                requireActivity().finish()
                            }

                            LoadingStatus.ERROR -> {
                                binding.progressSubmit.visibility = View.GONE
                                binding.btnSubmit.isEnabled = true
                                binding.root.snack(state.error?.message ?: getString(R.string.something_went_wrong_please_try_again))
                                viewModel.resetSubmitStatus()
                            }

                            else -> {
                                binding.progressSubmit.visibility = View.GONE
                                binding.btnSubmit.isEnabled = true
                            }
                        }

                        // Nếu có imageUrl sẵn (edit), hiển thị preview
                        val url = state.imageUrl
                        if (!url.isNullOrBlank()) {
                            binding.ivSelectedImage.visibility = View.VISIBLE
                            binding.ivSelectedImage.load(url) { crossfade(true) }
                        }
                    }
                }
            }
        }
    }
}
