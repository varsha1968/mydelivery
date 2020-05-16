package com.example.pother_delivery

import android.util.Log
import androidx.constraintlayout.widget.Constraints.TAG


/**
 * Called if InstanceID token is updated. This may occur if the security of
 * the previous token had been compromised. Note that this is called when the InstanceID token
 * is initially generated so this is where you would retrieve the token.
 */

 fun onNewToken(token: String) {
    Log.v(TAG, "nobita")

    // If you want to send messages to this application instance or
    // manage this apps subscriptions on the server side, send the
    // Instance ID token to your app server.
    sendRegistrationToServer(token)
}

fun sendRegistrationToServer(token: String) {

}
// If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.


