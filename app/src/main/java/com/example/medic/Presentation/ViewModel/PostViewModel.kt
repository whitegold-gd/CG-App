package com.example.medic.Presentation.ViewModel

import com.example.medic.Domain.Model.Post
import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import com.example.medic.DI.ServiceLocator

class PostViewModel: ViewModel() {
    var post: Post? = null
    fun deletePost(post: Post?): LiveData<Boolean?>? {
        return ServiceLocator.repository.deletePost(post)
    }
}