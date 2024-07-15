package com.hciware.bitfields.list


import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.hciware.bitfields.R
import com.hciware.bitfields.model.BitField
import com.hciware.bitfields.model.BitFieldsViewModel
import com.hciware.bitfields.ui.theme.BitfieldmanipulatorTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    selectBitField: (BitField) -> Unit,
    deleteBitField: (BitField) -> Unit,
    bitFieldsViewModel: BitFieldsViewModel,
    modifier: Modifier = Modifier) {

    Scaffold (
        topBar = { TopAppBar(title = { Text("Bitfields") }) },
        floatingActionButton = { FloatingActionButton(onClick = {bitFieldsViewModel.addNew()},content = {
            Icon(
                Icons.Filled.AddCircle,
                contentDescription = stringResource(id = R.string.add)
            )
        }) },
        content = {  padding -> BitfieldList(
            list = bitFieldsViewModel.bitfields,
            modifier = modifier.padding(padding),
            onSelect = {bitField -> selectBitField(bitField)},
            onDelete = {bitField -> deleteBitField(bitField) })})
}

@Preview
@Composable
fun ListScreenPreview(){
    val model = BitFieldsViewModel()
    BitfieldmanipulatorTheme {
        ListScreen(
            selectBitField = {},
            deleteBitField = {},
            bitFieldsViewModel =  model)
    }
}