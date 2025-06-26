package com.megaappgnos.urmobo

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Handler
import android.os.Looper
import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition
import androidx.core.content.ContextCompat

import expo.modules.kotlin.Promise
import java.net.URL
import	android.os.Build

class UrmoboModule : Module() {

    private var deviceInfoPromise: Promise? = null

    // BroadcastReceiver para receber a resposta do Urmobo
    private val deviceInfoReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            try {
                val imei = intent.getStringExtra("imei") ?: ""
                val serialNumber = intent.getStringExtra("serial_number") ?: ""
                val deviceId = intent.getStringExtra("device_id") ?: ""
                
                val resultMap = mapOf(
                    "imei" to imei,
                    "serialNumber" to serialNumber,
                    "deviceId" to deviceId
                )
                
                deviceInfoPromise?.resolve(resultMap)
                deviceInfoPromise = null
            } catch (e: Exception) {
                deviceInfoPromise?.reject("ERROR", e.message, e)
                deviceInfoPromise = null
            }
        }
    }
    
    override fun definition() = ModuleDefinition {
        Name("UrmoboModule")

        Constants(
            "PI" to Math.PI
        )

        // Especificar explicitamente todos os eventos
        Events("onChange", "onDeviceInfo")

        Function("hello") {
            "Hello world! 👋"
        }

        AsyncFunction("setValueAsync") { value: String ->
            sendEvent("onChange", mapOf(
                "value" to value
            ))
        }

        AsyncFunction("getDeviceInfo") { promise: Promise ->
            
            appContext.reactContext?.let { context ->
            try {

                


                //checking version of the sdk and registering receiver with RECEIVER_EXPORTED
                if (Build.VERSION.SDK_INT >= 34 && context.getApplicationInfo().targetSdkVersion >= 34) {
                    context.registerReceiver(deviceInfoReceiver, IntentFilter("com.cliente.RESPONSE_INFO"), ContextCompat.RECEIVER_EXPORTED)
                }else{
                    context.registerReceiver(deviceInfoReceiver, IntentFilter("com.cliente.RESPONSE_INFO"))
                }
        
                // // Registrar o BroadcastReceiver se ainda não estiver registrado
                // .registerReceiver(
                //     deviceInfoReceiver,
                //     IntentFilter("com.cliente.RESPONSE_INFO")
                // )
                
                // Salvar a promise para resolver quando a resposta chegar
                deviceInfoPromise = promise
                
                // Enviar o Intent para o Urmobo
                val intent = Intent("com.urmobo.mdm.GET_DEVICE_DATA")
                appContext.reactContext?.sendBroadcast(intent)
                
                // Usar Handler com Looper.getMainLooper() para o timeout
                Handler(Looper.getMainLooper()).postDelayed({
                    if (deviceInfoPromise != null) {
                        deviceInfoPromise?.reject("TIMEOUT", "Não foi recebida resposta do Urmobo", null)
                        deviceInfoPromise = null
                    }
                }, 10000) // 10 segundos de timeout
                
            } catch (e: Exception) {
                promise.reject("ERROR", e.message, e)
            }
            } ?: promise.reject("ERROR", "React context is null")
        }

        OnDestroy {
            try {
                appContext.reactContext?.unregisterReceiver(deviceInfoReceiver)
            } catch (e: Exception) {
                // Ignorar se o receiver já foi desregistrado
            }
        }

        View(UrmoboModuleView::class) {
            Prop("url") { view: UrmoboModuleView, url: URL ->
                view.webView.loadUrl(url.toString())
            }
            Events("onLoad")
        }
    }
}