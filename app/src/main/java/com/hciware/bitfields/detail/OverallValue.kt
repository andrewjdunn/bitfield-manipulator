package com.hciware.bitfields.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.hciware.bitfields.model.BitFieldsViewModel
import com.hciware.bitfields.ui.theme.BitfieldmanipulatorTheme

@Composable
fun OverallValue(bitCount: Int,
                 mode: BitFieldsViewModel.RadixMode,
                 modeSelected: (BitFieldsViewModel.RadixMode) -> Unit,
                 modifier: Modifier = Modifier) {
    Column(modifier) {
        RadixSelector(title = "Overall Value", mode, modeSelected)
        FieldEditor(bitCount)
    }
}

@Preview
@Composable
fun PreviewOverallValue() {
    val model = BitFieldsViewModel()
    BitfieldmanipulatorTheme {
        OverallValue(8,
            model.overallValueMode,
            {_ -> },
            Modifier.fillMaxSize())
    }
}