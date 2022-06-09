package com.example.medic.Presentation.Repository.Network.MedicServer

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
import com.example.medic.Presentation.Repository.Network.ProfanityLofic.ProfanityChecker.ProfanityResponse
import com.example.medic.Presentation.Repository.Network.ProfanityLofic.PurgoMalumAPI
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.app.Application
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
import com.example.medic.Domain.Model.Comment
import com.example.medic.Domain.Model.User
import retrofit2.Call
import retrofit2.http.*
import java.util.*

open interface MedicServerAPI {
    @POST("/auth")
    @Headers("Accept: application/json")
    fun auth(@Body user: User?): Call<String?>

    @POST("/register")
    @Headers("Accept: application/json")
    fun register(@Body user: User?): Call<String?>

    @GET("/user/byId")
    @Headers("Accept: application/json")
    fun getInfoById(@Header("Authorization") token: String?, @Query("id") id: UUID?): Call<User?>?

    @GET("/user/byEmail")
    @Headers("Accept: application/json")
    fun getInfoByEmail(
        @Header("Authorization") token: String?,
        @Query("email") email: String?
    ): Call<User?>

    @GET("/comment/byPostId")
    @Headers("Accept: application/json")
    fun getCommentsByPostId(@Query("id") id: String?): Call<List<Comment?>?>

    @GET("/comment/deleteById")
    @Headers("Accept: application/json")
    fun deleteCommentById(
        @Query("id") id: String?,
        @Header("Authorization") token: String?
    ): Call<Boolean?>

    @POST("/comment/add")
    @Headers("Accept: application/json")
    fun addNewComment(@Body comment: Comment?): Call<Comment?>

    @get:Headers("Accept: application/json")
    @get:GET("/post/all")
    val allPosts: Call<List<PostDTO?>?>

    @GET("/post/search")
    @Headers("Accept: application/json")
    fun getAllPostsLike(@Query("value") value: String?): Call<List<PostDTO?>?>

    @POST("/post/add")
    @Headers("Accept: application/json")
    fun addNewPostToList(@Body post: Post?, @Header("Authorization") token: String?): Call<Post?>

    @GET("/post/delete")
    @Headers("Accept: application/json")
    fun deletePostById(
        @Query("id") id: String?,
        @Header("Authorization") token: String?
    ): Call<Boolean?>

    @GET("/post/byId")
    @Headers("Accept: application/json")
    fun getPostById(@Query("id") id: UUID?): Call<Post?>

    @GET("/post/share")
    @Headers("Accept: application/json")
    fun getLinkToShare(@Query("id") id: UUID?): Call<String?>?
}