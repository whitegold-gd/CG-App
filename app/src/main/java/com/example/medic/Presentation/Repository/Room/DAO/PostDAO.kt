package com.example.medic.Presentation.Repository.Room.DAO

import androidx.lifecycle.LiveData
import com.example.medic.Presentation.Repository.Room.DTO.PostDTO
import androidx.room.*

@Dao
interface PostDAO {
    @Insert
    fun addPost(post: PostDTO?)

    @Delete
    fun deletePost(post: PostDTO?)

    @get:Query("SELECT * FROM post")
    val getAllPosts: LiveData<List<PostDTO?>?>?
}