package com.kiryantsev.multiwidget.widget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.ExperimentalGlanceApi
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver

@OptIn(ExperimentalGlanceApi::class)
class MultiWidgetReceiver: GlanceAppWidgetReceiver() {

    companion object {
         val PREF_KEY = stringPreferencesKey("all_notes")
    }


    private val widget = MultiWidget()
    override val glanceAppWidget: GlanceAppWidget = widget

    private val updater = MultiWidgetUpdater(glanceAppWidget)


    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        updater.updateWidget(context)

    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        updater.updateWidget(context)
    }



}