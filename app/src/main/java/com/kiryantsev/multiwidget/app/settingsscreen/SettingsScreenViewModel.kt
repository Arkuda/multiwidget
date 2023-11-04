package com.kiryantsev.multiwidget.app.settingsscreen

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.Task
import com.google.gson.Gson
import com.kiryantsev.multiwidget.core.workers.BackgroundSyncWorker
import com.kiryantsev.multiwidget.core.settings.SettingsEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


class SettingsScreenViewModel(
    val application: Application
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsScreenState.getFromSettings())
    val state = _state.asStateFlow()

    @SuppressLint("MissingPermission")
    fun onSaveTapped(
        isLocationGranted: Boolean,
        yandexToken: String,
        fusedLocationProviderClient: FusedLocationProviderClient
    ) {
        SettingsEntity.load().copy(yandexToken = yandexToken).save()

        if (isLocationGranted && state.value.yandexToken.isNotEmpty()) {

            val currLocTask = fusedLocationProviderClient.getCurrentLocation(
                Priority.PRIORITY_BALANCED_POWER_ACCURACY, CancellationTokenSource().token
            )

            viewModelScope.launch {
                val location = currLocTask.await()

                if (location != null) {
                    SettingsEntity.load().copy(
                        userLat = location.latitude,
                        userLng = location.longitude
                    ).save()
                }

                val workManager = WorkManager.getInstance(application)

                workManager.cancelAllWork()

                //start service
                val constraints = Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
                val work = PeriodicWorkRequestBuilder<BackgroundSyncWorker>(
                    15,
                    TimeUnit.MINUTES
                ).setConstraints(constraints).build()

                workManager.enqueue(work)
            }
        }
    }

    fun onChangedWeatherSwitchState(isEnabled: Boolean) {
        viewModelScope.launch {
            _state.emit(
                state.value.copy(isYaWeatherEnabled = isEnabled)
            )
        }
    }

    fun onChangedCalendarSwitchState(isEnabled: Boolean, haveCalendarPermission: Boolean) {
        viewModelScope.launch {
            _state.emit(
                state.value.copy(
                    isCalendarEnabled = isEnabled,
                    isHaveCalendarPermission = haveCalendarPermission
                )
            )
        }
    }

    fun onChangedChangeInAppWidgetSwitchState(isEnabled: Boolean) {
        viewModelScope.launch {
            _state.emit(
                state.value.copy(
                    isInAppWidgetEnabled = isEnabled
                )
            )
        }
    }

    fun onChangedYandexToken(token: String) {
        viewModelScope.launch {
            _state.emit(
                state.value.copy(yandexToken = token)
            )
        }
    }

    fun onChangedOpenWeatherToken(token: String) {
        viewModelScope.launch {
            _state.emit(
                state.value.copy(openWeatherToken = token)
            )
        }
    }

    fun onChangedUserPos(lat: String, lng: String) {
        viewModelScope.launch {
            _state.emit(
                state.value.copy(
                    userLat = lat,
                    userLng = lng
                )
            )
        }
    }

    @SuppressLint("MissingPermission")
    fun onTryAutomaticGetPosition(fusedLocationProviderClient: FusedLocationProviderClient) {
        val currLocTask = fusedLocationProviderClient.getCurrentLocation(
            Priority.PRIORITY_BALANCED_POWER_ACCURACY, CancellationTokenSource().token
        )

        viewModelScope.launch {
            _state.emit(state.value.copy(locationFetchingInProgress = true))

            val location = currLocTask.await()
            state.value.apply {
                _state.emit(
                    copy(
                        locationFetchingInProgress = false,
                        userLat = location?.latitude.toString() ?: userLat,
                        userLng = location?.longitude.toString() ?: userLng
                    )
                )
            }
        }
    }

    fun onSaveClick() {
        state.value.toSettings().save() //save settings

        if(state.value.run { isYaWeatherEnabled || isCalendarEnabled || isOtpEnabled }){
            viewModelScope.launch {
                val workManager = WorkManager.getInstance(application)

                workManager.cancelAllWork()

                //start service
                val constraints = Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
                val work = PeriodicWorkRequestBuilder<BackgroundSyncWorker>(
                    15,
                    TimeUnit.MINUTES
                ).setConstraints(constraints).build()

//                workManager.enqueue(work)
            }
        }
    }

    private suspend fun <T> Task<T>.await(): T? = suspendCoroutine { continuation ->
        addOnCompleteListener { task ->
            if (task.isSuccessful) {
                continuation.resume(task.result)
            } else {
                continuation.resumeWithException(
                    task.exception ?: RuntimeException("Unknown task exception")
                )
            }
        }
    }
}