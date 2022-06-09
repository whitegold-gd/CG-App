package com.example.medic.Presentation.View.Adapters

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
import android.net.Uri
import android.view.View
import android.widget.ImageView
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
import java.io.FileNotFoundException

class ImageSliderAdapter constructor(
    var images: MutableList<String?>,
    adding: Boolean,
    var mActivity: MainActivity?
) : RecyclerView.Adapter<ImageSliderViewHolder>() {
    var imageView: ImageView? = null
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ImageSliderViewHolder {
        return ImageSliderViewHolder(
            LayoutInflater.from(parent.getContext()).inflate(R.layout.image_element, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ImageSliderViewHolder, position: Int) {
        if (images.get(position) == null) {
            holder.view.findViewById<View>(R.id.imageContent).setVisibility(View.GONE)
            holder.view.findViewById<View>(R.id.addButtonImage).setVisibility(View.VISIBLE)
            holder.view.findViewById<View>(R.id.addButtonImage)
                .setOnClickListener(object : View.OnClickListener {
                    override fun onClick(v: View) {
                        if (mActivity != null) {
                            mActivity!!.getActivityResultRegistry().register("key",
                                OpenDocument(),
                                object : ActivityResultCallback<Uri> {
                                    override fun onActivityResult(result: Uri) {
                                        mActivity!!.getApplicationContext().getContentResolver()
                                            .takePersistableUriPermission(
                                                result,
                                                Intent.FLAG_GRANT_READ_URI_PERMISSION
                                            )
                                        images.add(images.size - 1, result.toString())
                                        notifyDataSetChanged()
                                    }
                                }).launch(arrayOf("image/*"))
                        }
                    }
                })
        } else {
            holder.view.findViewById<View>(R.id.addButtonImage).setVisibility(View.GONE)
            holder.view.findViewById<View>(R.id.imageContent).setVisibility(View.VISIBLE)
            if (mActivity != null) {
                try {
                    imageView = holder.view.findViewById(R.id.imageContent)
                    imageView!!.setImageBitmap(
                        BitmapFactory.decodeFileDescriptor(
                            mActivity!!.getApplicationContext().getContentResolver()
                                .openFileDescriptor(
                                    Uri.parse(images.get(position)), "r"
                                )!!.getFileDescriptor()
                        )
                    )
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return images.size
    }

    inner class ImageSliderViewHolder constructor(var view: View) : RecyclerView.ViewHolder(
        view
    )

    init {
        if (adding) {
            images.add(null)
        }
    }
}