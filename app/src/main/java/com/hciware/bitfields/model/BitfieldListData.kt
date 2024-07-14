package com.hciware.bitfields.model

import android.app.Application
import androidx.room.Room
import com.hciware.bitfields.database.BitField
import com.hciware.bitfields.database.BitFieldDatabase
import com.hciware.bitfields.database.BitFieldSection
import com.hciware.bitfields.database.BitFieldWithSections

interface BitfieldListData {
    fun getBitfieldsWithSections(): List<BitFieldWithSections>
    fun insertBitField(bitField: BitField): Long
    fun insertBitFieldSection(bitFieldSection: BitFieldSection)
    fun delete(bitField: BitField)
    fun deleteFieldSections(bitFieldId: Long)
    fun updateBitField(bitField: BitField)
}


class SampleBitfieldListData : BitfieldListData {
    override fun getBitfieldsWithSections(): List<BitFieldWithSections> {
        return listOf(
            BitFieldWithSections(
                BitField(1, "IPV4 Address"),
                sectionList = listOf(
                    BitFieldSection(bitFieldId = 1, name = "Octet 4", startBit = 24, endBit = 31),
                    BitFieldSection(bitFieldId = 1, name = "Octet 3", startBit = 16, endBit = 23),
                    BitFieldSection(bitFieldId = 1, name = "Octet 2", startBit = 8, endBit = 15),
                    BitFieldSection(bitFieldId = 1, name = "Octet 1", startBit = 0, endBit = 7)
                )
            ),
            BitFieldWithSections(
                BitField(2, "Result"),
                sectionList = listOf(
                    BitFieldSection(bitFieldId = 2, name = "Stored", startBit = 12, endBit = 12),
                    BitFieldSection(bitFieldId = 2, name = "Success", startBit = 11, endBit = 11),
                    BitFieldSection(bitFieldId = 2, name = "Label", startBit = 5, endBit = 9),
                    BitFieldSection(bitFieldId = 2, name = "Box Color", startBit = 0, endBit = 4),
                )
            )
        )
    }

    override fun insertBitField(bitField: BitField): Long {
        TODO("Not yet implemented")
    }

    override fun insertBitFieldSection(bitFieldSection: BitFieldSection) {
        TODO("Not yet implemented")
    }

    override fun delete(bitField: BitField) {
        TODO("Not yet implemented")
    }

    override fun deleteFieldSections(bitFieldId: Long) {
        TODO("Not yet implemented")
    }

    override fun updateBitField(bitField: BitField) {
        TODO("Not yet implemented")
    }
}

class DatabaseBitfieldListData(application: Application) : BitfieldListData {

    private val db = Room.databaseBuilder(
        context = application,
        BitFieldDatabase::class.java, "bitfield"
    ).build()

    override fun getBitfieldsWithSections(): List<BitFieldWithSections> {
        return db.bitFieldDao().getBitfieldsWithSections()
    }

    override fun insertBitField(bitField: BitField): Long {
        return db.bitFieldDao().insertBitField(bitField)
    }

    override fun insertBitFieldSection(bitFieldSection: BitFieldSection) {
        db.bitFieldDao().insertBitFieldSection(bitFieldSection)
    }

    override fun delete(bitField: BitField) {
        db.bitFieldDao().delete(bitField)
    }

    override fun deleteFieldSections(bitFieldId: Long) {
        db.bitFieldDao().deleteFieldSections(bitFieldId)
    }

    override fun updateBitField(bitField: BitField) {
        db.bitFieldDao().updateBitField(bitField)
    }

}