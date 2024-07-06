package com.hciware.bitfields.detail

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.hciware.bitfields.model.BitFieldsViewModel
import com.hciware.bitfields.model.BitfieldSection
import com.hciware.bitfields.ui.theme.BitfieldmanipulatorTheme

// TODO: I called Fields Sections before.. but they are fields, make this consistent
@Composable
fun FieldValues(
    fields: List<BitfieldSection>,
    mode: BitFieldsViewModel.RadixMode,
    modeSelected: (BitFieldsViewModel.RadixMode) -> Unit,
    commonScrollState: ScrollState,
    modifier: Modifier = Modifier
) {

    Box {
        Row(modifier = Modifier
            .horizontalScroll(commonScrollState)
            .matchParentSize()
        ) {
            FieldBackgrounds(fields)
        }

        Column(modifier) {
            RadixSelector(title = "Field Values", mode, modeSelected)
            FieldList(fields, mode, commonScrollState)
        }
    }
}

@Preview
@Composable
fun PreviewFieldValues() {
    BitfieldmanipulatorTheme {
        // TODO: Another creeping problem - are bits always described as 1 based or 0..
        val model = BitFieldsViewModel()
        FieldValues(
            model.bitfields[1].sections,
            model.fieldsValueMode,
            { _ -> },
            rememberScrollState(),
            Modifier.fillMaxWidth()
        )
    }
}
