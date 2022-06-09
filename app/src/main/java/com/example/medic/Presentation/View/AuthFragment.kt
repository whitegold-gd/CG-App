package com.example.medic.Presentation.View

import com.example.medic.MainActivity
import android.view.ViewGroup
import android.view.LayoutInflater
import android.os.Bundle
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import com.example.medic.Presentation.ViewModel.AuthViewModel
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.medic.DI.ServiceLocator
import com.example.medic.Domain.Model.User
import com.example.medic.databinding.AuthFragmentBinding

class AuthFragment : Fragment() {
    var binding: AuthFragmentBinding? = null
    var mViewModel: AuthViewModel? = null
    var RC_SIGN_IN: Int = 6
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
        ServiceLocator.googleLogic!!.auth.init(getActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AuthFragmentBinding.inflate(getLayoutInflater(), container, false)
        binding!!.signInButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                auth(
                    binding!!.editTextTextEmailAddress.getText().toString(),
                    binding!!.editTextTextPassword.getText().toString()
                )
            }
        })
        binding!!.registerButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                register(
                    binding!!.editTextTextEmailAddress.getText().toString(),
                    binding!!.editTextTextPassword.getText().toString()
                )
            }
        })
        setHasOptionsMenu(true)
        return binding!!.getRoot()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        ServiceLocator.googleLogic!!.auth.requestCodeCheck(
            requestCode,
            RC_SIGN_IN,
            data
        )
        ServiceLocator.googleLogic!!.auth.getLastAuthInfo(getActivity() as MainActivity?)
        Navigation.findNavController((getActivity() as MainActivity?)!!.mBinding!!.navHostFragment)
            .popBackStack()
    }

    fun auth(email: String, password: String) {
        Log.d("LOGS", "Auth вызван")
        if (email.length != 0 && password.length != 0) {
            mViewModel!!.auth(email, password)
        }
        ServiceLocator.repository.findUser(email, getActivity())!!
            .observe(viewLifecycleOwner) {
                Log.d("LOGS", "Пользователь найден: " + it)
                ServiceLocator.user = it
                Navigation.findNavController((getActivity() as MainActivity?)!!.mBinding!!.navHostFragment)
                    .popBackStack()
            }
    }

    fun register(email: String, password: String) {
        if (email.length != 0 && password.length != 0) {
            Log.d("LOGS", "Register вызван")
            ServiceLocator.repository.register(User(email, password))!!
                .observe(viewLifecycleOwner){
                    Log.d("LOGS", "Ответ от сервера получен: " + it)
                    auth(email, password)
                }
        }
    }
}