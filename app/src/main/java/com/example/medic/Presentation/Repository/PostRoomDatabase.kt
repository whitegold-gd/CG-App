package com.example.medic.Presentation.Repository

import com.example.medic.Presentation.Repository.Room.DTO.PostDTO
import android.content.Context
import com.example.medic.Presentation.Repository.Room.DAO.PostDAO
import com.example.medic.Presentation.Repository.Room.DAO.UserDAO
import com.example.medic.Presentation.Repository.Room.DTO.UserDTO
import androidx.room.Database
import androidx.room.RoomDatabase
import kotlin.jvm.Volatile
import androidx.room.Room
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.medic.Domain.Model.User
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Database(entities = [PostDTO::class, UserDTO::class], version = 1, exportSchema = false)
abstract class PostRoomDatabase constructor() : RoomDatabase() {
    abstract fun postDAO(): PostDAO?
    abstract fun userDAO(): UserDAO

    companion object {
        @Volatile
        private var instance: PostRoomDatabase? = null
        private val NUMBER_OF_THREADS: Int = 3
        val databaseWriteExecutor: ExecutorService = Executors.newFixedThreadPool(
            NUMBER_OF_THREADS
        )

        fun getDatabase(context: Context): PostRoomDatabase? {
            if (instance == null) {
                synchronized(PostRoomDatabase::class.java) {
                    instance = Room.databaseBuilder(
                        context.getApplicationContext(),
                        PostRoomDatabase::class.java, "posts_database"
                    )
                        .allowMainThreadQueries().addCallback(object : Callback() {
                            override fun onCreate(db: SupportSQLiteDatabase) {
                                super.onCreate(db)
                                databaseWriteExecutor.execute {
                                    val admin: UserDTO = UserDTO()
                                    admin.email = "bonnie.terebko@gmail.com"
                                    admin.password = "admin"
                                    admin.firstName = "admin"
                                    admin.role = User.Role.Administrator
                                    getDatabase(context)!!.userDAO().addUser(admin)
                                    val moder: UserDTO = UserDTO()
                                    moder.email = "taleforhelen@gmail.com"
                                    moder.password = "moder"
                                    moder.firstName = "moder"
                                    moder.role = User.Role.Moderator
                                    getDatabase(context)!!.userDAO().addUser(moder)
                                }
                            }
                        }).build()
                }
            }
            return instance
        }
    }
}