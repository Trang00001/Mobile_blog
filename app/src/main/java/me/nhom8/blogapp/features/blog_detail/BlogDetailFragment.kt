package me.nhom8.blogapp.features.blog_detail

import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import me.nhom8.blogapp.R
import me.nhom8.blogapp.core.BaseFragment
import me.nhom8.blogapp.databinding.FragmentBlogDetailBinding
import me.nhom8.blogapp.models.Blog
import me.nhom8.blogapp.utils.extensions.getCompactParcelableExtra
import me.nhom8.blogapp.utils.extensions.toTimeAgo

class BlogDetailFragment : BaseFragment(R.layout.fragment_blog_detail) {
    private val binding: FragmentBlogDetailBinding by viewBinding()

    override fun setupView() {
        super.setupView()
        val blog = requireActivity().getCompactParcelableExtra<Blog>()
        binding.run {
            appBar.iconButton.setOnClickListener {
                requireActivity().finish()
            }
            ivCover.load(blog?.imageUrl) {
                crossfade(true)
            }
            tvAuthorName.text = blog?.creator?.fullName
            tvBlogTitle.text = blog?.title
            tvBlogCreatedDate.text = blog?.createdAt?.toTimeAgo()
        }
    }
}
