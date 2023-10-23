package com.kiryantsev.multiwidget.app.inappwidget

import android.app.Application
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.kiryantsev.multiwidget.core.workers.ForegroundSyncWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@HiltViewModel
class InAppWidgetViewModel @Inject constructor(
   private val application: Application
) : ViewModel() {
    private val _state = MutableStateFlow(InAppWidgetState())
    val state : StateFlow<InAppWidgetState> = _state.asStateFlow()

    private val workManager = WorkManager.getInstance(application)

    fun init(lifecycleOwner: LifecycleOwner) {
        runWorkAndSubscribe(lifecycleOwner)
    }

    fun onDestroy() = workManager.cancelAllWork()

    private fun runWorkAndSubscribe(lifecycleOwner: LifecycleOwner) {
        viewModelScope.launch {


            workManager.cancelAllWork()

            //init service
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
            val work = PeriodicWorkRequestBuilder<ForegroundSyncWorker>(
                15,
                TimeUnit.MINUTES
            ).setConstraints(constraints).build()

            //subscribe to update
            val workLiveData = workManager.getWorkInfoByIdLiveData(work.id)
            workLiveData.observe(lifecycleOwner) {
                val weather = ForegroundSyncWorker.extractWeatherRes(it.outputData)
                if (weather != null) {
                    viewModelScope.launch {
                        _state.emit(_state.value.copy(weather = weather))
                    }
                }
            }

            //start service
            workManager.enqueue(work)
        }
    }

}