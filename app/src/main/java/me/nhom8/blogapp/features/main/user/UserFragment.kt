package me.nhom8.blogapp.features.main.user

import android.app.AlertDialog
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import me.nhom8.blogapp.R
import me.nhom8.blogapp.core.BaseFragment
import me.nhom8.blogapp.databinding.FragmentUserBinding
import me.nhom8.blogapp.features.main.MainViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class UserFragment : BaseFragment(R.layout.fragment_user) {

    private val binding: FragmentUserBinding by viewBinding()
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun bindEvent() {
        // Logout
        binding.button.setOnClickListener {
            mainViewModel.requestLogout()
        }

        // Edit profile
        binding.btnEditProfile.setOnClickListener {
            showEditProfileDialog()
        }

        // Change password
        binding.btnChangePassword.setOnClickListener {
            showChangePasswordDialog()
        }
    }

    override fun collectState() {
        // Lắng nghe state từ MainViewModel
        lifecycleScope.launchWhenStarted {
            mainViewModel.state.collect { state ->
                state.user?.let { user ->
                    binding.tvFullName.text = user.fullName
                    binding.tvEmail.text = user.email
                }
            }
        }
    }

    private fun showEditProfileDialog() {
        val dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_edit_user, null)
        val edtFullName = dialogView.findViewById<EditText>(R.id.etFullName)
        val edtEmail = dialogView.findViewById<EditText>(R.id.etEmail)

        mainViewModel.state.value.user?.let { user ->
            edtFullName.setText(user.fullName)
            edtEmail.setText(user.email)
        }

        AlertDialog.Builder(requireContext())
            .setTitle("Chỉnh sửa thông tin")
            .setView(dialogView)
            .setPositiveButton("Lưu") { _, _ ->
                val fullName = edtFullName.text.toString()
                val email = edtEmail.text.toString()

                // Cập nhật ngay lên UI
                mainViewModel.updateUserProfile(fullName, email)

                // Thông báo
                Toast.makeText(requireContext(), "Thông tin đã được lưu", Toast.LENGTH_SHORT).show()
            }

            .setNegativeButton("Hủy", null)
            .show()
    }

    private fun showChangePasswordDialog() {
        val dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_change_password, null)
        val edtOldPassword = dialogView.findViewById<EditText>(R.id.edtOldPassword)
        val edtNewPassword = dialogView.findViewById<EditText>(R.id.edtNewPassword)

        AlertDialog.Builder(requireContext())
            .setTitle("Đổi mật khẩu")
            .setView(dialogView)
            .setPositiveButton("Lưu") { _, _ ->
                val oldPass = edtOldPassword.text.toString()
                val newPass = edtNewPassword.text.toString()

                mainViewModel.changePassword(oldPass, newPass)

                Toast.makeText(requireContext(), "Mật khẩu đã được thay đổi", Toast.LENGTH_SHORT).show()
            }

            .setNegativeButton("Hủy", null)
            .show()
    }
}
