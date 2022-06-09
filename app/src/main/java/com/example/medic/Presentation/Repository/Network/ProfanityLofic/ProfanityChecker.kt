package com.example.medic.Presentation.Repository.Network.ProfanityLofic

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfanityChecker constructor() {
    private val api: PurgoMalumAPI
    private val API_KEY: String = "29bb57a21fmsh971770ea1c6d2a3p14da05jsn492b8dd6c6b5"
    private val API_HOST: String = "community-purgomalum.p.rapidapi.com"

    internal class ProfanityRequest constructor(var value: String)
    class ProfanityResponse constructor() {
        var result: String? = null
    }

    fun getCorrectedBody(body: String?): LiveData<String?> {
        val correctedText: MutableLiveData<String?> = MutableLiveData()
        val call: Call<ProfanityResponse?>? = api.listAddresses(body, API_HOST, API_KEY)
        call!!.enqueue(object : Callback<ProfanityResponse?> {
            override fun onResponse(
                call: Call<ProfanityResponse?>,
                response: Response<ProfanityResponse?>
            ) {
                if (response.isSuccessful() && response.body() != null) {
                    correctedText.setValue(response.body()!!.result)
                }
            }

            override fun onFailure(call: Call<ProfanityResponse?>, t: Throwable) {}
        })
        return correctedText
    }

    init {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://community-purgomalum.p.rapidapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        api = retrofit.create(PurgoMalumAPI::class.java)
    }
}