package com.example.pother_delivery

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        val context=this
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                val TAG="vazi"
                if (!task.isSuccessful) {
                    Log.v(TAG,"Error")
                    return@OnCompleteListener
                }
                else{
                    // Get new Instance ID token
                    val token = task.result?.token
                    val msg=token
                    FirebaseToken(context).setToken(token)
                    Log.v(TAG,msg)
                    Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()// Log and toast
                    val intent=Intent(context,MainActivity::class.java)

                    startActivity(intent)
                    context.finish()
                }})
    }
}
