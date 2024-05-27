package com.hciware.bitfields.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel


class BitFieldsViewModel : ViewModel() {
    private val _bitfields = getSampleBitFields().toMutableList()

    enum class RadixMode {
        Binary, Hexadecimal, Decimal
    }

    val bitfields: List<BitField> get() = _bitfields
    var selectedBitField: BitField? by mutableStateOf(null)
    var overallValueMode: RadixMode by mutableStateOf(RadixMode.Binary)
    var fieldsValueMode: RadixMode by mutableStateOf(RadixMode.Binary)

    fun selectBitField(bitfield: BitField) {
        selectedBitField = bitfield
    }

    private fun getSampleBitFields() = listOf(
        BitField(
            BitfieldDescription(1,"IPV4 Address"),
            listOf(
                BitfieldSection(1,"Octet 1",0, 7),
                BitfieldSection(2,"Octet 2",8, 15),
                BitfieldSection(3,"Octet 3",16, 23),
                BitfieldSection(4,"Octet 4",24, 31))
        ),
        BitField(
            BitfieldDescription(2, "Result"),
            listOf(
                BitfieldSection(1, "Box Color", 0,4),
                BitfieldSection(2, "Label 1", 5, 9),
                // A Gap.. should be represented by whitespace..?
                BitfieldSection(3, "Success", 11, 11),
                BitfieldSection(3, "Stored", 12, 12))
        ))
}