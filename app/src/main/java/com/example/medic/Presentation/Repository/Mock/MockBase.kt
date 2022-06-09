package com.example.medic.Presentation.Repository.Mock

import androidx.lifecycle.*
import com.example.medic.Domain.Model.Comment
import com.example.medic.Domain.Model.Post
import com.example.medic.Domain.Model.User
import com.example.medic.Presentation.Repository.RepositoryTasks
import com.example.medic.Presentation.Repository.Room.DTO.PostDTO
import java.time.LocalDateTime
import java.util.function.Predicate

class MockBase constructor() : RepositoryTasks {
    var posts: MutableList<Post>
    override fun auth(user: User?): MutableLiveData<String?>? {
        return null
    }

    override fun register(user: User?): MutableLiveData<String?>? {
        return null
    }

    override fun getAllPosts(): MutableLiveData<List<PostDTO?>>? {
        return null;
    }

    override fun getAllPostsLike(value: String?): MutableLiveData<List<Post?>>? {
        return null;
    }

    override fun addPost(post: Post): MutableLiveData<Post?>? {
        return null
    }

    override fun deletePost(post: Post?): MutableLiveData<Boolean?>? {
        return null
    }

    override fun findPost(id: String?, owner: LifecycleOwner?): MutableLiveData<PostDTO>? {
        return null;
    }

    override fun getCommentsByPostId(id: String?): MutableLiveData<List<Comment?>>? {
        return null;
    }


    override fun deleteCommentsById(id: String?): MutableLiveData<Boolean?>? {
        return null
    }

    override fun addComment(comment: Comment?): MutableLiveData<Boolean?>? {
        return null
    }

    override fun findUser(email: String?, owner: LifecycleOwner?): MutableLiveData<User>? {
        return null;
    }

    override fun findUser(
        email: String?,
        password: String?,
        owner: LifecycleOwner?
    ): MutableLiveData<User>? {
        return null;
    }



    override fun addUser(user: User?) {}
    override fun updateUser(user: User?) {}


    init {
        posts = ArrayList()
        val post1: Post = Post()
        post1.user = User("Nikita", "Ostapeko")
        post1.title = "Введение 1"
        post1.body =
            ("Повседневная практика показывает, что сложившаяся структура организации " +
                    "позволяет выполнять важные задания по разработке модели развития." +
                    "\n" +
                    "Равным образом сложившаяся структура организации в значительной степени " +
                    "обуславливает создание форм развития. Равным образом рамки и место " +
                    "обучения кадров позволяет выполнять важные задания по разработке " +
                    "существенных финансовых и административных условий." +
                    "\n" +
                    "Повседневная практика показывает, что сложившаяся структура организации " +
                    "представляет собой интересный эксперимент проверки дальнейших " +
                    "направлений развития. Товарищи! постоянное " +
                    "информационно-пропагандистское " +
                    "обеспечение нашей деятельности позволяет выполнять важные задания по " +
                    "разработке существенных финансовых и административных условий.")

        post1.tags = "Tags6, tags7, tags8, tags9"
        post1.date = LocalDateTime.of(2002, 7, 25, 7, 0, 0).toString()
        posts.add(post1)
        val post2: Post = Post()
        post1.user = User("Nikita", "Ostapeko")
        post1.title = "Введение 2"
        post1.body =
            ("Повседневная практика показывает, что сложившаяся структура организации " +
                    "позволяет выполнять важные задания по разработке модели развития." +
                    "\n" +
                    "Равным образом сложившаяся структура организации в значительной степени " +
                    "обуславливает создание форм развития. Равным образом рамки и место " +
                    "обучения кадров позволяет выполнять важные задания по разработке " +
                    "существенных финансовых и административных условий." +
                    "\n" +
                    "Повседневная практика показывает, что сложившаяся структура организации " +
                    "представляет собой интересный эксперимент проверки дальнейших " +
                    "направлений развития. Товарищи! постоянное " +
                    "информационно-пропагандистское " +
                    "обеспечение нашей деятельности позволяет выполнять важные задания по " +
                    "разработке существенных финансовых и административных условий.")

        post1.tags = "Tags1 tags2, tags3, tags4"
        post2.date = LocalDateTime.of(2002, 7, 25, 8, 0, 0).toString()
        posts.add(post2)
        val post3: Post = Post()
        post3.user = User("Nikita", "Ostapeko")
        post3.title = "Введение 1"
        post3.body =
            ("Повседневная практика показывает, что сложившаяся структура организации " +
                    "позволяет выполнять важные задания по разработке модели развития." +
                    "\n" +
                    "Равным образом сложившаяся структура организации в значительной степени " +
                    "обуславливает создание форм развития. Равным образом рамки и место " +
                    "обучения кадров позволяет выполнять важные задания по разработке " +
                    "существенных финансовых и административных условий." +
                    "\n" +
                    "Повседневная практика показывает, что сложившаяся структура организации " +
                    "представляет собой интересный эксперимент проверки дальнейших " +
                    "направлений развития. Товарищи! постоянное " +
                    "информационно-пропагандистское " +
                    "обеспечение нашей деятельности позволяет выполнять важные задания по " +
                    "разработке существенных финансовых и административных условий.")

        post3.tags = "Tags2, tags1, tags4, tags3"
        post3.date = LocalDateTime.of(2002, 7, 25, 9, 0, 0).toString()
        posts.add(post3)
    }
}