package com.example.medic.Presentation.View

import com.example.medic.Domain.Model.Operations.PostOperations.addPost
import androidx.recyclerview.widget.RecyclerView
import com.example.medic.Domain.Model.Post
import android.widget.TextView
import com.example.medic.MainActivity
import android.view.ViewGroup
import android.view.LayoutInflater
import android.annotation.SuppressLint
import android.os.Bundle
import com.example.medic.R
import com.example.medic.Presentation.View.Adapters.ImageSliderAdapter.ImageSliderViewHolder
import androidx.activity.result.contract.ActivityResultContracts.OpenDocument
import androidx.activity.result.ActivityResultCallback
import android.content.Intent
import android.graphics.BitmapFactory
import android.view.View.MeasureSpec
import androidx.recyclerview.widget.LinearSmoothScroller
import android.util.DisplayMetrics
import com.example.medic.Presentation.View.CustomViews.CustomRecycleView
import com.example.medic.Presentation.ViewModel.AddPostViewModel
import androidx.lifecycle.ViewModelProvider
import android.widget.Toast
import com.example.medic.Presentation.View.AddPost
import com.example.medic.Presentation.ViewModel.PostListViewModel
import com.example.medic.Presentation.View.Adapters.PostListAdapter
import androidx.recyclerview.widget.GridLayoutManager
import android.view.MenuInflater
import com.example.medic.Presentation.ViewModel.AuthViewModel
import com.example.medic.Presentation.ViewModel.PostViewModel
import com.example.medic.Presentation.ViewModel.CommentViewModel
import com.example.medic.Presentation.View.Adapters.CommentListAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.medic.Presentation.View.PostFragment
import com.example.medic.Presentation.ViewModel.ProfileViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import com.example.medic.Domain.Model.Operations.PostOperations
import com.example.medic.Presentation.Repository.RepositoryTasks
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LifecycleOwner
import androidx.room.Dao
import com.example.medic.Presentation.Repository.Room.DTO.PostDTO
import androidx.room.Delete
import androidx.room.Update
import androidx.room.ColumnInfo
import com.google.gson.Gson
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import android.app.Activity
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.example.medic.Presentation.Repository.Network.Google.OATH.GoogleAuth
import retrofit2.http.POST
import retrofit2.http.GET
import com.example.medic.Presentation.Repository.Network.ProfanityLofic.ProfanityChecker.ProfanityResponse
import com.example.medic.Presentation.Repository.Network.ProfanityLofic.PurgoMalumAPI
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.app.Application
import android.util.Log
import android.view.View
import com.example.medic.Presentation.Repository.Room.DAO.PostDAO
import com.example.medic.Presentation.Repository.Room.DAO.UserDAO
import com.example.medic.Presentation.Repository.Network.MedicServer.MedicServerAPI
import com.example.medic.Presentation.Repository.Room.DTO.UserDTO
import com.example.medic.Presentation.Repository.PostRoomDatabase
import com.google.gson.GsonBuilder
import com.example.medic.Presentation.Repository.PostRepository
import androidx.room.Database
import androidx.room.RoomDatabase
import kotlin.jvm.Volatile
import androidx.room.Room
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
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