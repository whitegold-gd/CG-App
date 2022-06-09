package com.example.medic

import com.example.medic.Domain.Model.Post
import android.os.Bundle
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import androidx.navigation.Navigation
import com.example.medic.DI.ServiceLocator
import com.example.medic.Domain.Model.User
import com.example.medic.databinding.ActivityMainBinding

class MainActivity: AppCompatActivity() {
    var mBinding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(getLayoutInflater())
        setContentView(mBinding!!.getRoot())
        ServiceLocator.init(getApplication())
        ServiceLocator.googleLogic!!.auth.init(this)
        val income: Uri? = getIntent().getData()
        if (income != null) {
            val parts: Array<String> = income.toString().split("/").toTypedArray()
            val id: String = parts.get(parts.size - 1)
            ServiceLocator.repository.findPost(id, this)!!
                .observe(this, object : Observer<Post?> {
                    public override fun onChanged(post: Post?) {
                        val bundle: Bundle = Bundle()
                        bundle.putString(
                            "Post",
                            ServiceLocator.gson!!.toJson(post)
                        )
                        Navigation.findNavController(mBinding!!.navHostFragment).navigate(
                            R.id.action_postList_to_postFragment, bundle
                        )
                    }
                })
        }
        setupUser()
    }

    fun setupUser() {
        val user = User()
        user.role = User.Role.Guest
        user.firstName = "Anon"
        ServiceLocator.user = user
    }

    companion object {
        private val TAG: String = "MainActivity"
    }
}