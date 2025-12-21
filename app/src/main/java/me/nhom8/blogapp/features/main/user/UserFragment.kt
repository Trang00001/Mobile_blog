package me.nhom8.blogapp.features.main.user

import androidx.fragment.app.activityViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import me.nhom8.blogapp.R
import me.nhom8.blogapp.core.BaseFragment
import me.nhom8.blogapp.databinding.FragmentUserBinding
import me.nhom8.blogapp.features.main.MainViewModel

class UserFragment : BaseFragment(R.layout.fragment_user) {
    private val binding: FragmentUserBinding by viewBinding()

    private val mainViewModel: MainViewModel by activityViewModels()

    override fun bindEvent() {
        binding.button.setOnClickListener {
            mainViewModel.requestLogout()
        }
    }

    override fun collectState() {
        // TODO: Handle UserFragment State from ViewModel or MainViewModel
    }
}
