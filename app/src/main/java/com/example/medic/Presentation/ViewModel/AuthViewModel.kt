package com.example.medic.Presentation.ViewModel

import androidx.lifecycle.ViewModel
import com.example.medic.DI.ServiceLocator
import com.example.medic.Domain.Model.User

class AuthViewModel constructor() : ViewModel() {
    fun auth(login: String, password: String) {
        ServiceLocator.repository.auth(User(login, password))
    }

    fun register(login: String, password: String) {
        ServiceLocator.repository.register(User(login, password))
    }
}