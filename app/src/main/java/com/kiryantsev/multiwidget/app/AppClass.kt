package com.kiryantsev.multiwidget.app

import android.app.Application
import androidx.work.Configuration
import com.kiryantsev.multiwidget.core.di.BackgroundSyncWorkerFactory
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class AppClass : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: BackgroundSyncWorkerFactory

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.INFO)
            .setWorkerFactory(workerFactory)
            .build()
    }
}
