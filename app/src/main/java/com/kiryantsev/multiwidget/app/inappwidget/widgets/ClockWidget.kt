@file:OptIn(
    ExperimentalAnimationApi::class, ExperimentalAnimationApi::class,
    ExperimentalAnimationApi::class, ExperimentalAnimationApi::class
)

package com.kiryantsev.multiwidget.app.inappwidget.widgets

import android.text.format.Time
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.animation.with
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import kotlinx.datetime.Clock
import java.util.Locale
import java.util.Timer
import java.util.TimerTask


@Composable
fun ClockWidget(
    modifier: Modifier = Modifier,
    timeTextSize: TextUnit = 120.sp,
    dateTextSize: TextUnit = 78.sp,
    stokerWidth: Float = 30f
) {
    val time = remember {
        mutableStateOf(Time())
    }
    val timer = remember { Timer() }

    LaunchedEffect(Unit) {
        timer.schedule(
            object : TimerTask() {
                override fun run() {
                    time.value = Time().apply { setToNow() }
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
        "HH:mm",
//        "HH:mm\ndd MMMM\nEEEE",
        time.value.toMillis(false)
    ).toString()

    val date = android.text.format.DateFormat.format(
        "dd MMMM\nEEEE",
        time.value.toMillis(false)
    ).toString()
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }


    Column(modifier) {
        Box {
            AnimatedContent(
                targetState = timeStr,
                transitionSpec = {
                    fadeIn(animationSpec = tween(durationMillis = 500)) togetherWith
                            fadeOut(animationSpec = tween(durationMillis = 500))
                },
                contentAlignment = Alignment.Center, label = ""
            ) { targetTime ->
                Text(
                    text = targetTime,
                    style = TextStyle.Default.copy(
                        color = Color.White,
                        fontSize = timeTextSize,
                        drawStyle = Stroke(
                            miter = 0f,
                            width = stokerWidth,
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
                    text = targetTime,
                    style = TextStyle.Default.copy(
                        fontSize = timeTextSize,
                        color = Color.Black
                    )
                )
            }
        }
        Box {
            AnimatedContent(
                targetState = date,
                transitionSpec = {
                    fadeIn(animationSpec = tween(durationMillis = 500)) togetherWith
                            fadeOut(animationSpec = tween(durationMillis = 500))
                },
                contentAlignment = Alignment.Center, label = ""
            ) { targetTime ->
                Text(
                    text = targetTime,
                    style = TextStyle.Default.copy(
                        fontSize = dateTextSize,
                        color = Color.White,
                        drawStyle = Stroke(
                            miter = 0f,
                            width = stokerWidth,
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
                    text = date,
                    style = TextStyle.Default.copy(
                        fontSize = dateTextSize,
                        color = Color.Black
                    )
                )
            }
        }
    }


}