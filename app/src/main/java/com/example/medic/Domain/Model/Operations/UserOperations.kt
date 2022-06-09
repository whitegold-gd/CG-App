package com.example.medic.Domain.Model.Operations

import com.example.medic.Domain.Model.User

object UserOperations {
    fun addUser(
        firstName: String, secondName: String,
        email: String, password: String
    ): User {
        val user = User()
        user.firstName = firstName
        user.lastName = secondName
        user.email = email
        user.password = password
        return user
    }
}