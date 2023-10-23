@file:OptIn(ExperimentalMaterial3Api::class)

package com.kiryantsev.multiwidget.app.inappwidget

import android.annotation.SuppressLint
import android.os.Build
import android.view.Window
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices.TABLET
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.kiryantsev.multiwidget.R
import com.kiryantsev.multiwidget.app.Router
import com.kiryantsev.multiwidget.app.inappwidget.widgets.ClockWidget
import com.kiryantsev.multiwidget.app.inappwidget.widgets.NowWeatherWidget
import com.kiryantsev.multiwidget.app.inappwidget.widgets.OnLifecycleEvent


@Composable
fun InAppWidgetScreen(
    viewModel: InAppWidgetViewModel,
    navController: NavController,
    window: Window
) {
    val state = viewModel.state.collectAsState().value
    InAppWidgetScreenImpl(
        state = state,
        window = window,
        onInitViewModel = { viewModel.init(it) },
        onDestroyEvent = viewModel::onDestroy,
        onOpenSettings = {
            navController.navigate(Router.SETTINGS_ROUTE)
        }
    )
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
private fun InAppWidgetScreenImpl(
    state: InAppWidgetState,
    window: Window?,
    onInitViewModel: (LifecycleOwner) -> Unit,
    onDestroyEvent: () -> Unit,
    onOpenSettings: () -> Unit,
) {
    Scaffold(Modifier.safeContentPadding()) {

        val lifecycleOwner = LocalLifecycleOwner.current
        LaunchedEffect(Unit) {
            if (window != null) {
                hideSystemUI(window = window)
                onInitViewModel.invoke(lifecycleOwner)
            }
        }

        OnLifecycleEvent { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                onDestroyEvent.invoke()
            }
        }


        AsyncImage(
            model = "https://cdnph.upi.com/svc/sv/i/2321501089579/2017/1/15010945395977/Why-does-it-drizzle-instead-of-rain-NASA-offers-answer.jpg",
            contentDescription = "",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )

        ClockWidget(modifier = Modifier.padding(32.dp))




            Box(Modifier.fillMaxSize()) {
                val configuration = LocalConfiguration.current

                Image(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                        .clickable { onOpenSettings.invoke() },
                    painter = painterResource(R.drawable.baseline_app_settings_alt_24),
                    contentDescription = null
                )


                if(state.weather != null){
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                            .background(Color.Gray.copy(alpha = 0.2f))
                            .height(
                                (configuration.screenHeightDp / 3).dp
                            )
                    ) {
                        NowWeatherWidget(
                            state.weather.fact
                        )
                    }
                }
            }



    }
}


fun hideSystemUI(window: Window) {
    WindowCompat.setDecorFitsSystemWindows(window, false)

    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    } else {
        window.insetsController?.apply {
            hide(WindowInsets.Type.statusBars())
            systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }
}

@Composable
@Preview(device = TABLET)
private fun InAppWidgetScreenImplPreview() {
    InAppWidgetScreenImpl(
        state = InAppWidgetState(
            weather = null
        ),
        window = null,
        onInitViewModel = {},
        onDestroyEvent = {},
        onOpenSettings = {}
    )
}