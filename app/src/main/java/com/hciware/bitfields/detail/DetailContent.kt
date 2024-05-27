package com.hciware.bitfields.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.hciware.bitfields.model.BitFieldsViewModel
import com.hciware.bitfields.model.BitfieldSection
import com.hciware.bitfields.ui.theme.BitfieldmanipulatorTheme

// If the field names are large they will overlap.. single bit fields will be interesting..
// Maybe have the value editors in a single row but the names alternate top or bottom?
// I think its ok to have the field get bigger just to fit the label - the bits etc will
// just need to be further apart

// How do we edit the field length and label?? not sure I've thought that far
// Maybe have an edit bitfield button and then we drag the field and the labels become edited able in the same layout??

@Composable
fun DetailContent(fields: List<BitfieldSection>,
                  overallValueMode: BitFieldsViewModel.RadixMode,
                  overallValueModeSelected: (BitFieldsViewModel.RadixMode) -> Unit,
                  fieldValuesMode: BitFieldsViewModel.RadixMode,
                  fieldValuesModeSelected: (BitFieldsViewModel.RadixMode) -> Unit,
                  bitCount: Int,
                  modifier: Modifier = Modifier) {
    Column (modifier.verticalScroll(rememberScrollState())) {
        // TODO: Any need to add to the view model?
        val commonScrollState = rememberScrollState()
        OverallValue(bitCount = bitCount, overallValueMode, overallValueModeSelected, commonScrollState)
        BitRuler(commonScrollState, bitCount = bitCount)
        FieldValues(fields, fieldValuesMode, fieldValuesModeSelected, commonScrollState)
    }
}

@Preview
@Composable
fun PreviewDetailContent() {
    val model = BitFieldsViewModel()
    BitfieldmanipulatorTheme {
        DetailContent(model.bitfields[0].sections,
            model.overallValueMode,
            {_ ->},
            model.fieldsValueMode,
            {_ ->},
            8,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background))
    }
}