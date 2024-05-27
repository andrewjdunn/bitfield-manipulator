package com.hciware.bitfields.detail

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.hciware.bitfields.model.BitFieldsViewModel
import com.hciware.bitfields.model.BitfieldSection
import com.hciware.bitfields.ui.theme.BitfieldmanipulatorTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(name: String,
                 fields: List<BitfieldSection>,
                 overallValueMode: BitFieldsViewModel.RadixMode,
                 overallValueModeSelected: (BitFieldsViewModel.RadixMode) -> Unit,
                 fieldValuesMode: BitFieldsViewModel.RadixMode,
                 fieldValuesModeSelected: (BitFieldsViewModel.RadixMode) -> Unit,
                 modifier: Modifier = Modifier) {
    Scaffold (
        topBar = { TopAppBar(title = { Text(name) }) },
        floatingActionButton = { FloatingActionButton(onClick = {},content = {Text("Add")}) },
        content = {  padding -> DetailContent(
            fields,
            overallValueMode,
            overallValueModeSelected,
            fieldValuesMode,
            fieldValuesModeSelected,
            modifier.padding(padding))})
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    val model = BitFieldsViewModel()
    val name = model.bitfields[0].description.name
    val fields = model.bitfields[0].sections
    model.fieldsValueMode = BitFieldsViewModel.RadixMode.Decimal
    BitfieldmanipulatorTheme {
        DetailScreen(name, fields,
            model.overallValueMode, {_ ->},
            model.fieldsValueMode, {_ ->})
    }
}