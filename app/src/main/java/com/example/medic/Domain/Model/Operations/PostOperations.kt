package com.example.medic.Domain.Model.Operations

import androidx.lifecycle.MutableLiveData
import com.example.medic.Domain.Model.Post
import com.example.medic.Domain.Model.User

object PostOperations {
    fun addPost(
        title: String?, text: String?,
        time: String?, user: User?,
        tags: String?, images: MutableList<String?>?
    ): Post {
        val post = Post()
        post.title = title
        post.body = text
        post.tags = tags
        post.user = user
        post.date = time
        post.images = images
        return post
    }
}