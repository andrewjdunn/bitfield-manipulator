package com.hciware.bitfields.detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hciware.bitfields.R
import com.hciware.bitfields.ui.theme.BitfieldmanipulatorTheme

@Composable
fun BitRuler(bitCount: Int) {
    Row {
        for (bit in bitCount.downTo(0)) {
            Surface(
                shape = MaterialTheme.shapes.small,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier
                    .padding(start = dimensionResource(id = R.dimen.bit_padding))
                    .width(dimensionResource(id = R.dimen.width_per_bit))
            ) {
                Text(
                    "${bit + 1}",
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewButRuler() {
    BitfieldmanipulatorTheme {
        BitRuler(bitCount = 32)
    }
}