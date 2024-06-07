package com.hciware.bitfields.detail

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.hciware.bitfields.model.BitFieldsViewModel
import com.hciware.bitfields.model.Field
import com.hciware.bitfields.ui.theme.BitfieldmanipulatorTheme

@Composable
fun OverallValue(
    overallField: Field,
    mode: BitFieldsViewModel.RadixMode,
    modeSelected: (BitFieldsViewModel.RadixMode) -> Unit,
    commonScrollState: ScrollState,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        RadixSelector(title = "Overall Value", mode, modeSelected)
        FieldEditor(overallField, mode, modifier = Modifier.horizontalScroll(commonScrollState))
    }
}

@Preview
@Composable
fun PreviewOverallValue() {
    val model = BitFieldsViewModel()
    BitfieldmanipulatorTheme {
        OverallValue(
            model.bitfields[1],
            model.overallValueMode,
            { _ -> },
            rememberScrollState(),
            Modifier.fillMaxSize()
        )
    }
}