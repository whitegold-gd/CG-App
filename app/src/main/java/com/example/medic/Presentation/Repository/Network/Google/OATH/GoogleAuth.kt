package com.example.medic.Presentation.Repository.Network.Google.OATH

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
import com.example.medic.DI.ServiceLocator
import com.example.medic.Domain.Model.User
import com.google.android.gms.tasks.Task

class GoogleAuth constructor() {
    var RC_SIGN_IN: Int = 8
    var mGoogleSignInClient: GoogleSignInClient? = null
    fun init(activity: Activity?) {
        val gso: GoogleSignInOptions =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .build()
        mGoogleSignInClient = GoogleSignIn.getClient(activity!!.applicationContext, gso)
    }

    fun getLastAuthInfo(activity: MainActivity?) {
        val acct: GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(activity!!.applicationContext)
        if (acct != null) {
            val personName: String? = acct.displayName
            val personEmail: String? = acct.email
            ServiceLocator.repository
                .findUser(personEmail, activity)!!
                .observe(activity){
                    if (it == null) {
                        val newUser: User = User()
                        newUser.email = personEmail!!
                        newUser.password = "123"
                        if (personName == null) {
                            newUser.firstName = "anon"
                        } else {
                            newUser.firstName = personName
                        }
                        newUser.role = User.Role.User
                        ServiceLocator.repository.addUser(newUser)
                        ServiceLocator.user = newUser
                    } else {
                        ServiceLocator.user = it
                    }
                }
        }
    }

    val intent: Intent
        get() {
            return mGoogleSignInClient!!.signInIntent
        }

    fun requestCodeCheck(requestCode: Int, RC_SIGN_IN: Int, data: Intent?): Boolean {
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            return handleSignInResult(task)
        }
        return false
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>): Boolean {
        try {
            val account: GoogleSignInAccount = completedTask.getResult(ApiException::class.java)
            return true
        } catch (e: ApiException) {
            Log.w("TAG", "signInResult:failed code=" + e.statusCode)
            return false
        }
    }

    fun signOut(activity: Activity?) {
        mGoogleSignInClient!!.signOut()
            .addOnCompleteListener((activity)!!, object : OnCompleteListener<Void?> {
                public override fun onComplete(task: Task<Void?>) {}
            })
        ServiceLocator.user.role = User.Role.Guest
    }

    companion object {
        private val TAG: String = "GoogleAuth"
    }
}