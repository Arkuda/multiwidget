package com.kiryantsev.multiwidget.app

import android.app.Application
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.kiryantsev.multiwidget.app.Router.Companion.APP_ROUTE
import com.kiryantsev.multiwidget.app.inappwidget.InAppWidget
import com.kiryantsev.multiwidget.app.inappwidget.InAppWidgetViewModel
import com.kiryantsev.multiwidget.app.settingsscreen.SettingsScreenImpl
import com.kiryantsev.multiwidget.app.settingsscreen.SettingsScreenViewModel
import com.kiryantsev.multiwidget.core.settings.SettingsEntity

class Router {
    companion object {
        const val APP_ROUTE = "app"
        private const val IN_APP_WIDGET_ROUTE ="inAppWidget"
        private const val SETTINGS_ROUTE = "settings"

        val IN_APP_WIDGET_SCREEN = "$APP_ROUTE/$IN_APP_WIDGET_ROUTE"
        val SETTINGS_SCREEN = "$APP_ROUTE/$SETTINGS_ROUTE"



        fun buildNavGraph(builder: NavGraphBuilder, application: Application) {
            builder.apply {
                navigation(
                    route = APP_ROUTE,
                    startDestination = chooseRootRoute()
                ) {
                    composable(
                        SETTINGS_ROUTE,
                    ){
                        SettingsScreenImpl(viewModel = SettingsScreenViewModel(application))
                    }

                    composable(IN_APP_WIDGET_ROUTE){
                        InAppWidget(viewModel = InAppWidgetViewModel())
                    }
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

