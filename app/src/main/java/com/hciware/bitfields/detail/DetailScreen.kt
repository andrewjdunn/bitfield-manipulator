package com.hciware.bitfields.detail

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.hciware.bitfields.ui.theme.BitfieldmanipulatorTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(name: String, modifier: Modifier = Modifier) {
    Scaffold (
        topBar = { TopAppBar(title = { Text(name) }) },
        floatingActionButton = { FloatingActionButton(onClick = {},content = {Text("Add")}) },
        content = {  padding -> Text(
            text = "Bits and other things",
            modifier.padding(padding).fillMaxSize())})
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BitfieldmanipulatorTheme {
        DetailScreen("Sample BitField")
    }
}