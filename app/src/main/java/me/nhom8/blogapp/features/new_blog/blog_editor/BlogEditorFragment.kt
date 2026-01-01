package me.nhom8.blogapp.features.new_blog.blog_editor

import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import me.nhom8.blogapp.R
import me.nhom8.blogapp.core.BaseFragment
import me.nhom8.blogapp.databinding.FragmentBlogEditorBinding
import me.nhom8.blogapp.features.new_blog.NewBlogViewModel

@AndroidEntryPoint
class BlogEditorFragment : BaseFragment(R.layout.fragment_blog_editor) {

    private val binding: FragmentBlogEditorBinding by viewBinding()

    private val viewModel: NewBlogViewModel by activityViewModels()

    override fun setupView() {
        super.setupView()

        // Prefill content (khi edit)
        val currentState = viewModel.state.value
        binding.etContent.setText(currentState.content)

        binding.btnBack.setOnClickListener { requireActivity().finish() }

        binding.btnNext.setOnClickListener {
            val content = binding.etContent.text?.toString()?.trim().orEmpty()
            viewModel.setContent(content)
            findNavController().navigate(R.id.action_blogEditorFragment_to_submitBlogInfoFragment)
        }
    }
}
