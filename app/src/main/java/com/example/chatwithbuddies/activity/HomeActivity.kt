package com.example.chatwithbuddies.activity

import android.Manifest
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.commit
import com.example.chatwithbuddies.R
import com.example.chatwithbuddies.databinding.ActivityHomeBinding
import com.example.chatwithbuddies.fragment.HomeFragment
import com.example.chatwithbuddies.fragment.LoginFragment
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : AppCompatActivity(), LoginFragment.OnLoginSuccessListener, HomeFragment.OnLogOutListener {

    lateinit var binding: ActivityHomeBinding

    @Inject
    lateinit var preference: SharedPreferences

    private val permissions = arrayOf(Manifest.permission.RECORD_AUDIO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO)

        if (preference.getBoolean(IS_LOGIN, false)) {
            navigateToHome()
        } else {
            navigateToLogin()
        }
    }

    private fun navigateToLogin() {
        supportFragmentManager.commit {
            replace(R.id.container, LoginFragment.newInstance(this@HomeActivity), LOGIN)
        }
    }

    private fun navigateToHome() {
        supportFragmentManager.commit {
            replace(R.id.container, HomeFragment.newInstance(this@HomeActivity), HOME)
        }
    }

    override fun onLoginSuccess(userId: String, userName: String) {
        preference.edit().apply {
            putBoolean(IS_LOGIN, true)
            putString(USER_ID, userId)
            putString(USER_NAME, userName)
            apply()
        }
        navigateToHome()
    }

    override fun onLogOut() {
        preference.edit().putBoolean(IS_LOGIN, false).apply()
        navigateToLogin()
    }

    companion object {
        private const val HOME = "HOME"
        private const val LOGIN = "LOGIN"

        private const val IS_LOGIN = "is_login"
        const val USER_ID = "user_id"
        const val USER_NAME = "user_name"

        private const val REQUEST_RECORD_AUDIO = 200
    }
}