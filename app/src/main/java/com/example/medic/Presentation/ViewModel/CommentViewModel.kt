package com.example.medic.Presentation.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import com.example.medic.DI.ServiceLocator
import com.example.medic.Domain.Model.Comment

class CommentViewModel: ViewModel() {
    fun getCommentsByPostId(id: String?): MutableLiveData<List<Comment?>>? {
        return ServiceLocator.repository.getCommentsByPostId(id)
    }

    fun deleteCommentsById(id: String?): MutableLiveData<Boolean?>? {
        return ServiceLocator.repository.deleteCommentsById(id)
    }

    fun addComment(body: String?, userName: String?, date: String?, id: String?): MutableLiveData<Boolean?>? {
        val comment: Comment = Comment(body, userName, date, id)
        return ServiceLocator.repository.addComment(comment)
    }
}