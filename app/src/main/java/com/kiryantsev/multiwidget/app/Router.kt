package com.kiryantsev.multiwidget.app

import android.app.Application
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.kiryantsev.multiwidget.app.inappwidget.InAppWidgetScreen
import com.kiryantsev.multiwidget.app.inappwidget.InAppWidgetViewModel
import com.kiryantsev.multiwidget.app.settingsscreen.SettingsScreenImpl
import com.kiryantsev.multiwidget.app.settingsscreen.SettingsScreenViewModel
import com.kiryantsev.multiwidget.core.settings.SettingsEntity

class Router {
    companion object {
        const val APP_ROUTE = "app"
        const val IN_APP_WIDGET_ROUTE ="inAppWidget"
        const val SETTINGS_ROUTE = "settings"



        fun buildNavGraph(builder: NavGraphBuilder, application: Application, navController: NavController) = builder.apply {
            navigation(
                route = APP_ROUTE,
                startDestination = chooseRootRoute()
            ) {
                composable(SETTINGS_ROUTE){
                    SettingsScreenImpl(viewModel = SettingsScreenViewModel(application), navController = navController)
                }

                composable(IN_APP_WIDGET_ROUTE){
                    InAppWidgetScreen(viewModel = InAppWidgetViewModel(),navController = navController)
                }
            }
        }

        private fun chooseRootRoute(): String {
            return if(SettingsEntity.load().isInAppWidgetEnabled){
                IN_APP_WIDGET_ROUTE
            }else {
                SETTINGS_ROUTE
            }
        }

    }
}

