package com.hciware.bitfields.detail

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hciware.bitfields.model.BitFieldsViewModel
import com.hciware.bitfields.model.BitfieldSection
import com.hciware.bitfields.ui.theme.BitfieldmanipulatorTheme

@Composable
fun FieldList(
    fields: List<BitfieldSection>,
    mode: BitFieldsViewModel.RadixMode,
    commonScrollState: ScrollState,
    modifier: Modifier = Modifier
) {
    Row(
        modifier
            .horizontalScroll(commonScrollState)
            .height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        fields.forEach { field ->
            Field(
                field = field,
                mode
            )
            VerticalDivider(
                color = MaterialTheme.colorScheme.outlineVariant,
                modifier = Modifier
                    .offset(x = 2.5.dp)
            )

        }
    }
}

@Preview
@Composable
fun PreviewFiledList() {
    val model = BitFieldsViewModel()
    BitfieldmanipulatorTheme {
        FieldList(
            fields = model.bitfields[0].sections,
            BitFieldsViewModel.RadixMode.Decimal,
            rememberScrollState(),
            Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
        )
    }
}

@Composable
fun Field(
    field: BitfieldSection,
    mode: BitFieldsViewModel.RadixMode,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Text(
            text = field.name,
            style = MaterialTheme.typography.labelSmall,
            modifier = modifier.padding(start = 10.dp)
        )
        Text(text = field.getMaxValueStr(when(mode) {
            BitFieldsViewModel.RadixMode.Binary -> 2
            BitFieldsViewModel.RadixMode.Hexadecimal -> 16
            BitFieldsViewModel.RadixMode.Decimal -> 10
        }),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = modifier.padding(start = 10.dp)
        )
        FieldEditor(
            field,
            mode
        )
    }
}

@Preview
@Composable
fun PreviewField() {
    val model = BitFieldsViewModel()
    val field = model.bitfields[0].sections[0]
    BitfieldmanipulatorTheme {
        Field(
            field = field,
            BitFieldsViewModel.RadixMode.Decimal,
            Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
        )
    }
}