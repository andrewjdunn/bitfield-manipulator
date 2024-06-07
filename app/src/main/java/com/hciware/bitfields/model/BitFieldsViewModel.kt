package com.hciware.bitfields.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel


class BitFieldsViewModel : ViewModel() {
    private val _bitfields = getSampleBitFields().toMutableList()

    enum class RadixMode {
        Binary, Hexadecimal, Decimal
    }


    companion object {
        // TODO: Need a common place this so that all the numbers can line up. here is good for now
        val widthPerBit = 50.dp
        val bitPadding = 5.dp
    }

    val bitfields: List<BitField> get() = _bitfields
    var selectedBitField: BitField? by mutableStateOf(null)
    var overallValueMode: RadixMode by mutableStateOf(RadixMode.Binary)
    var fieldsValueMode: RadixMode by mutableStateOf(RadixMode.Binary)

    fun selectBitField(bitfield: BitField) {
        selectedBitField = bitfield
    }

    // TODO: We want to keep this list indefinitely - not when hooked up to the database - but when previewing. somehow
    // Maybe a implementation of a model interface..s
    private fun getSampleBitFields() = listOf(
        BitField(
            BitfieldDescription(1,"IPV4 Address"))
            .addBitfieldSection("Octet 4",24, 31)
            .addBitfieldSection("Octet 3",16, 23)
            .addBitfieldSection("Octet 2",8, 15)
            .addBitfieldSection("Octet 1",0, 7),
        BitField(
            BitfieldDescription(2, "Result"))
            .addBitfieldSection( "Stored", 12, 12)
            // TODO: Success is too big for a 1 bit field so it makes for a gap after Success..  try alternating the labels?
            .addBitfieldSection( "Success", 11, 11)
            .addBitfieldSection( "Label 1", 5, 9)
            .addBitfieldSection( "Box Color", 0,4))
}

