package com.hciware.bitfields.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.hciware.bitfields.model.BitFieldsViewModel
import com.hciware.bitfields.model.Field
import com.hciware.bitfields.ui.theme.BitfieldmanipulatorTheme

@Composable
fun FieldEditor(
    field: Field,
    mode: BitFieldsViewModel.RadixMode,
    modifier: Modifier = Modifier
) {
    when (mode) {
        BitFieldsViewModel.RadixMode.Binary -> BinaryNumberEditors(modifier, field)
        BitFieldsViewModel.RadixMode.Hexadecimal -> HexNumberEditor(field)
        BitFieldsViewModel.RadixMode.Decimal -> DecimalNumberEditor(field)
    }
}

@Composable
private fun BinaryNumberEditors(
    modifier: Modifier,
    field: Field
) {
    Row(modifier) {
        for (bit in (field.endBit - 1).downTo(field.startBit - 1)) {
            val mask = (1L shl bit)
            val valueBit = if (((mask and field.getValue(mask)) == 0L)) 0 else 1
            BinaryNumberEditor(
                value = "$valueBit",
                { field.up(mask) },
                { field.down(mask) },
                { v -> field.setValue(((if (v == "1") 1L else 0L)), mask) },
                field.enabled
            )
        }
    }
}

@Preview
@Composable
fun PreviewFieldEditor() {
    val model = BitFieldsViewModel()
    val bitField = model.bitfields[1]
    BitfieldmanipulatorTheme {
        FieldEditor(
            bitField,
            mode = BitFieldsViewModel.RadixMode.Binary,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun BinaryNumberEditor(
    value: String,
    up: () -> Unit,
    down: () -> Unit,
    valueChanged: (String) -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .width(BitFieldsViewModel.widthPerBit + BitFieldsViewModel.bitPadding)
            .padding(start = BitFieldsViewModel.bitPadding)
    ) {
        IconButton(onClick = up, enabled = enabled) {
            Icon(Icons.Filled.KeyboardArrowUp, contentDescription = "Increase Value")
        }
        // TODO: For binary editing.. makes     little sense to allow keyboard entry.. clicking the text but should toggle the bit?
        TextField(value = value, onValueChange = valueChanged, enabled = enabled)
        IconButton(onClick = down, enabled = enabled) {
            Icon(Icons.Filled.KeyboardArrowDown, contentDescription = "Decrease Value")
        }
    }
}

@Preview
@Composable
fun PreviewNumberEditor() {
    BitfieldmanipulatorTheme {
        BinaryNumberEditor(
            "1", {}, {}, {},false)
    }
}

// TODO: For Hex fields that contain more than one nibble - split the nibbles and align with bits in bit ruler..
@OptIn(ExperimentalStdlibApi::class)
@Composable
fun HexNumberEditor(
    field: Field,
    modifier: Modifier = Modifier
) {
    Column(
        modifier
            .width((BitFieldsViewModel.widthPerBit + BitFieldsViewModel.bitPadding) * ((1 + field.endBit) - field.startBit))
            .padding(start = BitFieldsViewModel.bitPadding)
    ) {
        IconButton(onClick = { field.up() }, enabled = field.enabled,) {
            Icon(Icons.Filled.KeyboardArrowUp, contentDescription = "Increase Value")
        }

        TextField(
            value = field.getHexString(),
            enabled = field.enabled,
            onValueChange = { text -> field.setValue(text.hexToLong(), Long.MAX_VALUE) },
            modifier = Modifier.fillMaxWidth()
        )

        IconButton(onClick = { field.down() }, enabled = field.enabled,) {
            Icon(Icons.Filled.KeyboardArrowDown, contentDescription = "Decrease Value")
        }
    }
}

@Preview
@Composable
fun PreviewHexNumberEditor() {
    val field = BitFieldsViewModel().bitfields[0]
    BitfieldmanipulatorTheme {
        HexNumberEditor(field)
    }
}

@Composable
fun DecimalNumberEditor(
    field: Field,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .width((BitFieldsViewModel.widthPerBit + BitFieldsViewModel.bitPadding) * ((1 + field.endBit) - field.startBit))
            .padding(start = BitFieldsViewModel.bitPadding)
    ) {
        IconButton(onClick = { field.up() }, enabled = field.enabled,) {
            Icon(Icons.Filled.KeyboardArrowUp, contentDescription = "Increase Value")
        }

        TextField(
            value = "${field.getValue()}",
            enabled = field.enabled,
            onValueChange = { v -> field.setValue(v.toLong()) },
            modifier = Modifier.fillMaxWidth()
        )
        IconButton(onClick = { field.down() },enabled = field.enabled,) {
            Icon(Icons.Filled.KeyboardArrowDown, contentDescription = "Decrease Value")
        }
    }
}

@Preview
@Composable
fun PreviewDecimalNumberEditor() {
    val field = BitFieldsViewModel().bitfields[1]
    BitfieldmanipulatorTheme {
        DecimalNumberEditor(field)
    }
}