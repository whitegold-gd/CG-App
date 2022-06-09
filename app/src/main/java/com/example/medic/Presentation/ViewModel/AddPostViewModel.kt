package com.example.medic.Presentation.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.medic.DI.ServiceLocator
import com.example.medic.Domain.Model.Operations.PostOperations.addPost
import com.example.medic.Domain.Model.Post
import java.time.LocalDateTime
import java.util.*
import java.util.function.Predicate
import java.util.stream.Collectors

class AddPostViewModel: ViewModel() {
    fun addPost(
        title: String?,
        body: String?,
        tags: String?,
        images: List<String?>
    ): MutableLiveData<Post?>? {
        val localDateTime: String = LocalDateTime.now().toString()
        val post: Post = addPost(
            title,
            body,
            localDateTime,  /*ServiceLocator.getInstance().getUser(),*/
            null,
            tags,
            images.stream().filter(Predicate { obj: String? -> Objects.nonNull(obj) }).collect(
                Collectors.toList()
            )
        )
        return ServiceLocator.repository.addPost(post)
    }

    fun getCorrectedText(untreatedText: String?): LiveData<String?> {
        return ServiceLocator.profanityChecker!!.getCorrectedBody(untreatedText)
    }
}