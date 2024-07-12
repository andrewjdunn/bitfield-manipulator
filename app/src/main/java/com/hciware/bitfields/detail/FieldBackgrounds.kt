package com.hciware.bitfields.detail

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hciware.bitfields.R
import com.hciware.bitfields.model.BitFieldsViewModel
import com.hciware.bitfields.model.BitfieldSection
import com.hciware.bitfields.ui.theme.BitfieldmanipulatorTheme

@Composable
fun FieldBackgrounds(fields: List<BitfieldSection>) {
    for (f in fields) {
        val bitSize = f.endBit - f.startBit + 1
        val width =
            dimensionResource(id = R.dimen.width_per_bit).times(bitSize) + dimensionResource(id = R.dimen.bit_padding).times(bitSize-1)
        Surface (
            color = f.color,
            modifier = Modifier
                .padding(start = dimensionResource(id = R.dimen.bit_padding))
                .width(width)
                .fillMaxHeight()
        ){}
    }
}

@Preview
@Composable
private fun PreviewFieldBackground() {
    BitfieldmanipulatorTheme {
        val model = BitFieldsViewModel().addSampledData()
        Row(modifier = Modifier
            .height(75.dp)
            .fillMaxWidth()) {
            FieldBackgrounds(model.bitfields[1].sections)
        }
    }
}