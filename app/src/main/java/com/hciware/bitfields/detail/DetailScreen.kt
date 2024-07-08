package com.hciware.bitfields.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.hciware.bitfields.R
import com.hciware.bitfields.model.BitFieldsViewModel
import com.hciware.bitfields.model.BitfieldSection
import com.hciware.bitfields.model.Field
import com.hciware.bitfields.ui.theme.BitfieldmanipulatorTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    name: String,
    fields: List<BitfieldSection>,
    overallValueMode: BitFieldsViewModel.RadixMode,
    overallValueModeSelected: (BitFieldsViewModel.RadixMode) -> Unit,
    fieldValuesMode: BitFieldsViewModel.RadixMode,
    fieldValuesModeSelected: (BitFieldsViewModel.RadixMode) -> Unit,
    overallField: Field,
    editMode: Boolean,
    setEditMode: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {


    Scaffold(
        topBar = {
            TopAppBar(title = {
                if (editMode) {
                    TextField(value = name, onValueChange = {})
                } else {
                    Text(name)
                }
            })
        },

        floatingActionButton = {
            if (editMode) {
                FloatingActionButton(onClick = { setEditMode(false) }, content = {
                    Image(
                        painter = painterResource(id = android.R.drawable.ic_menu_save),
                        contentDescription = stringResource(id = R.string.save)
                    )
                })
            } else {
                FloatingActionButton(onClick = { setEditMode(true) }, content = {
                    Image(
                        painter = painterResource(id = android.R.drawable.ic_menu_edit),
                        contentDescription = stringResource(id = R.string.edit)
                    )
                })
            }
        },
        content = { padding ->
            DetailContent(
                fields,
                overallValueMode,
                overallValueModeSelected,
                fieldValuesMode,
                fieldValuesModeSelected,
                overallField,
                editMode,
                modifier.padding(padding)
            )
        })
}

@Preview(showBackground = true)
@Composable
fun DetailScreenPreview() {
    val model = BitFieldsViewModel()
    val name = model.bitfields[1].description.name
    val fields = model.bitfields[1].sections
    model.fieldsValueMode = BitFieldsViewModel.RadixMode.Decimal
    model.selectedBitField = model.bitfields[1]
    BitfieldmanipulatorTheme {
        DetailScreen(
            name, fields,
            model.overallValueMode, { _ -> },
            model.fieldsValueMode, { _ -> },
            model.selectedBitField!!,
            true,
            { _ -> }
        )
    }
}