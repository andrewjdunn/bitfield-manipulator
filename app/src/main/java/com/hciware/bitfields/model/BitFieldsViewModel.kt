package com.hciware.bitfields.model

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import com.hciware.bitfields.R

class BitFieldsViewModel : ViewModel() {
    private val _bitfields = mutableStateListOf<BitField>()

    enum class RadixMode {
        Binary, Hexadecimal, Decimal;

        @Composable
        fun getString() : String {
            return when(this) {
                Binary -> stringResource(id = R.string.binary)
                Hexadecimal -> stringResource(id = R.string.hexadecimal)
                Decimal -> stringResource(id = R.string.decimal)
            }
        }
    }

    val bitfields: List<BitField> get() = _bitfields
    var selectedBitField: BitField? by mutableStateOf(null)
    var overallValueMode: RadixMode by mutableStateOf(RadixMode.Binary)
    var fieldsValueMode: RadixMode by mutableStateOf(RadixMode.Binary)
    var editMode: Boolean by mutableStateOf(false)

    fun selectBitField(bitfield: BitField) {
        selectedBitField = bitfield
    }

    fun addNew() {
        val nextId = if(_bitfields.isNotEmpty()) _bitfields.maxOf { it.description.id } + 1 else 1
        _bitfields.add(BitField(
            BitfieldDescription(nextId,
                mutableStateOf("new") )
        ).addBitfieldSection("new",0,3))
    }

    fun delete(bitfield: BitField) {
        _bitfields.remove(bitfield)
    }

    fun addSampledData(): BitFieldsViewModel {
        _bitfields.addAll(getSampleBitFields())
        return this
    }


    private fun getSampleBitFields() = listOf(
        BitField(
            BitfieldDescription(1, mutableStateOf("IPV4 Address")))
            .addBitfieldSection("Octet 4",24, 31)
            .addBitfieldSection("Octet 3",16, 23)
            .addBitfieldSection("Octet 2",8, 15)
            .addBitfieldSection("Octet 1",0, 7),
        BitField(
            BitfieldDescription(2, mutableStateOf("Result")))
            .addBitfieldSection( "Stored", 12, 12)
            .addBitfieldSection( "Success", 11, 11)
            .addBitfieldSection( "Label 1", 5, 9)
            .addBitfieldSection( "Box Color", 0,4))
}

