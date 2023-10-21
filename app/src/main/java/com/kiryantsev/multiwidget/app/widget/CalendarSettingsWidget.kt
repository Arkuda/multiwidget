package com.kiryantsev.multiwidget.app.widget

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kiryantsev.multiwidget.R

@Composable
fun CalendarSettingsWidget(calendarPermissionIsGranted: Boolean, onRequestPermission : ()-> Unit) {
    Column(
        modifier = Modifier.padding(all = if(!calendarPermissionIsGranted) 16.dp else 0.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (!calendarPermissionIsGranted) {
            Text(
                stringResource(R.string.calendar_settings_grant_permission_title),
                textAlign = TextAlign.Center
            )
            Button(onClick = onRequestPermission) {
                Text(stringResource(R.string.calendar_settings_grant_permission_action))
            }
        }
    }

}
