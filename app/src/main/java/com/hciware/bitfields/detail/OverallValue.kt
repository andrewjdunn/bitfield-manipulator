package com.hciware.bitfields.detail

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hciware.bitfields.R
import com.hciware.bitfields.model.BitFieldsViewModel
import com.hciware.bitfields.model.BitfieldSection
import com.hciware.bitfields.model.Field
import com.hciware.bitfields.ui.theme.BitfieldmanipulatorTheme

@Composable
fun OverallValue(
    overallField: Field,
    fields: List<BitfieldSection>,
    mode: BitFieldsViewModel.RadixMode,
    modeSelected: (BitFieldsViewModel.RadixMode) -> Unit,
    commonScrollState: ScrollState,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        RadixSelector(title = stringResource(id = R.string.overall_value), mode, modeSelected, modifier = Modifier.padding(horizontal = 10.dp))
        Box {
            Row (modifier = Modifier
                .horizontalScroll(commonScrollState)
                .matchParentSize()) {
                FieldBackgrounds(fields)
            }
            FieldEditor(overallField, mode, modifier = Modifier.horizontalScroll(commonScrollState))
        }
    }
}

@Preview
@Composable
fun PreviewOverallValue() {
    val model = BitFieldsViewModel().addSampledData()
    BitfieldmanipulatorTheme {
        OverallValue(
            model.bitfields[1],
            model.bitfields[1].sections,
            model.overallValueMode,
            { _ -> },
            rememberScrollState(),
            Modifier.fillMaxSize()
        )
    }
}