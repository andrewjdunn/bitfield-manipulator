package com.hciware.bitfields.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.hciware.bitfields.model.BitFieldsViewModel
import com.hciware.bitfields.model.BitfieldSection
import com.hciware.bitfields.ui.theme.BitfieldmanipulatorTheme
import kotlin.math.max

// If the field names are large they will overlap.. single bit fields will be interesting..
// Maybe have the value editors in a single row but the names alternate top or bottom?
// I think its ok to have the field get bigger just to fit the label - the bits etc will
// just need to be further apart

// How do we edit the field length and label?? not sure I've thought that far
// Maybe have an edit bitfield button and then we drag the field and the labels become edited able in the same layout??

@Composable
fun DetailContent(fields: List<BitfieldSection>, modifier: Modifier = Modifier) {
    Column (modifier) {
        // TODO: Model can supply this - the last end bit rounding to 8?
        val bitCount = 8
        OverallValue(bitCount = bitCount)
        BitRuler(bitCount = bitCount)
        FieldValues(fields)
    }
}

@Preview
@Composable
fun PreviewDetailContent() {
    val model = BitFieldsViewModel()
    BitfieldmanipulatorTheme {
        DetailContent(model.bitfields[0].sections, modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background))
    }
}