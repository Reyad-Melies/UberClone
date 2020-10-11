package com.example.taxi

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.firebase.ui.auth.AuthMethodPickerLayout
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.*
import java.util.concurrent.TimeUnit

class SplashScreenActivity : AppCompatActivity() {
    companion object{
        private  val LoginRequestCode=7171
    }
    private lateinit var providers:List<AuthUI.IdpConfig>
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var listener: FirebaseAuth.AuthStateListener
    override fun onStart() {
        super.onStart()
        delaySplashScreen();
    }

    private fun delaySplashScreen() {
        Completable.timer(3, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
            .subscribe() {
                firebaseAuth.addAuthStateListener ( listener )
            }
    }

    override fun onStop() {
       if (firebaseAuth!=null&&listener!=null)firebaseAuth.removeAuthStateListener(listener)
        super.onStop()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
init()
        }

    private fun init() {
        providers=Arrays.asList(
          AuthUI.IdpConfig.PhoneBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )
        firebaseAuth=FirebaseAuth.getInstance()
        listener= FirebaseAuth.AuthStateListener { myFirebaseAuth->
            val user =myFirebaseAuth.currentUser
            if(user!=null)
                Toast.makeText(this@SplashScreenActivity,"Welcome"+user.uid,Toast.LENGTH_SHORT).show()
            else
                showLoginLayout()
        }
    }

    private fun showLoginLayout() {
        val authMethodPickerLayout=AuthMethodPickerLayout.Builder(R.layout.layout_sign_in).setPhoneButtonId(R.id.phone).setGoogleButtonId(R.id.google).build()
        startActivityForResult(
             AuthUI.getInstance()
                 .createSignInIntentBuilder()
                 .setAuthMethodPickerLayout(authMethodPickerLayout)
                 .setTheme(R.style.LoginTheme)
                 .setAvailableProviders(providers)
                 .setIsSmartLockEnabled(false)
                 .build()
            , LoginRequestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    if (resultCode== LoginRequestCode){
       val response =IdpResponse.fromResultIntent(data)
        if (resultCode==Activity.RESULT_OK){
val user=FirebaseAuth.getInstance().currentUser;
        }
        else
            Toast.makeText(this@SplashScreenActivity,""+response!!.error!!.message,Toast.LENGTH_SHORT).show()
    }
     }
}