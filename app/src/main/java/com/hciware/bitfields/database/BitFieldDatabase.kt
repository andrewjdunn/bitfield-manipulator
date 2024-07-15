package com.hciware.bitfields.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [BitField::class, BitFieldSection::class], version = 1)
abstract class BitFieldDatabase :RoomDatabase() {
    abstract fun bitFieldDao() : BitFieldDAO

}