package com.example.pother_delivery

import android.content.Context

class FirebaseToken(context: Context) {
    val sharedPreference=context.getSharedPreferences("FIREBASE_TOKEN", Context.MODE_PRIVATE)

    fun getToken(): String{
        val token = sharedPreference.getString("token","")
        return token!!
    }

    fun setToken(token:String?){
        val editor=sharedPreference.edit()
        editor.putString("token",token)
        editor.apply()
    }

    fun setUserId(id:String){
        val editor=sharedPreference.edit()
        editor.putString("user_id",id.toString())
        editor.apply()
    }

    fun getUserId(): Int{
        val userId = sharedPreference.getString("user_id","0")
        return userId?.toInt()!!
    }

}