@file:OptIn(ExperimentalAnimationApi::class)

package com.kiryantsev.multiwidget.app.inappwidget.widgets

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import kotlinx.datetime.Clock
import java.util.Locale
import java.util.Timer
import java.util.TimerTask


@Composable
fun ClockWidget(modifier: Modifier = Modifier) {
    val time = remember {
        mutableStateOf(Clock.System.now())
    }
    val timer = remember { Timer() }

    LaunchedEffect(Unit) {
        timer.schedule(
            object : TimerTask() {
                override fun run() {
                    time.value = Clock.System.now()
                }
            },
            0, 500,
        )
    }

    OnLifecycleEvent { _, event ->
        if (event == Lifecycle.Event.ON_DESTROY) {
            timer.cancel()
        }
    }

    val timeStr = android.text.format.DateFormat.format(
        "HH:mm\ndd MMMM\nEEEE",
        time.value.toEpochMilliseconds()
    ).toString()
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

    val textSize = 78.sp

    AnimatedContent(
        targetState = timeStr,
        transitionSpec = {
            fadeIn(animationSpec = tween(durationMillis = 500)) with
                    fadeOut(animationSpec = tween(durationMillis = 500))
        },
        contentAlignment = Alignment.Center, label = ""
    ) { targetTime ->
        Text(
            modifier = modifier,
            text = targetTime,
            style = TextStyle.Default.copy(
                fontSize = textSize,
                drawStyle = Stroke(
                    miter = 10f,
                    width = 5f,
                    join = StrokeJoin.Round,
                    cap = StrokeCap.Butt
                ),
                shadow = Shadow(
                    color = Color.Black,
                    offset = Offset(4f, 4f),
                    blurRadius = 8f
                )
            )
        )

        Text(
            modifier = modifier,
            text = targetTime,
            style = TextStyle.Default.copy(
                fontSize = textSize,
                color = Color.White
            )
        )
    }


}