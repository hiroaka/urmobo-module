package com.megaappgnos.urmobo

import android.content.BroadcastReceiver
import android.content.Context
import androidx.core.content.ContextCompat
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import expo.modules.kotlin.Promise
import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import	android.os.Build

class UrmoboModule : Module() {
    private val TAG = "UrmoboModule"
    private val context: Context
        get() = appContext.reactContext ?: throw Exception("React Application Context is null")

    override fun definition() = ModuleDefinition {
        Name("UrmoboModule")

        Constants(
            "PI" to Math.PI
        )

        Function("hello") {
            return@Function "Hello from Urmobo Module!"
        }

        Function("sendIntent") {
            try {
                val intent = Intent("com.urmobo.mdm.GET_DEVICE_DATA")
                context.sendBroadcast(intent)
                Log.d(TAG, "Intent sent: com.urmobo.mdm.GET_DEVICE_DATA")
                return@Function "Intent sent successfully"
            } catch (e: Exception) {
                Log.e(TAG, "Error sending intent: ${e.message}")
                return@Function "Error sending intent: ${e.message}"
            }
        }

        AsyncFunction("getDeviceInfo") { promise: Promise ->
            GlobalScope.launch(Dispatchers.Main) {
                try {
                    val result = getDeviceInfoInternal()
                    promise.resolve(result)
                } catch (e: Exception) {
                    Log.e(TAG, "Error in getDeviceInfo: ${e.message}")
                    promise.reject("ERROR", e.message ?: "Unknown error", e)
                }
            }
        }
    }

    private suspend fun getDeviceInfoInternal(): Map<String, String> {
        return withContext(Dispatchers.IO) {
            val latch = CountDownLatch(1)
            var result: Map<String, String> = emptyMap()
            var error: Exception? = null
            
            val receiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    try {
                        Log.d(TAG, "Received response from Urmobo MDM")
                        val imei = intent.getStringExtra("IMEI") ?: ""
                        val serialNumber = intent.getStringExtra("SerialNumber") ?: ""
                        val deviceId = intent.getStringExtra("UrmoboID") ?: ""

                        Log.d(TAG, "IMEI: $imei, SerialNumber: $serialNumber, DeviceID: $deviceId")

                        result = mapOf(
                            "imei" to imei,
                            "serialNumber" to serialNumber,
                            "deviceId" to deviceId
                        )
                        latch.countDown()
                    } catch (e: Exception) {
                        Log.e(TAG, "Error processing response: ${e.message}")
                        error = e
                        latch.countDown()
                    }
                }
            }

            withContext(Dispatchers.Main) {

                //checking version of the sdk and registering receiver with RECEIVER_EXPORTED
                if (Build.VERSION.SDK_INT >= 34 && context.getApplicationInfo().targetSdkVersion >= 34) {
                    context.registerReceiver(receiver, IntentFilter("com.cliente.RESPONSE_INFO"), ContextCompat.RECEIVER_EXPORTED)
                }else{
                    context.registerReceiver(receiver, IntentFilter("com.cliente.RESPONSE_INFO"))
                }

                Log.d(TAG, "Registered BroadcastReceiver for com.cliente.RESPONSE_INFO")
                
                // Send the intent to request device info
                val intent = Intent("com.urmobo.mdm.GET_DEVICE_DATA")
                context.sendBroadcast(intent)
                Log.d(TAG, "Sent intent: com.urmobo.mdm.GET_DEVICE_DATA")
            }
            
            // Wait for response with timeout
            val received = latch.await(10, TimeUnit.SECONDS)
            
            withContext(Dispatchers.Main) {
                try {
                    context.unregisterReceiver(receiver)
                    Log.d(TAG, "Unregistered BroadcastReceiver")
                } catch (e: Exception) {
                    Log.e(TAG, "Error unregistering receiver: ${e.message}")
                }
            }
            
            if (!received) {
                throw Exception("Timeout waiting for device info")
            }
            
            error?.let { throw it }
            
            return@withContext result
        }
    }
}