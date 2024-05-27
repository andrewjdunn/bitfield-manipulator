package com.hciware.bitfields.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.unit.dp
import com.hciware.bitfields.model.BitFieldsViewModel
import com.hciware.bitfields.ui.theme.BitfieldmanipulatorTheme

@Composable
fun FieldEditor(bitCount: Int, mode: BitFieldsViewModel.RadixMode, modifier: Modifier = Modifier) {
    // Return 1 of three editors - hex,dec,bin
    // set to passed in value
    // event sends new values
    // made up of a component per bit/hextet/declet (made them up)

    when(mode) {
        BitFieldsViewModel.RadixMode.Binary -> Row(modifier) {
            for(bit in bitCount.downTo(1)) {
                BinaryNumberEditor(value = "${bit % 2}")
            }
        }
        BitFieldsViewModel.RadixMode.Hexadecimal -> HexNumberEditor(bitCount)
        BitFieldsViewModel.RadixMode.Decimal -> DecimalNumberEditor(bitCount)
    }
}

@Preview
@Composable
fun PreviewFieldEditor() {
    BitfieldmanipulatorTheme {
        FieldEditor(8, BitFieldsViewModel.RadixMode.Hexadecimal, Modifier.fillMaxWidth())
    }
}

// TODO: Good practice to keep all the parts of this editor here?
// I think so as they won't be used in other places alone...
@Composable
fun BinaryNumberEditor(value: String, modifier: Modifier = Modifier) {
    Column(modifier) {
        IconButton(onClick = { /*TODO*/ }) {
            Icon(Icons.Filled.KeyboardArrowUp, contentDescription = "Increase Value")
        }
        // TODO: 40.dp... need to get this base don content maybe.. and it needs to
        // Match up with the bit ruler.. Which is does now by some freak chance :-)
        // TODO: For binary editing.. makes little sense to allow keyboard entry.. clicking the text but should toggle the bit?
        TextField(value = value, onValueChange = {}, modifier = Modifier.width(40.dp))
        IconButton(onClick = { /*TODO*/ }) {
            Icon(Icons.Filled.KeyboardArrowDown, contentDescription = "Decrease Value")
        }
    }
}

@Preview
@Composable
fun PreviewNumberEditor() {
    BitfieldmanipulatorTheme {
        BinaryNumberEditor("1")
    }
}

@Composable
fun HexNumberEditor(bitCount: Int, modifier: Modifier = Modifier) {
    Column(modifier) {
        IconButton(onClick = { /*TODO*/ }) {
            Icon(Icons.Filled.KeyboardArrowUp, contentDescription = "Increase Value")
        }
        // TODO: 40.dp... need to get this base don content maybe.. and it needs to
        // Match up with the bit ruler.. Which is does now by some freak chance :-)
        // TODO: For binary editing.. makes little sense to allow keyboard entry.. clicking the text but should toggle the bit?
        TextField(value = "H$bitCount", onValueChange = {}, modifier = Modifier.width(40.dp * bitCount))
        IconButton(onClick = { /*TODO*/ }) {
            Icon(Icons.Filled.KeyboardArrowDown, contentDescription = "Decrease Value")
        }
    }    
}

@Preview
@Composable
fun PreviewHexNumberEditor() {
    BitfieldmanipulatorTheme {
        HexNumberEditor(bitCount = 5)
    }
}

@Composable
fun DecimalNumberEditor(bitCount: Int, modifier: Modifier = Modifier) {
    Column(modifier) {
        IconButton(onClick = { /*TODO*/ }) {
            Icon(Icons.Filled.KeyboardArrowUp, contentDescription = "Increase Value")
        }
        // TODO: 40.dp... need to get this base don content maybe.. and it needs to
        // Match up with the bit ruler.. Which is does now by some freak chance :-)
        // TODO: For binary editing.. makes little sense to allow keyboard entry.. clicking the text but should toggle the bit?
        TextField(value = "D${bitCount}", onValueChange = {}, modifier = Modifier.width(40.dp * bitCount))
        IconButton(onClick = { /*TODO*/ }) {
            Icon(Icons.Filled.KeyboardArrowDown, contentDescription = "Decrease Value")
        }
    }
}

@Preview
@Composable
fun PreviewDecimalNumberEditor() {
    BitfieldmanipulatorTheme {
        DecimalNumberEditor(bitCount = 5)
    }
}