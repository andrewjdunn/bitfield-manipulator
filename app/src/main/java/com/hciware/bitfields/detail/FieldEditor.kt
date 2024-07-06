package com.hciware.bitfields.detail

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
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
        for (bit in (field.endBit).downTo(field.startBit)) {
            val mask = (1UL shl bit - field.startBit)
            BinaryNumberEditor(
                value = field.getValue(2, mask),
                { field.up(mask) },
                { field.down(mask) },
                { field.setValue(if (field.getValue(2, mask) == "0") "1" else "0", 2, mask)},
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
    toggle: () -> Unit,
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

        val source = remember {
            MutableInteractionSource()
        }.also { mutableInteractionSource -> LaunchedEffect(mutableInteractionSource) {
            mutableInteractionSource.interactions.collect {
                if (enabled && it is PressInteraction.Release ) {
                    toggle()
                }
            }
        } }

        TextField(value = value, readOnly = true, onValueChange = { _ -> }, interactionSource = source)
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
            "1", {}, {}, {}, false)
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
            value = field.getValue(16),
            enabled = field.enabled,
            onValueChange = { text -> field.setValue(text, 16) },
            isError = !field.isValueValid,
            supportingText = {Text(text = field.getInvalidValueText(16),
                maxLines = 1, modifier = Modifier.fillMaxWidth())},
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
            value = field.getValue(10),
            enabled = field.enabled,
            onValueChange = { v -> field.setValue(v, 10) },
            isError = !field.isValueValid,
            supportingText = {Text(text = field.getInvalidValueText(10),
                maxLines = 1, modifier = Modifier.fillMaxWidth())},
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