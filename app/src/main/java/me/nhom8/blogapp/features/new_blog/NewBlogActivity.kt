package me.nhom8.blogapp.features.new_blog

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import me.nhom8.blogapp.R
import me.nhom8.blogapp.models.Blog
import me.nhom8.blogapp.utils.extensions.getCompactParcelableExtra

@AndroidEntryPoint
class NewBlogActivity : AppCompatActivity() {

    private val viewModel: NewBlogViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_blog)

        // Nếu activity được mở để edit blog từ BlogDetail, blog sẽ được truyền qua Intent extras.
        val blog = getCompactParcelableExtra<Blog>()
        if (savedInstanceState == null && blog != null) {
            viewModel.setEditingBlog(blog)
        }
    }
}
