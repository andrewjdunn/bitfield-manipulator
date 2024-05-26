package com.hciware.bitfields.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.hciware.bitfields.model.BitFieldsViewModel
import com.hciware.bitfields.model.BitfieldSection
import com.hciware.bitfields.ui.theme.BitfieldmanipulatorTheme

@Composable
fun FieldList(fields: List<BitfieldSection>, modifier: Modifier = Modifier) {

    Row(modifier.horizontalScroll(rememberScrollState())) {
        fields.forEach { field -> Field(field = field) }
    }
}

@Preview
@Composable
fun PreviewFiledList() {
    val model = BitFieldsViewModel()
    BitfieldmanipulatorTheme {
        FieldList(fields = model.bitfields[0].sections, Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background))
    }
}

@Composable
fun Field(field: BitfieldSection, modifier: Modifier = Modifier) {
    Column(modifier) {
        Text(text = field.name)
        // TODO: Will need to pass the field for
        FieldEditor(bitCount = field.end - field.start + 1)

    }
}

@Preview
@Composable
fun PreviewField() {
    val model = BitFieldsViewModel()
    val field = model.bitfields[0].sections[0]
    BitfieldmanipulatorTheme {
        Field(field = field,
            Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background))
    }
}