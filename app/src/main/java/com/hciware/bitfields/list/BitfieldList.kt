package com.hciware.bitfields.list

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.hciware.bitfields.model.BitField
import com.hciware.bitfields.model.BitFieldsViewModel
import com.hciware.bitfields.ui.theme.BitfieldmanipulatorTheme

@Composable
fun BitfieldList(
    list: List<BitField>,
    modifier: Modifier = Modifier,
    onSelect: (BitField) -> Unit = {},
    onDelete: (BitField) -> Unit = {}) {
    LazyColumn(modifier = modifier) {
        items(items = list, key = {item: BitField -> item.description.id }) {
            item: BitField -> BitFieldListItem(
            name = item.description.name.value,
            onClick = { onSelect(item) },
            onDelete = {onDelete(item)}
            )
        }
    }
}

@Preview
@Composable
fun BitfieldListPreview() {
    val model = BitFieldsViewModel()
    BitfieldmanipulatorTheme {
        BitfieldList(list = model.bitfields, modifier = Modifier.fillMaxSize())
    }
}