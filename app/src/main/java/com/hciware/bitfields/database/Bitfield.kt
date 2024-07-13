package com.hciware.bitfields.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "bitfields")
data class BitField(
    @PrimaryKey val id:Int,
    @ColumnInfo(name = "name") val name:String
)

@Entity(tableName = "bitfield_section")
data class BitFieldSection(
    @PrimaryKey val id:Int,
    @ColumnInfo(name = "name") val name:String,
    @ColumnInfo(name = "start_bit") val startBit:Int,
    @ColumnInfo(name = "end_bit") val endBit:Int,
)