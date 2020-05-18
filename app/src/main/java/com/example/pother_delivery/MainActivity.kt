package com.example.pother_delivery
import android.graphics.Bitmap

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.AsyncTask


import android.util.Log
import android.webkit.WebChromeClient
import android.widget.Toast

import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat.isLocationEnabled
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging

import java.lang.Exception




class MainActivity : AppCompatActivity() {

    lateinit var serviceIntent:Intent
    lateinit var deliveryBoy: HashMap<String,Any>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val WebView:WebView=findViewById(R.id.WebView)
        val that=this
        if (WebView!=null){
            val webSettings=WebView!!.settings
            webSettings.javaScriptEnabled=true
            WebView!!.webChromeClient= WebChromeClient()
            //WebView!!.loadUrl("http://192.168.43.241/delivery-master/deliveryboy")
            WebView!!.loadUrl("http://192.168.1.4/deliveryboy/deliveryboy/login.php?token="+FirebaseToken(this).getToken())
            WebView!!.webViewClient=object:WebViewClient(){
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                }

            }
        }
        var ref = FirebaseDatabase.getInstance().reference.child("delivery_boys/"+FirebaseToken(this).getToken())
        val menuListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.value!=null){
                    deliveryBoy=dataSnapshot.value as HashMap<String,Any>
                    FirebaseToken(that).setUserId(deliveryBoy["id"] as String)
                    Log.v("vazi",deliveryBoy.toString())
                    Toast.makeText(that,FirebaseToken(that).getUserId().toString(),Toast.LENGTH_LONG).show()
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.v("vazi","Error")
            }
        }
        ref.addValueEventListener(menuListener)
        if(isLocationEnabled()) {
            try {
                ServiceUtil(this).execute()
                Log.v("shinchan","error")

            } catch (e:Exception){
                Log.v("debug",e.toString())
            }
        } else {
            setLocationPermission()
        }

    }

    override fun onDestroy() {
        stopService(serviceIntent)
        super.onDestroy()
    }

    fun setLocationPermission(){
        val permission= ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)
        if(permission!= PackageManager.PERMISSION_GRANTED){
            Log.v("Coding","Permission Denied")
        }
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.ACCESS_FINE_LOCATION)){
            val builder=androidx.appcompat.app.AlertDialog.Builder(this)
            builder.setMessage("Location permission is required")
            builder.setTitle("Permission Required!")
            builder.setPositiveButton("Ok")
            {
                    dialog,which->
                Log.v("Coding","Clicked")
                makeRequest()
            }
            val dialog=builder.create()
            dialog.show()
        } else{
            makeRequest()
        }
    }

    fun setBackgroundLocationPermission(){
        val permission= ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        if(permission!= PackageManager.PERMISSION_GRANTED){
            Log.v("Coding","Permission Denied")
        }
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.ACCESS_BACKGROUND_LOCATION)){
            val builder=androidx.appcompat.app.AlertDialog.Builder(this)
            builder.setMessage("Background Location permission is required")
            builder.setTitle("Permission Required!")
            builder.setPositiveButton("Ok")
            {
                    dialog,which->
                Log.v("Coding","Clicked")
                makeRequestBackground()
            }
            val dialog=builder.create()
            dialog.show()
        } else{
            makeRequestBackground()
        }
    }

    fun makeRequest(){
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),1)
    }
    fun makeRequestBackground(){
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_BACKGROUND_LOCATION),2)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            1->{
                if(grantResults.isEmpty() || grantResults[0]!= PackageManager.PERMISSION_GRANTED){
                    Log.v("Coding","Permission has been denied by user")
                } else{
                    Log.v("Coding","Permission has been granted by user")

                }
            }
            2->{
                if(grantResults.isEmpty() || grantResults[0]!= PackageManager.PERMISSION_GRANTED){
                    Log.v("Coding","Permission has been denied by user")
                } else{
                    Log.v("Coding","Permission has been granted by user")
                    ServiceUtil(this).execute()
                }
            }
        }
    }

    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }
}

class ServiceUtil(val context: Context):AsyncTask<String,String,String>(){
    override fun doInBackground(vararg p0: String?): String {
        (context as MainActivity).serviceIntent=Intent(context,LocationTracker::class.java)
        context.startService(context.serviceIntent)
        return "Done"
    }

}
