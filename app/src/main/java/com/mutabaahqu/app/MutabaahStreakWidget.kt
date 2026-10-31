package com.mutabaahqu.app

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews

class MutabaahStreakWidget : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        val prefs = context.getSharedPreferences("MutabaahWidgetPrefs", Context.MODE_PRIVATE)
        val sholatStreak = prefs.getInt("sholat_streak", 0)
        val ngajiStreak = prefs.getInt("ngaji_streak", 0)
        val targetStreak = prefs.getInt("target_streak", 0)
        val doneSholat = prefs.getInt("done_sholat", 0)
        val ayatToday = prefs.getInt("ayat_today", 0)

        for (appWidgetId in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.widget_mutabaah_streak)
            views.setTextViewText(R.id.tv_streak_sholat, "🔥 $sholatStreak hr")
            views.setTextViewText(R.id.tv_streak_ngaji, "📖 $ngajiStreak hr")
            views.setTextViewText(R.id.tv_streak_target, "🎯 $targetStreak hr")
            views.setTextViewText(R.id.tv_sub_info, "Hari ini: $doneSholat/5 Sholat • $ayatToday Ayat")

            val intent = Intent(context, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(
                context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(R.id.widget_root, pendingIntent)
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}