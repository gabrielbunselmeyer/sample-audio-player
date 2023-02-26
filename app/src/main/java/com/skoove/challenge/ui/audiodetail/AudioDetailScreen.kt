package com.skoove.challenge.ui.audiodetail

import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun AudioDetailScreen(audioTitle: String) {
    Text("title: $audioTitle")
    AudioDetail()
}

@Composable
private fun AudioDetail() {
//    Text("AudioDetail")
}
