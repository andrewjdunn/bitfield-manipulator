package com.hciware.bitfields.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AssistChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.hciware.bitfields.ui.theme.BitfieldmanipulatorTheme

// TODO: Don't forget that each bit has a colour that runs vertically from top to bottom
@Composable
fun BitRuler(modifier: Modifier = Modifier, bitCount: Int) {
    Row(modifier.background(MaterialTheme.colorScheme.background)) {
        for(bit in bitCount.downTo(1)) {
            AssistChip(onClick = { /*TODO*/ }, label = { Text("$bit") })
        }
    }
}

@Preview
@Composable
fun PreviewButRuler() {
    BitfieldmanipulatorTheme {
        BitRuler(bitCount = 8, modifier = Modifier.fillMaxWidth())
    }
}