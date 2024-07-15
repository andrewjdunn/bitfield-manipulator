package com.hciware.bitfields.model

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.hciware.bitfields.R
import com.hciware.bitfields.database.BitFieldDatabase
import com.hciware.bitfields.database.BitFieldSection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BitFieldsViewModel(application: Application = Application()) : AndroidViewModel(application) {


    private val _bitfieldList: BitfieldListData = if (application.baseContext == null)
        SampleBitfieldListData() else DatabaseBitfieldListData(application)
    private val _bitfields = mutableStateListOf<BitField>()

    enum class RadixMode {
        Binary, Hexadecimal, Decimal;

        @Composable
        fun getString(): String {
            return when (this) {
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

    init {
        viewModelScope.launch(Dispatchers.IO) {

            val bitfields = _bitfieldList.getBitfieldsWithSections()

            withContext(Dispatchers.Main) {
                bitfields.forEach { bitFieldWithSections ->
                    val newBitField = BitField(
                        BitfieldDescription(
                            bitFieldWithSections.bitField.fieldId,
                            mutableStateOf(bitFieldWithSections.bitField.name)
                        )
                    )
                    _bitfields.add(newBitField)
                    bitFieldWithSections.sectionList.forEach {
                        newBitField.addBitfieldSection(it.name, it.startBit, it.endBit)
                    }
                }
            }
        }
    }

    fun selectBitField(bitfield: BitField) {
        selectedBitField = bitfield
    }

    fun addNew() {
        val nextId = if (_bitfields.isNotEmpty()) _bitfields.maxOf { it.description.id } + 1 else 1
        val bf = BitField(
            BitfieldDescription(
                nextId,
                mutableStateOf("new")
            )
        )
        _bitfields.add(bf)

        viewModelScope.launch(Dispatchers.IO) {
            val bfId = _bitfieldList.insertBitField(com.hciware.bitfields.database.BitField(
                bf.description.id,
                bf.description.name.value
            ))
            bf.sections.forEach {
                _bitfieldList.insertBitFieldSection(
                    BitFieldSection(
                        bitFieldId = bfId,
                        name = it.name,
                        startBit = it.startBit,
                        endBit = it.endBit
                    )
                )
            }
        }
    }

    fun delete(bitfield: BitField) {
        _bitfields.remove(bitfield)
        viewModelScope.launch(Dispatchers.IO) {
            _bitfieldList.delete(com.hciware.bitfields.database.BitField(bitfield.description.id, bitfield.description.name.value))
        }
    }

    fun save(bitfield: BitField) {
        viewModelScope.launch(Dispatchers.IO) {
            _bitfieldList.updateBitField(
                com.hciware.bitfields.database.BitField(
                    fieldId = bitfield.description.id,
                    name = bitfield.description.name.value))

            // Update or delete the sections.. TODO: Think I'm making this all a bit too hard.. but anyway
            // TODO: Delete and add again.. not all that nice
            _bitfieldList.deleteFieldSections(bitFieldId = bitfield.description.id)

            // TODO: Also - this is a C&P
            bitfield.sections.forEach {
                _bitfieldList.insertBitFieldSection(
                    BitFieldSection(
                        bitFieldId = bitfield.description.id,
                        name = it.name,
                        startBit = it.startBit,
                        endBit = it.endBit
                    )
                )
            }

        }
    }
}

