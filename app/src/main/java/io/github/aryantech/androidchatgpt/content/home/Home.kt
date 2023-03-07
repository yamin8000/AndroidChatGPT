package io.github.aryantech.androidchatgpt.content.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent() {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Scaffold(
            content = { paddingValues ->
                Column(
                    modifier = Modifier.padding(paddingValues)
                ) {
                    Text("Hello there!")
                    Button(
                        onClick = {},
                        content = { Text("OK") },
                    )
                }
            }
        )
    }
}