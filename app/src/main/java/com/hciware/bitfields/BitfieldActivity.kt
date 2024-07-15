package com.hciware.bitfields

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.hciware.bitfields.ui.theme.BitfieldApp
import com.hciware.bitfields.ui.theme.BitfieldmanipulatorTheme

class BitfieldActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BitfieldmanipulatorTheme {
                BitfieldApp()
            }
        }
    }
}