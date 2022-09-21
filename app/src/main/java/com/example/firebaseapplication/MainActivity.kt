package com.example.firebaseapplication

import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignInAccount


class MainActivity : AppCompatActivity(), GoogleHelper.GoogleHelperCallback {

    private lateinit var googleHelper: GoogleHelper
    val CUSTOM_PREF_NAME = "User_data"
    var pref: SharedPreferenceHelper? = null
    var buttonLogin: TextView? = null
    var buttonLogOut: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        SharedPreferenceHelper(this).init()
        pref = SharedPreferenceHelper.getInstance()
        googleHelper = GoogleHelper(this, this)
        findIds()
        manageButtons()

    }

    private fun manageButtons() {
        if (pref?.getUserName() != null) {
            buttonLogOut?.visibility = View.VISIBLE
            buttonLogin?.visibility = View.GONE
        } else {
            buttonLogOut?.visibility = View.GONE
            buttonLogin?.visibility = View.VISIBLE
        }
    }

    private fun findIds() {
        buttonLogin = findViewById<TextView>(R.id.login)
        buttonLogOut = findViewById<TextView>(R.id.logOut)
    }

    override fun onSuccessGoogle(account: GoogleSignInAccount?) {
        initGoogleApi(account)
    }

    private fun initGoogleApi(account: GoogleSignInAccount?) {
        if (account == null) {
            return
        }
        pref?.saveUserName(account.givenName)
        buttonLogOut?.visibility = View.VISIBLE
        buttonLogin?.visibility = View.GONE
        Toast.makeText(this, pref?.getUserName(), Toast.LENGTH_SHORT).show()

    }

    override fun onErrorGoogle() {
        Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
    }


    fun hasNetwork(): Boolean {
        return checkIfHasNetwork()
    }

    fun checkIfHasNetwork(): Boolean {
        val cm = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = cm.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        googleHelper.onResult(requestCode, resultCode, data)
    }

    fun onClickLogin(view: View) {
        if (hasNetwork()) {
            googleHelper.signIn()
        } else
            Toast.makeText(this, "Please check internet connection", Toast.LENGTH_SHORT).show()
    }

    fun onClickLogOut(view: View) {
        if (hasNetwork()) {
            googleHelper.signOut()
            pref?.clearAll()
            Handler(Looper.getMainLooper()).postDelayed(Runnable {
                Toast.makeText(this, "Logout Successfully", Toast.LENGTH_SHORT).show()
                buttonLogOut?.visibility = View.GONE
                buttonLogin?.visibility = View.VISIBLE
            }, 1500)

        } else
            Toast.makeText(this, "Please check internet connection", Toast.LENGTH_SHORT).show()
    }

}
