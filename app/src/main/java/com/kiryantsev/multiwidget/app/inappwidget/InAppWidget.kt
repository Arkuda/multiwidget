@file:OptIn(ExperimentalMaterial3Api::class)

package com.kiryantsev.multiwidget.app.inappwidget

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier


@Composable
fun InAppWidget(viewModel: InAppWidgetViewModel) {
    val state = viewModel.state.collectAsState().value
    InAppWidgetImpl(state = state)

}


@Composable
private fun InAppWidgetImpl(state: InAppWidgetState){
    Scaffold {
        Row(Modifier.padding(it)) {

        }
    }
}