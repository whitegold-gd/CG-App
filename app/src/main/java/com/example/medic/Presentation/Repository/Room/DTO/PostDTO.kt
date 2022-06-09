package com.example.medic.Presentation.Repository.Room.DTO

import com.example.medic.Domain.Model.Post
import com.google.gson.Gson
import androidx.room.*
import com.example.medic.Domain.Model.User

@Entity(
    tableName = "post",
    primaryKeys = ["id"],
    ignoredColumns = ["title", "body", "tags", "user", "date", "images"]
)
class PostDTO: Post() {
    @kotlin.jvm.JvmField
    @ColumnInfo
    var titleDTO: String? = null

    @kotlin.jvm.JvmField
    @ColumnInfo
    var bodyDTO: String? = null

    @kotlin.jvm.JvmField
    @ColumnInfo
    var tagsDTO: String? = null

    @kotlin.jvm.JvmField
    @ColumnInfo
    var userDTO: String? = null

    @kotlin.jvm.JvmField
    @ColumnInfo
    var dateDTO: String? = null

    @kotlin.jvm.JvmField
    @ColumnInfo
    var imagesDTO: String? = null
    override var user: User?
        get() {
            if (super.user == null) {
                super.user = Gson().fromJson(userDTO, User::class.java)
            }
            return super.user
        }
        set(user) {
            super.user = user
            userDTO = Gson().toJson(user)
        }
    override var images: MutableList<String?>?
        get() {
            if (super.images == null || super.images!!.isEmpty()) {
                //super.images = Gson().fromJson(imagesDTO, MutableList<String>::class.java)
                super.images = null
            }
            return super.images
        }
        set(images) {
            super.images = images
            imagesDTO = Gson().toJson(images)
        }
    override var date: String?
        get() {
            if (super.date == null) {
                if (dateDTO != null) {
                    date = dateDTO
                } else {
                    return null
                }
            }
            return super.date
        }
        set(date) {
            dateDTO = date
            super.date = date
        }
    override var title: String?
        get() {
            if (super.title == null) {
                super.title = titleDTO
            }
            return super.title
        }
        set(title) {
            super.title = title
            titleDTO = title
        }
    override var body: String?
        get() {
            if (super.body == null) {
                super.body = bodyDTO
            }
            return super.body
        }
        set(body) {
            super.body = body
            bodyDTO = body
        }
    override var tags: String?
        get() {
            if (super.tags == null) {
                super.tags = tagsDTO
            }
            return super.tags
        }
        set(tags) {
            super.tags = tags
            tagsDTO = tags
        }

    companion object {
        fun convertFromPost(post: Post?): PostDTO {
            val dto = PostDTO()
            dto.id = post!!.id
            dto.title = post.title
            dto.body = post.body
            dto.tags = post.tags
            dto.user = post.user
            dto.date = post.date
            dto.images = post.images
            return dto
        }
    }
}