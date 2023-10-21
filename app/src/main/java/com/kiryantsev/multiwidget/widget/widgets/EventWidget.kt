package com.kiryantsev.multiwidget.widget.widgets

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceModifier
import androidx.glance.LocalContext
import androidx.glance.background
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.text.Text
import com.kiryantsev.multiwidget.R
import com.kiryantsev.multiwidget.core.calendar.db.CalendarEventEntity
import com.kiryantsev.multiwidget.widget.ddFormatter
import com.kiryantsev.multiwidget.widget.hhMMFormatter
import kotlinx.datetime.LocalTime
import kotlinx.datetime.toJavaLocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun EventWidget(modifier: GlanceModifier = GlanceModifier, event: CalendarEventEntity, inFuture :Boolean = false) {
    Row(
        modifier.fillMaxWidth().padding(vertical = 4.dp)
    ) {
        Spacer(
            modifier.width(4.dp).height(42.dp).background(Color(event.color))
        )
        Column(GlanceModifier.padding(start = 8.dp)) {
            Text(event.title, maxLines = 1)
            Text(event.timesStr(inFuture))
        }
    }
}

@Composable
private fun CalendarEventEntity.timesStr(inFuture: Boolean): String {
    val startLocal = startDateLocal()
    val endLocal = endDateLocal()
    if (startLocal.time == LocalTime(0, 0, 0) && endLocal.time == LocalTime(23, 59, 59)) {
        //allday event
        return "${ddFormatter.format(startLocal.toJavaLocalDateTime())} " + LocalContext.current.getString(
            R.string.calendar_all_day
        )
    }
    if (startLocal.time == LocalTime(0, 0, 0) && endLocal.time == LocalTime(0, 0, 0)) {
        //multiday allday event
        return ddFormatter.format(startLocal.toJavaLocalDateTime()) +
                " - ${ddFormatter.format(endLocal.toJavaLocalDateTime())}"
    }

    if(inFuture){
        val jLocTime = startLocal.toJavaLocalDateTime()
        return ddFormatter.format(jLocTime) + " " + hhMMFormatter.format(jLocTime) +
                " - ${hhMMFormatter.format(endLocal.toJavaLocalDateTime())}"
    }
    return hhMMFormatter.format(startLocal.toJavaLocalDateTime()) +
            " - ${hhMMFormatter.format(endLocal.toJavaLocalDateTime())}"
}