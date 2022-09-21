package com.example.firebaseapplication

import android.accounts.Account
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener

class GoogleHelper(
    private val context: FragmentActivity?,
    private var callback: GoogleHelperCallback?,
) : OnConnectionFailedListener {

    interface GoogleHelperCallback {
        fun onSuccessGoogle(account: GoogleSignInAccount?)
        fun onErrorGoogle()
    }

    val TAG = GoogleHelper::class.java.simpleName

    private val RC_SIGN_IN = 9001
    private var mGoogleApiClient: GoogleApiClient? = null
    private val mAccount: Account? = null

    init {
        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN) //                .requestIdToken(context.getResources().getString(R.string.server_client_id))
                .requestEmail().build()
        mGoogleApiClient = GoogleApiClient.Builder(context!!)
            .enableAutoManage(context, this)
            .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
            .build()
    }

    fun signIn() {
        if (mGoogleApiClient != null){
            val signInIntent: Intent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient!!)
            context!!.startActivityForResult(signInIntent, RC_SIGN_IN)
        }

    }

    fun signOut() {
        try {
            if (mGoogleApiClient!!.isConnected) {
                Auth.GoogleSignInApi.signOut(mGoogleApiClient!!)
                return
            }
        } catch (e: Exception) {
            Toast.makeText(context, "error", Toast.LENGTH_SHORT).show()

        }
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {}
    fun onResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_SIGN_IN) {
            val result: GoogleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data!!)!!
            handleSignInResult(result)
        }
    }

    private fun handleSignInResult(result: GoogleSignInResult) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess)
        Log.d(TAG, "handleSignInResult:" + result.status + result)
        if (result.isSuccess) {
            // Signed in successfully, show authenticated UI.
            val account: GoogleSignInAccount = result.signInAccount!!
            // text for dob
            callback!!.onSuccessGoogle(account)
        } else {
            callback!!.onErrorGoogle()
        }
    }
}