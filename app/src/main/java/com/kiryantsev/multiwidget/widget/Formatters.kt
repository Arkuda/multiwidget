package com.kiryantsev.multiwidget.widget

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
val hhMMFormatter = DateTimeFormatter.ofPattern("HH:mm")

@RequiresApi(Build.VERSION_CODES.O)
val ddFormatter = DateTimeFormatter.ofPattern("dd.MM")