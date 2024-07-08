package com.hciware.bitfields.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hciware.bitfields.R
import com.hciware.bitfields.model.BitFieldsViewModel
import com.hciware.bitfields.model.BitfieldSection
import com.hciware.bitfields.ui.theme.BitfieldmanipulatorTheme

@Composable
fun FieldDefinitionEditor(field: BitfieldSection, modifier: Modifier = Modifier) {

    val bitCount = ((1 + field.endBit) - field.startBit)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .width(
                dimensionResource(id = R.dimen.width_per_bit).times(bitCount)
                        + dimensionResource(id = R.dimen.bit_padding).times(bitCount)
            )
            .padding(
                start = dimensionResource(id = R.dimen.bit_padding)
            )

    ) {
        IconButton(onClick = { TODO() }, enabled = field.enabled) {
            Icon(Icons.Filled.Edit, contentDescription = stringResource(id = R.string.edit))
        }

        // TODO: Set the height to the same as fieldeditor..
        if (field.enabled) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                IconButton(onClick = { TODO() }, modifier.size(20.dp)) {
                    Icon(
                        Icons.Filled.AddCircle,
                        contentDescription = stringResource(id = R.string.add_bit_left)
                    )
                }
                IconButton(onClick = { TODO() }, modifier.size(20.dp)) {
                    Icon(
                        Icons.Filled.AddCircle,
                        contentDescription = stringResource(id = R.string.add_bit_right)
                    )
                }
            }
        } else {
            IconButton(onClick = { TODO() }, modifier.size(20.dp)) {
                Icon(
                    Icons.Filled.AddCircle,
                    contentDescription = stringResource(id = R.string.add_field_section)
                )
            }
        }
        IconButton(onClick = { TODO() }, enabled = field.enabled) {
            Icon(Icons.Filled.Delete, contentDescription = stringResource(id = R.string.delete))
        }
    }
}

@Preview
@Composable
fun PreviewFieldDefinitionEditor() {
    val model = BitFieldsViewModel()
    val field = model.bitfields[1].sections[2]
    BitfieldmanipulatorTheme {
        Surface(
            onClick = { /*TODO*/ },
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        ) {
            FieldDefinitionEditor(field = field)
        }

    }
}