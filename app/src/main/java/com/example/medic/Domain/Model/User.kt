package com.example.medic.Domain.Model

import java.util.*

open class User() {
    var id: String
    lateinit var firstName: String
    lateinit var  lastName: String
    lateinit var  email: String
    lateinit var  password: String
    lateinit var  role: Role

    enum class Role {
        Administrator, Moderator, User, Guest
    }

    constructor(email: String, password: String) : this() {
        this.email = email
        this.password = password
        role = Role.User
        firstName = "Anon"
        lastName = "Anon"
    }

    init {
        id = UUID.randomUUID().toString()
    }
}