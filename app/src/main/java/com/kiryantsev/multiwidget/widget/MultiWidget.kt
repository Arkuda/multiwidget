package com.kiryantsev.multiwidget.widget


import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.Preferences
import androidx.glance.Button
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.CircularProgressIndicator
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.updateAll
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.kiryantsev.multiwidget.R
import com.kiryantsev.multiwidget.app.MainActivity
import com.kiryantsev.multiwidget.core.calendar.db.CalendarEventEntity
import com.kiryantsev.multiwidget.widget.widgets.EventsWidget
import com.kiryantsev.multiwidget.widget.widgets.WeatherWidget
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.json.Json

class MultiWidget : GlanceAppWidget() {

    override val stateDefinition: GlanceStateDefinition<*> = PreferencesGlanceStateDefinition

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            val jsonState = currentState<Preferences>()[MultiWidgetReceiver.PREF_KEY]
            val state = jsonState?.let { Json.decodeFromString<MultiWidgetGlobalState>(it) }
            MyContent(state)
        }
    }

    @Composable
    private fun MyContent(state: MultiWidgetGlobalState?) {
        if(state == null){
            CircularProgressIndicator()
        }else {
            when (state.state) {
                MultiWidgetGlobalStateEnum.NEED_CONFIGURE -> NeedConfigWidget()
                MultiWidgetGlobalStateEnum.ERROR -> Text(text = state.error ?: "Some internal error")
                MultiWidgetGlobalStateEnum.OK -> Widget(state)
                MultiWidgetGlobalStateEnum.LOADING -> CircularProgressIndicator()
            }
        }

    }

    @Composable
    private fun Widget(
        state: MultiWidgetGlobalState
    ) {
        val ctx = LocalContext.current
        if (state.weather != null) {
            Box(
                modifier = GlanceModifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd
            ) {
                Row(modifier = GlanceModifier.fillMaxSize()) {
                    WeatherWidget(state.weather)
                    EventsWidget(state.calendarEvents)
                }

                Row {
                    Text(
                        text = state.weather.getFetchTimeInstant().toMMhhStr(),
                        style = TextStyle(color = ColorProvider(Color.Black.copy(alpha = .5f)))
                    )
                    Image(
                        modifier = GlanceModifier.clickable {
                            runBlocking { updateAll(ctx) }
                        },
                        provider = ImageProvider(R.drawable.refresh_24),
                        contentDescription = "Refresh"
                    )
                }
            }
        } else {
            Text("Weather is unknown")
        }
    }


    @Composable
    private fun NeedConfigWidget() {
        Column(
            modifier = GlanceModifier.fillMaxSize(),
            verticalAlignment = Alignment.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = LocalContext.current.getString(R.string.before_start_title),
                modifier = GlanceModifier.padding(12.dp).fillMaxWidth(),
                style = TextStyle(textAlign = TextAlign.Center)
            )
            Row(horizontalAlignment = Alignment.CenterHorizontally) {
                Button(
                    text = LocalContext.current.getString(R.string.before_start_open_app),
                    onClick = actionStartActivity<MainActivity>()
                )
            }
        }
    }
}

private fun Instant.toMMhhStr(): String {
    val time = toLocalDateTime(timeZone = TimeZone.currentSystemDefault()).time
    return String.format("%02d:%02d", time.hour, time.minute)
}


