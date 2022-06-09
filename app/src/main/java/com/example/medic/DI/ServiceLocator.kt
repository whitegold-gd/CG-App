package com.example.medic.DI

import android.app.Application
import com.example.medic.Domain.Model.User
import com.example.medic.Presentation.Repository.Mock.MockBase
import com.example.medic.Presentation.Repository.Network.Google.GoogleLogic
import com.example.medic.Presentation.Repository.Network.ProfanityLofic.ProfanityChecker
import com.example.medic.Presentation.Repository.PostRepository
import com.example.medic.Presentation.Repository.RepositoryTasks
import com.google.gson.*
import java.lang.reflect.Type
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object ServiceLocator {
    private var repositoryTasks: RepositoryTasks? = null
    var profanityChecker: ProfanityChecker? = null
        get() {
            if (field == null) {
                field = ProfanityChecker()
            }
            return field
        }
    lateinit var user: User
    var token: String? = null
    var googleLogic: GoogleLogic? = null
        get() {
            if (field == null) {
                field = GoogleLogic()
            }
            return field
        }

    fun init(application: Application) {
        if (repositoryTasks == null) {
            repositoryTasks = PostRepository(application)
        }
    }

    val repository: RepositoryTasks
        get() {
            if (repositoryTasks == null) {
                repositoryTasks = MockBase()
            }
            return repositoryTasks!!
        }
    var gson: Gson? = null
        get() {
            if (field == null) {
                field = GsonBuilder()
                    .registerTypeAdapter(
                        LocalDateTime::class.java,
                        JsonDeserializer { json, typeOfT, context ->
                            LocalDateTime.parse(
                                json.asString,
                                DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
                            )
                        }
                    )
                    .registerTypeAdapter(
                        LocalDateTime::class.java,
                        JsonSerializer { src: LocalDateTime?, typeOfSrc: Type?, context: JsonSerializationContext? ->
                            JsonPrimitive(
                                DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm").format(src)
                            )
                        }
                    )
                    .create()
            }
            return field
        }
        private set
}