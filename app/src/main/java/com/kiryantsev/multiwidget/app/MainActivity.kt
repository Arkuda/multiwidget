package com.kiryantsev.multiwidget.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.kiryantsev.multiwidget.app.Router.Companion.APP_ROUTE
import com.kiryantsev.multiwidget.core.theme.MultiWidgetTheme
import com.pixplicity.easyprefs.library.Prefs
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setup prefs
        Prefs.Builder()
            .setContext(this)
            .setMode(MODE_PRIVATE)
            .setPrefsName(packageName)
            .setUseDefaultSharedPreference(true)
            .build()

        val application = application
        setContent {
            MultiWidgetTheme {
                NavHost(navController = rememberNavController(), startDestination = APP_ROUTE){
                    Router.buildNavGraph(this,application)
                }
            }
        }
    }
}

