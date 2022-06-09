package com.example.medic.Domain.Model

import java.util.*

open class Post {
    var id: String
    open var title: String? = null
    open var body: String? = null
    open var tags: String? = null
    open var user: User? = null
    open var date: String? = null
    open var images: MutableList<String?>? = null

    init {
        id = UUID.randomUUID().toString()
    }
}