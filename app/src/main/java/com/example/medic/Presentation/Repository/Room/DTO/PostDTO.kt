package com.example.medic.Presentation.Repository.Room.DTO

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
import com.example.medic.Presentation.Repository.Room.DTO.PostDTO
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
import com.example.medic.Presentation.Repository.Room.DAO.PostDAO
import com.example.medic.Presentation.Repository.Room.DAO.UserDAO
import com.example.medic.Presentation.Repository.Network.MedicServer.MedicServerAPI
import com.example.medic.Presentation.Repository.Room.DTO.UserDTO
import com.example.medic.Presentation.Repository.PostRoomDatabase
import com.google.gson.GsonBuilder
import com.example.medic.Presentation.Repository.PostRepository
import kotlin.jvm.Volatile
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import androidx.room.*
import com.example.medic.Domain.Model.User

@Entity(
    tableName = "post",
    primaryKeys = ["id"],
    ignoredColumns = ["title", "body", "tags", "user", "date", "images"]
)
class PostDTO: Post() {
    @kotlin.jvm.JvmField
    @ColumnInfo
    var titleDTO: String? = null

    @kotlin.jvm.JvmField
    @ColumnInfo
    var bodyDTO: String? = null

    @kotlin.jvm.JvmField
    @ColumnInfo
    var tagsDTO: String? = null

    @kotlin.jvm.JvmField
    @ColumnInfo
    var userDTO: String? = null

    @kotlin.jvm.JvmField
    @ColumnInfo
    var dateDTO: String? = null

    @kotlin.jvm.JvmField
    @ColumnInfo
    var imagesDTO: String? = null
    override var user: User?
        get() {
            if (super.user == null) {
                super.user = Gson().fromJson(userDTO, User::class.java)
            }
            return super.user
        }
        set(user) {
            super.user = user
            userDTO = Gson().toJson(user)
        }
    override var images: MutableList<String?>?
        get() {
            if (super.images == null || super.images!!.isEmpty()) {
                //super.images = Gson().fromJson(imagesDTO, MutableList<String>::class.java)
                super.images = null
            }
            return super.images
        }
        set(images) {
            super.images = images
            imagesDTO = Gson().toJson(images)
        }
    override var date: String?
        get() {
            if (super.date == null) {
                if (dateDTO != null) {
                    date = dateDTO
                } else {
                    return null
                }
            }
            return super.date
        }
        set(date) {
            dateDTO = date
            super.date = date
        }
    override var title: String?
        get() {
            if (super.title == null) {
                super.title = titleDTO
            }
            return super.title
        }
        set(title) {
            super.title = title
            titleDTO = title
        }
    override var body: String?
        get() {
            if (super.body == null) {
                super.body = bodyDTO
            }
            return super.body
        }
        set(body) {
            super.body = body
            bodyDTO = body
        }
    override var tags: String?
        get() {
            if (super.tags == null) {
                super.tags = tagsDTO
            }
            return super.tags
        }
        set(tags) {
            super.tags = tags
            tagsDTO = tags
        }

    companion object {
        fun convertFromPost(post: Post?): PostDTO {
            val dto = PostDTO()
            dto.id = post!!.id
            dto.title = post.title
            dto.body = post.body
            dto.tags = post.tags
            dto.user = post.user
            dto.date = post.date
            dto.images = post.images
            return dto
        }
    }
}