package com.example.medic.Presentation.View

import android.view.ViewGroup
import android.view.LayoutInflater
import android.os.Bundle
import com.example.medic.R
import androidx.lifecycle.ViewModelProvider
import com.example.medic.Presentation.ViewModel.ProfileViewModel
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.medic.DI.ServiceLocator
import com.example.medic.databinding.ProfileFragmentBinding

class ProfileFragment constructor() : Fragment() {
    var profileViewModel: ProfileViewModel? = null
    lateinit var mBinding: ProfileFragmentBinding
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = ProfileFragmentBinding.inflate(getLayoutInflater(), container, false)
        mBinding.name.setText(ServiceLocator.user.firstName)
        mBinding.nameRole.setText(ServiceLocator.user.role.toString())
        mBinding.firstName.setText(ServiceLocator.user.firstName)
        mBinding.lastName.setText(ServiceLocator.user.lastName)
        mBinding.email.setText(ServiceLocator.user.email)
        mBinding.role.setText(ServiceLocator.user.role.toString())
        mBinding.authButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                Navigation.findNavController(mBinding.getRoot())
                    .navigate(R.id.action_profileFragment_to_authFragment)
            }
        })
        return mBinding.getRoot()
    }

    override fun onDestroy() {
        super.onDestroy()
        profileViewModel = null
    }
}