package com.example.medic.Domain.Model

import java.util.*

class UserAccountInfo {
    var id: UUID? = null
    var login: String? = null
    var password: String? = null
    var role: Role? = null
    var user: User? = null

    constructor() {}
    constructor(login: String?, password: String?, role: Role?, user: User?) {
        this.login = login
        this.password = password
        this.role = role
        this.user = user
    }

    enum class Role {
        Administrator, Moderator, User, Guest
    }
}