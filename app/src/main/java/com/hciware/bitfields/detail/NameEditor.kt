package com.hciware.bitfields.detail

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Card
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.hciware.bitfields.ui.theme.BitfieldmanipulatorTheme

@Composable
fun NameEditor(onDismissRequest: (String) -> Unit, name: String) {
    var newName by rememberSaveable { mutableStateOf(name) }
    Dialog(onDismissRequest = { onDismissRequest(name) }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            TextField(
                singleLine = true,
                value = newName,
                onValueChange = { newName = it},
                keyboardActions = KeyboardActions (
                    onDone = {onDismissRequest(newName)}
                ),
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            )
        }
    }
}

@Preview
@Composable
fun PreviewNameEditor() {
    BitfieldmanipulatorTheme {
        NameEditor({}, "Hello")
    }
}