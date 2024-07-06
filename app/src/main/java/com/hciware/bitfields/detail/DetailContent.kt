package com.hciware.bitfields.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.hciware.bitfields.model.BitFieldsViewModel
import com.hciware.bitfields.model.BitfieldSection
import com.hciware.bitfields.model.Field
import com.hciware.bitfields.ui.theme.BitfieldmanipulatorTheme

@Composable
fun DetailContent(fields: List<BitfieldSection>,
                  overallValueMode: BitFieldsViewModel.RadixMode,
                  overallValueModeSelected: (BitFieldsViewModel.RadixMode) -> Unit,
                  fieldValuesMode: BitFieldsViewModel.RadixMode,
                  fieldValuesModeSelected: (BitFieldsViewModel.RadixMode) -> Unit,
                  overallField: Field,
                  modifier: Modifier = Modifier) {
    Column (modifier.verticalScroll(rememberScrollState())) {
        val commonScrollState = rememberScrollState()
        OverallValue(overallField,
            fields,
            overallValueMode,
            overallValueModeSelected,
            commonScrollState)
        Box  {
            Row(modifier = Modifier
                .horizontalScroll(commonScrollState)
                .matchParentSize()
            ) {
                FieldBackgrounds(fields)
            }
            BitRuler(commonScrollState, bitCount = overallField.endBit)
        }

        FieldValues(
            fields,
            fieldValuesMode,
            fieldValuesModeSelected,
            commonScrollState)
    }
}

@Preview
@Composable
fun PreviewDetailContent() {
    val model = BitFieldsViewModel()
    model.selectedBitField = model.bitfields[0]
    BitfieldmanipulatorTheme {
        DetailContent(model.bitfields[1].sections,
            model.overallValueMode,
            {_ ->},
            model.fieldsValueMode,
            {_ ->},
            model.selectedBitField!!,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background))
    }
}