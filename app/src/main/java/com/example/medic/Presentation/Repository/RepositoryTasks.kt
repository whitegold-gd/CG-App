package com.example.medic.Presentation.Repository

import com.example.medic.Domain.Model.Post
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LifecycleOwner
import com.example.medic.Presentation.Repository.Room.DTO.PostDTO
import com.example.medic.Domain.Model.Comment
import com.example.medic.Domain.Model.User

interface RepositoryTasks {
    fun auth(user: User?): MutableLiveData<String?>?
    fun register(user: User?): MutableLiveData<String?>?
    fun getAllPosts(): MutableLiveData<List<PostDTO?>>?
    fun getAllPostsLike(value: String?): MutableLiveData<List<Post?>>?
    fun addPost(post: Post): MutableLiveData<Post?>?
    fun deletePost(post: Post?): MutableLiveData<Boolean?>?
    fun findPost(id: String?, owner: LifecycleOwner?): MutableLiveData<PostDTO>?
    fun getCommentsByPostId(id: String?): MutableLiveData<List<Comment?>>?
    fun deleteCommentsById(id: String?): MutableLiveData<Boolean?>?
    fun addComment(comment: Comment?): MutableLiveData<Boolean?>?
    fun findUser(email: String?, owner: LifecycleOwner?): MutableLiveData<User>?
    fun findUser(email: String?, password: String?, owner: LifecycleOwner?): MutableLiveData<User>?
    fun addUser(user: User?)
    fun updateUser(user: User?)
}