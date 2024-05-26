package com.hciware.bitfields.list


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.hciware.bitfields.model.BitFieldsViewModel
import com.hciware.bitfields.ui.theme.BitfieldmanipulatorTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    modifier: Modifier = Modifier,
    bitFieldsViewModel: BitFieldsViewModel) {

    Scaffold (
        topBar = { TopAppBar(title = { Text("Bitfields") }) },
        floatingActionButton = { FloatingActionButton(onClick = {},content = {Text("Add")}) },
        // TODO: Odd - why the big gaps when in the scaffold - need to set spacing for the list
        content = {  padding -> BitfieldList(
            list = bitFieldsViewModel.bitfields,
            modifier.padding(padding).fillMaxSize(),
            onClick = {bitField -> bitFieldsViewModel.selectBitField(bitField)})} )
}

@Preview
@Composable
fun ListScreenPreview(){
    val model = BitFieldsViewModel()
    BitfieldmanipulatorTheme {
        ListScreen(bitFieldsViewModel =  model)
    }
}