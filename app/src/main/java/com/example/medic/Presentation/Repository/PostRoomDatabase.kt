package com.example.medic.Presentation.Repository

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
import android.content.Context
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
import com.example.medic.Domain.Model.User
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Database(entities = [PostDTO::class, UserDTO::class], version = 1, exportSchema = false)
abstract class PostRoomDatabase constructor() : RoomDatabase() {
    abstract fun postDAO(): PostDAO?
    abstract fun userDAO(): UserDAO

    companion object {
        @Volatile
        private var instance: PostRoomDatabase? = null
        private val NUMBER_OF_THREADS: Int = 3
        val databaseWriteExecutor: ExecutorService = Executors.newFixedThreadPool(
            NUMBER_OF_THREADS
        )

        fun getDatabase(context: Context): PostRoomDatabase? {
            if (instance == null) {
                synchronized(PostRoomDatabase::class.java) {
                    instance = Room.databaseBuilder(
                        context.getApplicationContext(),
                        PostRoomDatabase::class.java, "posts_database"
                    )
                        .allowMainThreadQueries().addCallback(object : Callback() {
                            override fun onCreate(db: SupportSQLiteDatabase) {
                                super.onCreate(db)
                                databaseWriteExecutor.execute {
                                    val admin: UserDTO = UserDTO()
                                    admin.email = "bonnie.terebko@gmail.com"
                                    admin.password = "admin"
                                    admin.firstName = "admin"
                                    admin.role = User.Role.Administrator
                                    getDatabase(context)!!.userDAO().addUser(admin)
                                    val moder: UserDTO = UserDTO()
                                    moder.email = "taleforhelen@gmail.com"
                                    moder.password = "moder"
                                    moder.firstName = "moder"
                                    moder.role = User.Role.Moderator
                                    getDatabase(context)!!.userDAO().addUser(moder)
                                }
                            }
                        }).build()
                }
            }
            return instance
        }
    }
}