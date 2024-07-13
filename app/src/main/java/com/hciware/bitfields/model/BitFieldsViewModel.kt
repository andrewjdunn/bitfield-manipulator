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

    // TODO: This means none of the previews work as they use the model.. need to separate this
    private val db = Room.databaseBuilder(
        context = application,
        BitFieldDatabase::class.java, "bitfield"
    ).build()

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
        val bitfieldDao = db.bitFieldDao()

        viewModelScope.launch(Dispatchers.IO) {

            val bitfields = bitfieldDao.getBitfieldsWithSections()

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
        ).addBitfieldSection("new", 0, 3)
        _bitfields.add(bf)

        viewModelScope.launch(Dispatchers.IO) {
            val bitfieldDao = db.bitFieldDao()
            val bfId = bitfieldDao.insertBitField(com.hciware.bitfields.database.BitField(
                bf.description.id,
                bf.description.name.value
            ))
            bf.sections.forEach {
                bitfieldDao.insertBitFieldSection(
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
            val bitfieldDao = db.bitFieldDao()
            bitfieldDao.delete(com.hciware.bitfields.database.BitField(bitfield.description.id, bitfield.description.name.value))
            bitfieldDao.deleteFieldSections(bitFieldId = bitfield.description.id)
        }
    }

    fun save(bitfield: BitField) {
        viewModelScope.launch(Dispatchers.IO) {
            val bitfieldDao = db.bitFieldDao()
            bitfieldDao.updateBitField(
                com.hciware.bitfields.database.BitField(
                    fieldId = bitfield.description.id,
                    name = bitfield.description.name.value))

            // Update or delete the sections.. TODO: Think I'm making this all a bit too hard.. but anyway
            // TODO: Delete and add again.. not all that nice
            bitfieldDao.deleteFieldSections(bitFieldId = bitfield.description.id)

            // TODO: Also - this is a C&P
            bitfield.sections.forEach {
                bitfieldDao.insertBitFieldSection(
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

    fun addSampledData(): BitFieldsViewModel {
        _bitfields.addAll(getSampleBitFields())
        return this
    }


    private fun getSampleBitFields() = listOf(
        BitField(
            BitfieldDescription(1, mutableStateOf("IPV4 Address"))
        )
            .addBitfieldSection("Octet 4", 24, 31)
            .addBitfieldSection("Octet 3", 16, 23)
            .addBitfieldSection("Octet 2", 8, 15)
            .addBitfieldSection("Octet 1", 0, 7),
        BitField(
            BitfieldDescription(2, mutableStateOf("Result"))
        )
            .addBitfieldSection("Stored", 12, 12)
            .addBitfieldSection("Success", 11, 11)
            .addBitfieldSection("Label 1", 5, 9)
            .addBitfieldSection("Box Color", 0, 4)
    )
}

