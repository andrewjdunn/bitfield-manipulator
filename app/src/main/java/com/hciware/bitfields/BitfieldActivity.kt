package com.hciware.bitfields

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

import androidx.lifecycle.viewmodel.compose.viewModel
import com.hciware.bitfields.detail.DetailScreen
import com.hciware.bitfields.list.ListScreen
import com.hciware.bitfields.model.BitFieldsViewModel
import com.hciware.bitfields.ui.theme.BitfieldmanipulatorTheme

class BitfieldActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val bitFieldsViewModel: BitFieldsViewModel = viewModel()
            BitfieldmanipulatorTheme {
                // TODO: let else??
                if (bitFieldsViewModel.selectedBitField == null) {
                    ListScreen(bitFieldsViewModel = bitFieldsViewModel)
                } else {
                    bitFieldsViewModel.selectedBitField?.description?.name?.let {
                        DetailScreen(name = it)
                    }
                }
            }
        }
    }
}