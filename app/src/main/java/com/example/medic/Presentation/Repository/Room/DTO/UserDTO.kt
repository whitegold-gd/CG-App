package com.example.medic.Presentation.Repository.Room.DTO

import androidx.room.*
import com.example.medic.Domain.Model.User

@Entity(tableName = "user", primaryKeys = ["id"])
class UserDTO : User() {
    companion object {
        fun convertFromUser(user: User?): UserDTO {
            val dto = UserDTO()
            dto.id = user!!.id
            dto.firstName = user.firstName
            dto.lastName = user.lastName
            dto.email = user.email
            dto.password = user.password
            dto.role = user.role
            return dto
        }
    }
}