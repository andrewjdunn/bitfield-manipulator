package com.hciware.bitfields.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hciware.bitfields.ui.theme.BitfieldmanipulatorTheme


@Composable
fun BitFieldListItem(
    name: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}) {
    Text( text = name,
        modifier = modifier
            .fillMaxWidth()
            .height(30.dp)
            .padding(5.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable(onClick = onClick))
}

@Preview
@Composable
fun BitfieldListItemPreview() {
    BitfieldmanipulatorTheme {
        BitFieldListItem("Name of Bitfield")
    }
}

/*
* From the tutorial.. can use some of the style?
* @Composable
    fun MessageCard(message: Message) {
        Row(modifier = Modifier.padding(all = 8.dp)) {
            Image(painter = painterResource(id = R.drawable.profile_picture),
                    contentDescription = "Profile picture",
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(40.dp)
                        .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape)
            )

            Spacer(modifier = Modifier.width(8.dp))

            var isExpanded by remember { mutableStateOf(false)}
            val surfaceColor by animateColorAsState(
                    if (isExpanded) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface, label = "",
            )

            Column(modifier = Modifier.clickable { isExpanded = !isExpanded }) {
                Text(text = message.author,
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.titleSmall)
                Spacer(modifier = Modifier.height(4.dp))

                Surface(
                        shape = MaterialTheme.shapes.medium,
                        shadowElevation = 1.dp,
                        color = surfaceColor,
                        modifier = Modifier
                            .animateContentSize()
                            .padding(1.dp)) {
                    Text(
                            message.body,
                            modifier = Modifier.padding(all = 4.dp),
                            maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                            style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
* */
