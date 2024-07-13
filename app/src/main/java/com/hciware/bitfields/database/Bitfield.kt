package com.hciware.bitfields.database

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation


@Entity(tableName = "bitfields")
data class BitField(
    @PrimaryKey(autoGenerate = true) val fieldId:Long = 0,
    @ColumnInfo(name = "name") val name:String
)

@Entity(tableName = "bitfield_section")
data class BitFieldSection(
    @PrimaryKey(autoGenerate = true) val sectionId:Long = 0,
    val bitFieldId: Long,
    val name:String,
    @ColumnInfo(name = "start_bit") val startBit:Int,
    @ColumnInfo(name = "end_bit") val endBit:Int,
)

data class BitFieldWithSections(
    @Embedded val bitField: BitField,
    @Relation(
        parentColumn = "fieldId",
        entityColumn = "bitFieldId")
    val sectionList: List<BitFieldSection>
)