package com.example.medic.Presentation.Repository.Room.DAO

import androidx.lifecycle.LiveData
import com.example.medic.Presentation.Repository.Room.DTO.UserDTO
import androidx.room.*

@Dao
interface UserDAO {
    @Insert
    fun addUser(user: UserDTO?)

    @Query("SELECT * FROM user WHERE email = :email")
    fun getUserByEmail(email: String?): LiveData<UserDTO?>?

    @Query("SELECT * FROM user WHERE email = :email AND password = :password")
    fun getUserByEmailAndPassword(email: String?, password: String?): LiveData<UserDTO?>?

    @get:Query("SELECT * FROM user")
    val allUsers: LiveData<List<UserDTO?>?>?

    @Update
    fun updateUserInfo(user: UserDTO?)
}