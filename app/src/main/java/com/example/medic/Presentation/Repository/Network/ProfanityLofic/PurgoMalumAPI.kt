package com.example.medic.Presentation.Repository.Network.ProfanityLofic

import com.example.medic.Presentation.Repository.Network.ProfanityLofic.ProfanityChecker.ProfanityResponse
import retrofit2.Call
import retrofit2.http.*

open interface PurgoMalumAPI {
    @GET("json")
    @Headers("Accept: application/json")
    fun listAddresses(
        @Query("text") text: String?,
        @Header("x-rapidapi-host") host: String?,
        @Header("x-rapidapi-key") token: String?
    ): Call<ProfanityResponse?>?
}