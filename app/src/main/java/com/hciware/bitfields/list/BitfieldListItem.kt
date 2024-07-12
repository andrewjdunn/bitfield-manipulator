package com.hciware.bitfields.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hciware.bitfields.R
import com.hciware.bitfields.ui.theme.BitfieldmanipulatorTheme


@Composable
fun BitFieldListItem(
    name: String,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
    ) {

    Surface (
        shape = MaterialTheme.shapes.medium,
        shadowElevation = 1.dp,
        color = MaterialTheme.colorScheme.primaryContainer,
        modifier = Modifier.padding(10.dp)
    ) {
        Row (
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()) {
            Text(
                text = name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = modifier
                    .padding(20.dp, end = 0.dp)
                    .weight(0.1f)
                    .clickable(onClick = onClick)
            )
            IconButton(onClick = { onDelete() }) {
                Icon(Icons.Filled.Delete, modifier = Modifier.weight(1f),
                    contentDescription = stringResource(id = R.string.delete))
            }

        }

    }
}

@Preview
@Composable
fun BitfieldListItemPreview() {
    BitfieldmanipulatorTheme {
        BitFieldListItem(
            "This text might not always fit on the screen",{},{},)
    }
}
