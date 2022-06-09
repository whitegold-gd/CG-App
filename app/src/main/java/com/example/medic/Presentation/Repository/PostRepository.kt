package com.example.medic.Presentation.Repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.medic.DI.ServiceLocator
import com.example.medic.Domain.Model.Comment
import com.example.medic.Domain.Model.Post
import com.example.medic.Domain.Model.User
import com.example.medic.Presentation.Repository.Network.MedicServer.MedicServerAPI
import com.example.medic.Presentation.Repository.Room.DAO.PostDAO
import com.example.medic.Presentation.Repository.Room.DAO.UserDAO
import com.example.medic.Presentation.Repository.Room.DTO.PostDTO
import com.example.medic.Presentation.Repository.Room.DTO.UserDTO
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class PostRepository constructor(application: Application) : RepositoryTasks {
    private val postDAO: PostDAO?
    private val userDAO: UserDAO?
    private val posts: LiveData<List<PostDTO?>?>?
    private val api: MedicServerAPI

    companion object {
        private val HOST: String = "https://medic-server-cubd-test.herokuapp.com/"
    }

    init {
        val gson: Gson = GsonBuilder()
            .setLenient()
            .create()
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(HOST)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        api = retrofit.create(MedicServerAPI::class.java)
        val database: PostRoomDatabase? = PostRoomDatabase.getDatabase(application)
        postDAO = database!!.postDAO()
        userDAO = database.userDAO()
        posts = postDAO!!.getAllPosts
    }

    override fun auth(user: User?): MutableLiveData<String?> {
        val body: MutableLiveData<String?> = MutableLiveData()
        api.auth(user).enqueue(object : Callback<String?> {
            override fun onResponse(call: Call<String?>, response: Response<String?>) {
                ServiceLocator.token = response.body()!!
                body.setValue(response.body())
            }

            override fun onFailure(call: Call<String?>, t: Throwable) {
                t.printStackTrace()
            }
        })
        return body
    }

    override fun register(user: User?): MutableLiveData<String?> {
        val body: MutableLiveData<String?> = MutableLiveData()
        api.register(user).enqueue(object : Callback<String?> {
            override fun onResponse(call: Call<String?>, response: Response<String?>) {
                ServiceLocator.token = response.body()!!
                body.setValue(response.body())
            }

            override fun onFailure(call: Call<String?>, t: Throwable) {
                t.printStackTrace()
            }
        })
        return body
    }

    override fun getAllPostsLike(value: String?): MutableLiveData<List<Post?>> {
        val allSuitablePosts: MutableLiveData<List<Post?>> = MutableLiveData()
        api.getAllPostsLike(value).enqueue(object : Callback<List<PostDTO?>?> {
            override fun onResponse(
                call: Call<List<PostDTO?>?>,
                response: Response<List<PostDTO?>?>
            ) {
                allSuitablePosts.setValue(response.body())
            }

            override fun onFailure(call: Call<List<PostDTO?>?>, t: Throwable) {
                t.printStackTrace()
                //allPosts.setValue(posts);
            }
        })
        return allSuitablePosts
    }

    override fun getAllPosts(): MutableLiveData<List<PostDTO?>> {
        val allPosts: MutableLiveData<List<PostDTO?>> = MutableLiveData()
        api.allPosts.enqueue(object : Callback<List<PostDTO?>?> {
            override fun onResponse(
                call: Call<List<PostDTO?>?>,
                response: Response<List<PostDTO?>?>
            ) {
                allPosts.setValue(response.body())
            }

            override fun onFailure(call: Call<List<PostDTO?>?>, t: Throwable) {
                t.printStackTrace()
                //allPosts.setValue(posts);
            }
        })
        return allPosts
    }

    override fun addPost(post: Post): MutableLiveData<Post?> {
        val dto: PostDTO = PostDTO.convertFromPost(post)
        val liveData: MutableLiveData<Post?> = MutableLiveData()
        /*PostRoomDatabase.databaseWriteExecutor.execute(() -> {
            postDAO.addPost(dto);
        });*/Log.d(
            "LOG", (dto.title + "\n" +
                    dto.body + "\n" +
                    dto.tags + "\n" +
                    dto.user + "\n" +
                    dto.date + "\n" +
                    dto.images + "\n")
        )
        api.addNewPostToList(dto, ServiceLocator.token)
            .enqueue(object : Callback<Post?> {
                override fun onResponse(call: Call<Post?>, response: Response<Post?>) {
                    liveData.setValue(response.body())
                    println("Post added")
                    Log.d("LOGS", response.toString())
                }

                override fun onFailure(call: Call<Post?>, t: Throwable) {
                    println("Post adding failure")
                }
            })
        return liveData
    }

    override fun deletePost(post: Post?): MutableLiveData<Boolean?> {
        val bool: MutableLiveData<Boolean?> = MutableLiveData()
        Log.d("LOGS", "Вызван deletePost")
        api.deletePostById(post!!.id, ServiceLocator.token)
            .enqueue(object : Callback<Boolean?> {
                override fun onResponse(call: Call<Boolean?>, response: Response<Boolean?>) {
                    if (response.isSuccessful && response.body() != null) {
                        Log.d("LOGS", response.body().toString())
                    } else {
                        Log.d("LOGS", "Failed, response is null")
                    }
                    bool.setValue(response.body())
                }

                override fun onFailure(call: Call<Boolean?>, t: Throwable) {
                    t.printStackTrace()
                    bool.setValue(false)
                    /*PostRoomDatabase.databaseWriteExecutor.execute(() -> {
                            postDAO.deletePost(dto);
                        });*/
                }
            })
        return bool
    }

    override fun findPost(id: String?, owner: LifecycleOwner?): MutableLiveData<PostDTO> {
        val specificPost: MutableLiveData<PostDTO> = MutableLiveData()
        api.getPostById(UUID.fromString(id)).enqueue(object : Callback<Post?> {
            override fun onResponse(call: Call<Post?>, response: Response<Post?>) {
                if (response.isSuccessful && response.body() != null) {
                    specificPost.setValue(response.body() as PostDTO?)
                }
            }

            override fun onFailure(call: Call<Post?>, t: Throwable) {
                t.printStackTrace()
                /*posts.observe(owner, new Observer<List<PostDTO>>() {
                    @Override
                    public void onChanged(List<PostDTO> listParties) {
                        specificPost.setValue(listParties.stream()
                                .filter(new Predicate<PostDTO>() {
                                    @Override
                                    public boolean test(PostDTO postDTO) {
                                        return id.equals(postDTO.getId());
                                    }
                                })
                                .findAny()
                                .orElse(null));
                    }
                });*/
            }
        })
        return specificPost
    }

    override fun getCommentsByPostId(id: String?): MutableLiveData<List<Comment?>> {
        val comments: MutableLiveData<List<Comment?>> = MutableLiveData()
        api.getCommentsByPostId(id).enqueue(object : Callback<List<Comment?>?> {
            override fun onResponse(
                call: Call<List<Comment?>?>,
                response: Response<List<Comment?>?>
            ) {
                comments.setValue(response.body())
            }

            override fun onFailure(call: Call<List<Comment?>?>, t: Throwable) {
                t.printStackTrace()
            }
        })
        return comments
    }

    override fun deleteCommentsById(id: String?): MutableLiveData<Boolean?> {
        val bool: MutableLiveData<Boolean?> = MutableLiveData()
        api.deleteCommentById(id, ServiceLocator.token)
            .enqueue(object : Callback<Boolean?> {
                override fun onResponse(call: Call<Boolean?>, response: Response<Boolean?>) {
                    bool.setValue(response.body())
                }

                override fun onFailure(call: Call<Boolean?>, t: Throwable) {
                    t.printStackTrace()
                    bool.setValue(false)
                    /*PostRoomDatabase.databaseWriteExecutor.execute(() -> {
                            postDAO.deletePost(dto);
                        });*/
                }
            })
        return bool
    }

    override fun addComment(comment: Comment?): MutableLiveData<Boolean?> {
        val answer: MutableLiveData<Boolean?> = MutableLiveData()
        api.addNewComment(comment)
            .enqueue(object : Callback<Comment?> {
                override fun onResponse(call: Call<Comment?>, response: Response<Comment?>) {
                    answer.setValue(true)
                    Log.d("LOGS", response.toString())
                }

                override fun onFailure(call: Call<Comment?>, t: Throwable) {
                    answer.setValue(false)
                    println("Post adding failure")
                }
            })
        return answer
    }

    override fun findUser(email: String?, owner: LifecycleOwner?): MutableLiveData<User> {
        val answer: MutableLiveData<User> = MutableLiveData()
        api.getInfoByEmail(ServiceLocator.token, email)
            .enqueue(object : Callback<User?> {
                override fun onResponse(call: Call<User?>, response: Response<User?>) {
                    if (response.isSuccessful() && response.body() != null) {
                        answer.setValue(UserDTO.convertFromUser(response.body()))
                    }
                }

                override fun onFailure(call: Call<User?>, t: Throwable) {
                    t.printStackTrace()
                }
            })

        /*userDAO.getUserByEmail(email).observe(owner, new Observer<UserDTO>() {
            @Override
            public void onChanged(UserDTO userDTO) {
                answer.setValue(userDTO);
            }
        });*/return answer
    }

    override fun findUser(
        email: String?,
        password: String?,
        owner: LifecycleOwner?
    ): MutableLiveData<User> {
        val answer: MutableLiveData<User> = MutableLiveData()
        api.getInfoByEmail(ServiceLocator.token, email)
            .enqueue(object : Callback<User?> {
                override fun onResponse(call: Call<User?>, response: Response<User?>) {
                    answer.setValue(response.body() as UserDTO?)
                }

                override fun onFailure(call: Call<User?>, t: Throwable) {
                    t.printStackTrace()
                }
            })

        /*userDAO.getUserByEmailAndPassword(email, password).observe(owner, new Observer<UserDTO>() {
            @Override
            public void onChanged(UserDTO userDTO) {
                answer.setValue(userDTO);
            }
        });*/return answer
    }

    override fun addUser(user: User?) {
        val dto: UserDTO = UserDTO.convertFromUser(user)
        PostRoomDatabase.databaseWriteExecutor.execute(object : Runnable {
            override fun run() {
                userDAO!!.addUser(dto)
            }
        })
    }

    override fun updateUser(user: User?) {
        val dto: UserDTO = UserDTO.convertFromUser(user)
        PostRoomDatabase.databaseWriteExecutor.execute(object : Runnable {
            override fun run() {
                userDAO!!.updateUserInfo(dto)
            }
        })
    }
}