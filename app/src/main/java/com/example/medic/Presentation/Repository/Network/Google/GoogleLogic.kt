package com.example.medic.Presentation.Repository.Network.Google

import com.example.medic.Presentation.Repository.Network.Google.OATH.GoogleAuth

class GoogleLogic constructor() {
    var auth: GoogleAuth
    val personInfo: Unit
        get() {}

    init {
        auth = GoogleAuth()
    }
}