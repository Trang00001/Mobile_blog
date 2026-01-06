package me.nhom8.blogapp.features.blog_detail

import android.content.Intent
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import me.nhom8.blogapp.R
import me.nhom8.blogapp.core.BaseFragment
import me.nhom8.blogapp.data.local.LocalUserDataSource
import me.nhom8.blogapp.databinding.FragmentBlogDetailBinding
import me.nhom8.blogapp.features.helpers.LoadingStatus
import me.nhom8.blogapp.features.new_blog.NewBlogActivity
import me.nhom8.blogapp.models.Blog
import me.nhom8.blogapp.utils.extensions.getCompactParcelableExtra
import me.nhom8.blogapp.utils.extensions.snack
import me.nhom8.blogapp.utils.extensions.toBundle
import me.nhom8.blogapp.utils.extensions.toTimeAgo
import javax.inject.Inject

@AndroidEntryPoint
class BlogDetailFragment : BaseFragment(R.layout.fragment_blog_detail) {
    private val binding: FragmentBlogDetailBinding by viewBinding()

    private val viewModel: BlogDetailViewModel by viewModels()

    @Inject
    lateinit var localUserDataSource: LocalUserDataSource

    private var blog: Blog? = null

    override fun setupView() {
        super.setupView()
        blog = requireActivity().getCompactParcelableExtra<Blog>()

        val currentBlog = blog
        if (currentBlog == null) {
            requireActivity().finish()
            return
        }

        binding.run {
            appBar.iconButton.setOnClickListener { requireActivity().finish() }

            // Hiển thị nội dung blog
            // Ẩn ảnh nếu không có imageUrl
            if (currentBlog.imageUrl.isNullOrBlank()) {
                ivCover.visibility = android.view.View.GONE
            } else {
                ivCover.visibility = android.view.View.VISIBLE
                ivCover.load(currentBlog.imageUrl) { crossfade(true) }
            }
            tvAuthorName.text = currentBlog.creator.fullName
            tvBlogTitle.text = currentBlog.title
            tvBlogCreatedDate.text = currentBlog.createdAt.toTimeAgo()
            tvBlogContent.text = currentBlog.content

            // Chỉ hiển thị Edit/Delete nếu là chủ bài viết
            val userId = localUserDataSource.userId
            val isOwner = userId != null && userId == currentBlog.creator.id
            appBar.btnEdit.visibility = if (isOwner) android.view.View.VISIBLE else android.view.View.GONE
            appBar.btnDelete.visibility = if (isOwner) android.view.View.VISIBLE else android.view.View.GONE

            appBar.btnEdit.setOnClickListener {
                val intent = Intent(requireActivity(), NewBlogActivity::class.java).apply {
                    putExtras(currentBlog.toBundle())
                }
                startActivity(intent)
            }

            appBar.btnDelete.setOnClickListener {
                showDeleteConfirmDialog(currentBlog.id)
            }
        }
    }

    private fun showDeleteConfirmDialog(blogId: String) {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.delete_blog))
            .setMessage(getString(R.string.delete_blog_confirm_message))
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ -> dialog.dismiss() }
            .setPositiveButton(getString(R.string.confirm)) { dialog, _ ->
                dialog.dismiss()
                viewModel.deleteBlog(blogId)
            }
            .show()
    }

    override fun collectState() {
        super.collectState()
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.state.collect { state ->
                        when (state.deleteStatus) {
                            LoadingStatus.LOADING -> {
                                binding.root.snack(getString(R.string.deleting_blog))
                            }

                            LoadingStatus.DONE -> {
                                binding.root.snack(getString(R.string.delete_blog_success))
                                viewModel.resetDeleteStatus()
                                requireActivity().finish()
                            }

                            LoadingStatus.ERROR -> {
                                binding.root.snack(
                                    state.error?.message
                                        ?: getString(R.string.something_went_wrong_please_try_again),
                                )
                                viewModel.resetDeleteStatus()
                            }

                            else -> Unit
                        }
                    }
                }
            }
        }
    }
}
