package com.hciware.bitfields.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hciware.bitfields.R
import com.hciware.bitfields.model.BitFieldsViewModel
import com.hciware.bitfields.model.BitfieldSection
import com.hciware.bitfields.model.Field
import com.hciware.bitfields.ui.theme.BitfieldmanipulatorTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    name: String,
    setOverallName: (String) -> Unit,
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
            var editingName by rememberSaveable { mutableStateOf(false) }
            TopAppBar(title = {
                Row {
                    Text(name)
                    if (editMode) {
                        IconButton(onClick = { editingName = true }, enabled = !editingName,
                            modifier = Modifier.size(25.dp)) {
                            Icon(
                                Icons.Filled.Edit,
                                contentDescription = stringResource(id = R.string.edit)
                            )
                        }
                    }
                }
            })
            if(editingName) {
                NameEditor({
                    editingName = false
                    setOverallName(it)}, name)
            }
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
    val model = BitFieldsViewModel().addSampledData()
    val name = model.bitfields[1].description.name
    val fields = model.bitfields[1].sections
    model.fieldsValueMode = BitFieldsViewModel.RadixMode.Decimal
    model.selectedBitField = model.bitfields[1]
    BitfieldmanipulatorTheme {
        DetailScreen(
            name.value,
            {},
            fields,
            model.overallValueMode, { _ -> },
            model.fieldsValueMode, { _ -> },
            model.selectedBitField!!,
            true,
            { _ -> }
        )
    }
}