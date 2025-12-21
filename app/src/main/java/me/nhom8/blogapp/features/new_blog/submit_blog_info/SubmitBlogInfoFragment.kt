package me.nhom8.blogapp.features.new_blog.submit_blog_info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.nhom8.blogapp.R
import me.nhom8.blogapp.core.BaseFragment
import me.nhom8.blogapp.databinding.FragmentSubmitBlogInfoBinding

class SubmitBlogInfoFragment : BaseFragment(R.layout.fragment_submit_blog_info) {
    private var _binding: FragmentSubmitBlogInfoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSubmitBlogInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
