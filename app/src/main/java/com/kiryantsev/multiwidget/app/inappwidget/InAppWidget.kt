@file:OptIn(ExperimentalMaterial3Api::class)

package com.kiryantsev.multiwidget.app.inappwidget

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavController


@Composable
fun InAppWidgetScreen(viewModel: InAppWidgetViewModel, navController: NavController) {
    val state = viewModel.state.collectAsState().value
    InAppWidgetScreenImpl(state = state)

}


@Composable
private fun InAppWidgetScreenImpl(state: InAppWidgetState){
    Scaffold {
        Row(Modifier.padding(it)) {

        }
    }
}