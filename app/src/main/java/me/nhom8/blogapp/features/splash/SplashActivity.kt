package me.nhom8.blogapp.features.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import me.nhom8.blogapp.R
import me.nhom8.blogapp.core.BaseActivity
import me.nhom8.blogapp.features.authentication.AuthenticationActivity
import me.nhom8.blogapp.features.main.MainActivity

@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity(R.layout.activity_splash) {
    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindViewModel()
    }

    private fun bindViewModel() {
        viewModel.isLoggedInState
            .flowWithLifecycle(lifecycle)
            .onEach { isLoggedIn ->
                if (isLoggedIn) {
                    startActivity(Intent(this, MainActivity::class.java))
                } else {
                    startActivity(Intent(this, AuthenticationActivity::class.java))
                }
                finish()
            }
            .launchIn(lifecycleScope)
    }
}
