package com.hciware.bitfields.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hciware.bitfields.model.BitFieldsViewModel
import com.hciware.bitfields.model.BitfieldSection
import com.hciware.bitfields.ui.theme.BitfieldmanipulatorTheme

@Composable
fun FieldBackgrounds(fields: List<BitfieldSection>) {
    for (f in fields) {
        val bitSize = f.endBit - f.startBit + 1
        val width =
            BitFieldsViewModel.widthPerBit.times(bitSize) + BitFieldsViewModel.bitPadding.times(bitSize-1)
        Box (
            modifier = Modifier
                .padding(start = BitFieldsViewModel.bitPadding)
                .width(width)
                .fillMaxHeight()
                .background(color = f.color)
        )
    }
}

@Preview
@Composable
private fun PreviewFieldBackground() {
    BitfieldmanipulatorTheme {
        val model = BitFieldsViewModel()
        Row(modifier = Modifier.height(75.dp).fillMaxWidth()) {
            FieldBackgrounds(model.bitfields[1].sections)
        }
    }
}