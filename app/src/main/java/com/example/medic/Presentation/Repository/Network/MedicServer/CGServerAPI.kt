package com.example.medic.Presentation.Repository.Network.MedicServer

import com.example.medic.Domain.Model.Post
import com.example.medic.Presentation.Repository.Room.DTO.PostDTO
import com.example.medic.Domain.Model.Comment
import com.example.medic.Domain.Model.User
import retrofit2.Call
import retrofit2.http.*
import java.util.*

interface CGServerAPI {
    @POST("/auth")
    @Headers("Accept: application/json")
    fun auth(@Body user: User?): Call<String?>

    @POST("/register")
    @Headers("Accept: application/json")
    fun register(@Body user: User?): Call<String?>

    @GET("/user/byId")
    @Headers("Accept: application/json")
    fun getInfoById(@Header("Authorization") token: String?,
                    @Query("id") id: UUID?): Call<User?>?

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