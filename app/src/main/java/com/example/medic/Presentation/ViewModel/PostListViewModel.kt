package com.example.medic.Presentation.ViewModel

import com.example.medic.Domain.Model.Post
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import com.example.medic.Presentation.Repository.Room.DTO.PostDTO
import com.example.medic.DI.ServiceLocator
class PostListViewModel: ViewModel() {
    fun getPostList(): MutableLiveData<List<PostDTO?>>? {
        return ServiceLocator.repository.getAllPosts()
    }

    fun getAllPostsLike(value: String?): MutableLiveData<List<Post?>>? {
        return ServiceLocator.repository.getAllPostsLike(value)
    }
}

