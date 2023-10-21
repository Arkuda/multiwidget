package com.kiryantsev.multiwidget.widget.widgets

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceModifier
import androidx.glance.LocalContext
import androidx.glance.appwidget.cornerRadius
import androidx.glance.background
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.width
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import com.kiryantsev.multiwidget.R
import com.kiryantsev.multiwidget.core.calendar.db.CalendarEventEntity
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun EventsWidget(events: List<CalendarEventEntity>) {

    val eventsToFilter = mutableListOf<CalendarEventEntity>().apply { addAll(events) }
    val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    val todayDayEnd = LocalDateTime(now.date, LocalTime(23, 59, 59))

    if (eventsToFilter.isEmpty()) {
        Text("Dont have events to show")
        return
    }

    val todayEvents = events.filter { it.startDateLocal() < todayDayEnd }

    if (todayEvents.size > 4) {
        //only today
        Row(GlanceModifier.fillMaxWidth()) {
            Column(
                GlanceModifier.defaultWeight().cornerRadius(6.dp)
                    .background(Color.White)
            ) {
                todayEvents.subList(0, 3).toWidgets()
            }
            Spacer(GlanceModifier.width(6.dp))
            Column(
                GlanceModifier.defaultWeight().cornerRadius(6.dp)
                    .background(Color.White)
            ) {
                todayEvents.subList(4, minOf(todayEvents.size, 6)).toWidgets()
            }
        }

    } else if (todayEvents.isEmpty()) {
        //only tomorrow
        Column(
            GlanceModifier.fillMaxWidth().cornerRadius(6.dp)
                .background(Color.White)
        ) {
            Text(
                modifier = GlanceModifier.fillMaxWidth(),
                style = TextStyle(textAlign = TextAlign.Center),
                text = LocalContext.current.getString(R.string.calendar_not_today),
            )
            Row {
                Column(GlanceModifier.defaultWeight()) {
                    eventsToFilter.subList(0, minOf(eventsToFilter.size, 3)).toWidgets()
                }
                if (eventsToFilter.size > 3) {
                    Spacer(GlanceModifier.width(6.dp))
                    Column(GlanceModifier.defaultWeight()) {
                        eventsToFilter.subList(4, minOf(eventsToFilter.size, 6)).toWidgets()
                    }
                }
            }
        }

    } else {
        eventsToFilter.removeAll(todayEvents.filter { !it.isMultiDayEvent() })
        //today + tomorrow
        Row(GlanceModifier.fillMaxWidth()) {
            Column(
                GlanceModifier.defaultWeight().cornerRadius(6.dp)
                    .background(Color.White)
            ) {
                Text(
                    modifier = GlanceModifier.fillMaxWidth(),
                    style = TextStyle(textAlign = TextAlign.Center),
                    text = LocalContext.current.getString(R.string.calendar_today)
                )
                todayEvents.subList(0, minOf(todayEvents.size, 3)).toWidgets()
            }
            Spacer(GlanceModifier.width(6.dp))
            Column(
                GlanceModifier.defaultWeight().cornerRadius(6.dp)
                    .background(Color.White)
            ) {
                Text(
                    modifier = GlanceModifier.fillMaxWidth(),
                    style = TextStyle(textAlign = TextAlign.Center),
                    text = LocalContext.current.getString(R.string.calendar_not_today)
                )
                eventsToFilter.subList(0, minOf(eventsToFilter.size, 3)).toWidgets()
            }
        }
    }

}

@Composable
private fun List<CalendarEventEntity>.toWidgets(inFuture :Boolean = false) {
    this.forEach {
        EventWidget(event = it,)
    }
}
