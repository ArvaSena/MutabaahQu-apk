package com.mutabaahqu.app

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val webView = WebView(this)
        setContentView(webView)

        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            cacheMode = WebSettings.LOAD_DEFAULT
        }

        webView.webViewClient = WebViewClient()
        webView.addJavascriptInterface(MutabaahBridge(this), "AndroidBridge")
        webView.loadUrl("https://mutabaahqu-tracker.netlify.app")
    }
}

class MutabaahBridge(private val context: Context) {
    @JavascriptInterface
    fun updateThreeStreaks(jsonPayload: String) {
        val obj = JSONObject(jsonPayload)
        val prefs = context.getSharedPreferences("MutabaahWidgetPrefs", Context.MODE_PRIVATE)
        prefs.edit()
            .putInt("sholat_streak", obj.optInt("sholatStreak", 0))
            .putInt("ngaji_streak", obj.optInt("ngajiStreak", 0))
            .putInt("target_streak", obj.optInt("targetStreak", 0))
            .putInt("done_sholat", obj.optInt("doneSholatToday", 0))
            .putInt("ayat_today", obj.optInt("ayatToday", 0))
            .putInt("done_targets", obj.optInt("doneTargetsToday", 0))
            .putInt("total_targets", obj.optInt("totalTargetsToday", 0))
            .apply()

        val intent = Intent(context, MutabaahStreakWidget::class.java).apply {
            action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
            val ids = AppWidgetManager.getInstance(context)
                .getAppWidgetIds(ComponentName(context, MutabaahStreakWidget::class.java))
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
        }
        context.sendBroadcast(intent)
    }
}
