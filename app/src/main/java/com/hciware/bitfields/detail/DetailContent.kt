package com.hciware.bitfields.detail

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.hciware.bitfields.R
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
                  editMode: Boolean,
                  addSectionLeft: () -> Unit,
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
                Row {
                    Surface(modifier = Modifier .width(dimensionResource(id = R.dimen.add_left_box))){}
                    FieldBackgrounds(fields)
                }
            }
            Row(modifier = Modifier.horizontalScroll(commonScrollState)) {
                Surface(modifier = Modifier
                    .width(dimensionResource(id = R.dimen.add_left_box))){}
                BitRuler(bitCount = overallField.endBit)
            }
        }

        FieldValues(
            fields,
            fieldValuesMode,
            fieldValuesModeSelected,
            commonScrollState,
            editMode,
            addSectionLeft)
    }
}

@Preview
@Composable
fun PreviewDetailContent() {
    val model = BitFieldsViewModel(Application())
    model.selectedBitField = model.bitfields[0]
    BitfieldmanipulatorTheme {
        DetailContent(model.bitfields[1].sections,
            model.overallValueMode,
            {_ ->},
            model.fieldsValueMode,
            {_ ->},
            model.selectedBitField!!,
            editMode = true,
            {},
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background))
    }
}