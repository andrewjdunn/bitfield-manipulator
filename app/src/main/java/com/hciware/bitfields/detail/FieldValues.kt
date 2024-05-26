package com.hciware.bitfields.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.hciware.bitfields.model.BitFieldsViewModel
import com.hciware.bitfields.model.BitfieldSection
import com.hciware.bitfields.ui.theme.BitfieldmanipulatorTheme

// TODO: I called Fields Sections before.. but they are fields, make this consistent
@Composable
fun FieldValues(fields: List<BitfieldSection>, modifier: Modifier = Modifier) {
    Column(modifier) {
        RadixSelector(title = "Field Values")
        // And now a list of fields...
        // TODO: Nomenclature is getting messy - the landing page is a list of bitfields - but its not its a list of a list of bitfields
        FieldList(fields)
    }

}

@Preview
@Composable
fun PreviewFieldValues() {
    BitfieldmanipulatorTheme {
        // TODO: Another creeping problem - are bits always described as 1 based or 0..
        val model = BitFieldsViewModel()
        FieldValues(model.bitfields[0].sections, Modifier.fillMaxWidth())
    }
}