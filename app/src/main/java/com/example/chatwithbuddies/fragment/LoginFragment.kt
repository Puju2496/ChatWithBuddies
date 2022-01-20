package com.example.chatwithbuddies.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.chatwithbuddies.R
import com.example.chatwithbuddies.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentLoginBinding

    private var onLoginSuccessListener: OnLoginSuccessListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.login.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.login -> {
                val id = binding.id.text.toString()
                val name = binding.name.text.toString()

                if (id.isNotEmpty() && name.isNotEmpty()) {
                    onLoginSuccessListener?.onLoginSuccess(id,name)
                } else {
                    if (id.isEmpty())
                        binding.idLayout.error = "User id is required"
                    if (name.isEmpty())
                        binding.nameLayout.error = "User name is required"
                }
            }
        }
    }

    interface OnLoginSuccessListener {
        fun onLoginSuccess(userId: String, userName: String)
    }

    companion object {
        fun newInstance(onLoginSuccessListener: OnLoginSuccessListener) = LoginFragment().apply {
            this.onLoginSuccessListener = onLoginSuccessListener
        }
    }
}