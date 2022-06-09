package com.example.medic.Presentation.Repository.Network.Google.OATH

import com.example.medic.MainActivity
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import android.app.Activity
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import android.util.Log
import com.example.medic.DI.ServiceLocator
import com.example.medic.Domain.Model.User
import com.google.android.gms.tasks.Task

class GoogleAuth constructor() {
    var RC_SIGN_IN: Int = 8
    var mGoogleSignInClient: GoogleSignInClient? = null
    fun init(activity: Activity?) {
        val gso: GoogleSignInOptions =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .build()
        mGoogleSignInClient = GoogleSignIn.getClient(activity!!.applicationContext, gso)
    }

    fun getLastAuthInfo(activity: MainActivity?) {
        val acct: GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(activity!!.applicationContext)
        if (acct != null) {
            val personName: String? = acct.displayName
            val personEmail: String? = acct.email
            ServiceLocator.repository
                .findUser(personEmail, activity)!!
                .observe(activity){
                    if (it == null) {
                        val newUser: User = User()
                        newUser.email = personEmail!!
                        newUser.password = "123"
                        if (personName == null) {
                            newUser.firstName = "anon"
                        } else {
                            newUser.firstName = personName
                        }
                        newUser.role = User.Role.User
                        ServiceLocator.repository.addUser(newUser)
                        ServiceLocator.user = newUser
                    } else {
                        ServiceLocator.user = it
                    }
                }
        }
    }

    val intent: Intent
        get() {
            return mGoogleSignInClient!!.signInIntent
        }

    fun requestCodeCheck(requestCode: Int, RC_SIGN_IN: Int, data: Intent?): Boolean {
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            return handleSignInResult(task)
        }
        return false
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>): Boolean {
        try {
            val account: GoogleSignInAccount = completedTask.getResult(ApiException::class.java)
            return true
        } catch (e: ApiException) {
            Log.w("TAG", "signInResult:failed code=" + e.statusCode)
            return false
        }
    }

    fun signOut(activity: Activity?) {
        mGoogleSignInClient!!.signOut()
            .addOnCompleteListener((activity)!!, object : OnCompleteListener<Void?> {
                public override fun onComplete(task: Task<Void?>) {}
            })
        ServiceLocator.user.role = User.Role.Guest
    }

    companion object {
        private val TAG: String = "GoogleAuth"
    }
}