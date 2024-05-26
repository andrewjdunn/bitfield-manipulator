package com.hciware.bitfields.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.hciware.bitfields.ui.theme.BitfieldmanipulatorTheme

//https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary
// Not sure Radix is the right word...

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RadixSelector(title: String, modifier: Modifier = Modifier) {
    Column(modifier) {
        Text(title)
        val selectedIndex = 0
        val options = listOf("Binary", "Decimal", "Hexadecimal")
        SingleChoiceSegmentedButtonRow {
            options.forEachIndexed {
                index, label -> SegmentedButton(selected = index == selectedIndex,
                onClick = {},
                shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size)) {
                    Text(label)
            }
            }

        }
    }
}

@Preview
@Composable
fun PreviewRadixSelector() {
    BitfieldmanipulatorTheme {
        RadixSelector(title = "Overall Number", modifier = Modifier.fillMaxWidth())
    }
}