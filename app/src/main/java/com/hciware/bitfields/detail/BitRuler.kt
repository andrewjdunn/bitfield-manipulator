package com.hciware.bitfields.detail

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.AssistChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.hciware.bitfields.model.BitFieldsViewModel
import com.hciware.bitfields.ui.theme.BitfieldmanipulatorTheme

// TODO: Don't forget that each bit has a colour that runs vertically from top to bottom
@Composable
fun BitRuler(commonScrollState: ScrollState, bitCount: Int, modifier: Modifier = Modifier) {
    Row(
        modifier
            .background(MaterialTheme.colorScheme.background)
            .horizontalScroll(commonScrollState
            )) {
        for(bit in bitCount.downTo(1)) {
            AssistChip(
                onClick = { /*TODO*/ },
                label = { Text("$bit",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                )},
                modifier
                    .padding(start = BitFieldsViewModel.bitPadding)
                    .width(BitFieldsViewModel.widthPerBit))
        }
    }
}

@Preview
@Composable
fun PreviewButRuler() {
    BitfieldmanipulatorTheme {
        BitRuler(
            rememberScrollState(),
            bitCount = 8,
            modifier = Modifier.fillMaxWidth())
    }
}