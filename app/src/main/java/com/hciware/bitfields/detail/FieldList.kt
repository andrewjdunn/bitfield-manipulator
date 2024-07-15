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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hciware.bitfields.R
import com.hciware.bitfields.model.BitField
import com.hciware.bitfields.model.BitFieldsViewModel
import com.hciware.bitfields.model.BitfieldDescription
import com.hciware.bitfields.model.BitfieldSection
import com.hciware.bitfields.ui.theme.BitfieldmanipulatorTheme

@Composable
fun FieldList(
    fields: List<BitfieldSection>,
    mode: BitFieldsViewModel.RadixMode,
    commonScrollState: ScrollState,
    editMode: Boolean,
    addSectionLeft: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier
            .horizontalScroll(commonScrollState)
            .height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {

        Surface(
            modifier = Modifier
                .width(dimensionResource(id = R.dimen.add_left_box))
                .align(Alignment.CenterVertically)
        ) {
            if (editMode) {
                IconButton(onClick = { addSectionLeft() }, modifier.size(20.dp)) {
                    Icon(
                        Icons.Filled.AddCircle,
                        contentDescription = stringResource(id = R.string.add_field_section)
                    )
                }
            }
        }

        fields.forEach { field ->
            Field(
                field = field,
                mode,
                editMode
            )
        }
    }
}

@Preview
@Composable
fun PreviewFiledList() {
    val bitField = BitField(BitfieldDescription(0, mutableStateOf("Sample")))
    val sections = listOf(
        BitfieldSection(bitField, name = "Section 2", startBit = 3, _color = Color.Green,endBit = 6),
        BitfieldSection(bitField, name = "Section 1", startBit = 0, _color = Color.Cyan ,endBit = 2)
    )
    BitfieldmanipulatorTheme {
        FieldList(
            fields = sections,
            BitFieldsViewModel.RadixMode.Decimal,
            rememberScrollState(),
            editMode = true,
            {},
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
    editMode: Boolean,
    modifier: Modifier = Modifier
) {
    Column(modifier.width((dimensionResource(id = R.dimen.width_per_bit) + dimensionResource(id = R.dimen.bit_padding)) * ((1 + field.endBit) - field.startBit))) {

        Text(
            text = field.name,
            style = MaterialTheme.typography.labelSmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = modifier.padding(start = 10.dp)
        )

        Text(
            text = field.getMaxValueStr(
                when (mode) {
                    BitFieldsViewModel.RadixMode.Binary -> 2
                    BitFieldsViewModel.RadixMode.Hexadecimal -> 16
                    BitFieldsViewModel.RadixMode.Decimal -> 10
                }
            ),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = modifier.padding(start = 10.dp)
        )
        if (editMode) {
            FieldDefinitionEditor(field)
        } else {
            FieldEditor(
                field,
                mode
            )
        }
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
            false,
            Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
        )
    }
}