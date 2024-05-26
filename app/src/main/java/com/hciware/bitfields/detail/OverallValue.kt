package com.hciware.bitfields.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.hciware.bitfields.ui.theme.BitfieldmanipulatorTheme

@Composable
fun OverallValue(bitCount: Int, modifier: Modifier = Modifier) {
    Column(modifier) {
        RadixSelector(title = "Overall Value")
        FieldEditor(bitCount)
    }
}

@Preview
@Composable
fun PreviewOverallValue() {
    BitfieldmanipulatorTheme {
        OverallValue(8, Modifier.fillMaxSize())
    }
}