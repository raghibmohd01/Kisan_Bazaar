package com.mtr.kisanbazaar

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mtr.kisanbazaar.daos.UserDao
import com.mtr.kisanbazaar.models.User


class LoginActivity : AppCompatActivity() {


    lateinit var txtSignUp:TextView
    lateinit var tvGoogleLogin:TextView

    lateinit var signInButton: com.google.android.gms.common.SignInButton
    lateinit var progressBar: ProgressBar
    lateinit var  googleSignInClient: GoogleSignInClient
    val RC_SIGN_IN=123
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        txtSignUp=findViewById(R.id.txt_signUp)
        tvGoogleLogin=findViewById(R.id.txt_google)

       progressBar=findViewById(R.id.progressBar)


        val gso= GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        auth= Firebase.auth

        googleSignInClient = GoogleSignIn.getClient(this,gso)




        tvGoogleLogin.setOnClickListener {
            signIn()
        }





        txtSignUp.setOnClickListener {
            val i= Intent(this,RegisterActivity::class.java)
            startActivity(i)
        }




    }


    override fun onStart() {
        super.onStart()
        var currentUser=auth.currentUser       //stores currently logged in user
        updateUI(currentUser)                  //update ui if user logged in or ask to login if null
    }

    private fun signIn() {                                //signed in user using google account


        //signInButton.visibility = View.GONE

        val signInIntent = googleSignInClient.signInIntent              //shows list of users in device
        progressBar.visibility = View.VISIBLE
        startActivityForResult(signInIntent, RC_SIGN_IN)

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(ContentValues.TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(ContentValues.TAG, "Google sign in failed", e)
            }
        }
    }


    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(ContentValues.TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(ContentValues.TAG, "signInWithCredential:failure", task.exception)
                    updateUI(null)
                    Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
                }
            }
    }


    private fun updateUI(firebaseUser: FirebaseUser?) {

        if(firebaseUser != null) {
            val userDao= UserDao()
            val user= User(firebaseUser.uid,firebaseUser.displayName,firebaseUser.photoUrl.toString())
            userDao.addUser(user)
            Toast.makeText(this, "Login Successfull!", Toast.LENGTH_SHORT).show()
            progressBar.visibility = View.GONE

            val intent=Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
        else{
            signIn()
        }
    }
}