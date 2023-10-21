package com.kiryantsev.multiwidget.app.inappwidget

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class InAppWidgetViewModel : ViewModel() {


    private val _state = MutableStateFlow(InAppWidgetState())
    val state : StateFlow<InAppWidgetState> = _state.asStateFlow()
}