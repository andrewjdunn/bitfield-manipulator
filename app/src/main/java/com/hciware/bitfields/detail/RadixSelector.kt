package com.hciware.bitfields.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.hciware.bitfields.R
import com.hciware.bitfields.model.BitFieldsViewModel
import com.hciware.bitfields.ui.theme.BitfieldmanipulatorTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RadixSelector(
    title: String,
    selectedMode: BitFieldsViewModel.RadixMode,
    onSelect: (BitFieldsViewModel.RadixMode) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Text(title, modifier = Modifier.padding(start = dimensionResource(id = R.dimen.add_left_box)))
        val options = BitFieldsViewModel.RadixMode.entries
        SingleChoiceSegmentedButtonRow {
            options.forEachIndexed { index, mode ->
                SegmentedButton(
                    selected = index == selectedMode.ordinal,
                    onClick = {onSelect(mode)},
                    shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size)
                ) {
                    Text(mode.getString())
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewRadixSelector() {
    BitfieldmanipulatorTheme {
        RadixSelector(title = "Overall Number",
            BitFieldsViewModel.RadixMode.Decimal, { _ -> },  modifier = Modifier.fillMaxWidth())
    }
}