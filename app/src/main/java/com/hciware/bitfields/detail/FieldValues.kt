package com.hciware.bitfields.detail

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hciware.bitfields.R
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
    editMode: Boolean,
    addSectionLeft: () -> Unit,
    modifier: Modifier = Modifier
) {

    Box {
        Row(modifier = Modifier
            .horizontalScroll(commonScrollState)
            .matchParentSize()
        ) {
            Surface(modifier = Modifier
                .width(dimensionResource(id = R.dimen.add_left_box))){}
            FieldBackgrounds(fields)
        }

        Column(modifier) {
            RadixSelector(title = stringResource(id = R.string.field_values), mode, modeSelected,  modifier = Modifier.padding(horizontal = 10.dp))
            FieldList(fields, mode, commonScrollState, editMode, addSectionLeft)
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
            false,
            {},
            Modifier.fillMaxWidth()
        )
    }
}
