package com.megaappgnos.urmobo

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Handler
import android.os.Looper
import expo.modules.kotlin.Promise
import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition
import java.net.URL
import androidx.core.content.ContextCompat

class UrmoboModule : Module() {

    private var deviceInfoPromise: Promise? = null

    // BroadcastReceiver para receber a resposta do Urmobo
    private val deviceInfoReceiver =
            object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    try {
                        val imei = intent.getStringExtra("imei") ?: ""
                        val serialNumber = intent.getStringExtra("serial_number") ?: ""
                        val deviceId = intent.getStringExtra("device_id") ?: ""

                        val resultMap =
                                mapOf(
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

        Constants("PI" to Math.PI)

        // Especificar explicitamente todos os eventos
        Events("onChange", "onDeviceInfo")

        Function("hello") { "Hello world! 👋" }

        AsyncFunction("setValueAsync") { value: String ->
            sendEvent("onChange", mapOf("value" to value))
        }

        AsyncFunction("getDeviceInfo") { promise: Promise ->
            appContext.reactContext?.let { context ->
                try {
                    // Verificação de versão do SDK
                    if (Build.VERSION.SDK_INT >= 34 && context.applicationInfo.targetSdkVersion >= 34) {
                        context.registerReceiver(
                            deviceInfoReceiver, 
                            IntentFilter("com.cliente.RESPONSE_INFO"), 
                            ContextCompat.RECEIVER_EXPORTED
                        )
                    } else {
                        context.registerReceiver(
                            deviceInfoReceiver, 
                            IntentFilter("com.cliente.RESPONSE_INFO")
                        )
                    }
                    
                    // Salvar a promise
                    deviceInfoPromise = promise
                    
                    // Enviar o Intent
                    val intent = Intent("com.urmobo.mdm.GET_DEVICE_DATA")
                    context.sendBroadcast(intent)
                    
                    // Configurar timeout
                    Handler(Looper.getMainLooper()).postDelayed({
                        deviceInfoPromise?.let { pendingPromise ->
                            pendingPromise.reject("TIMEOUT", "Não foi recebida resposta do Urmobo", null)
                            deviceInfoPromise = null
                        }
                    }, 10000)
                    
                } catch (e: Exception) {
                    promise.reject("ERROR", e.message, e)
                }
            } ?: run {
                promise.reject("ERROR", "React context is null", null)
            }
        }

        OnDestroy {
            try {
                appContext.reactContext?.unregisterReceiver(deviceInfoReceiver)
            } catch (e: Exception) {
                // Ignorar se o receiver já foi desregistrado
            }
        }

        View(UrmoboModuleView::class) {
            Prop("url") { view: UrmoboModuleView, url: URL -> view.webView.loadUrl(url.toString()) }
            Events("onLoad")
        }
    }
}
