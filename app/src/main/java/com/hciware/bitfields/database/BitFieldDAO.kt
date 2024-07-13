package com.hciware.bitfields.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface BitFieldDAO {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertBitFields(vararg bitfields: BitField)

    @Update
    fun updateBitFields(vararg  bitfields: BitField)

    @Query("SELECT * FROM bitfields")
    fun loadAllBitFields() : Array<BitField>

}