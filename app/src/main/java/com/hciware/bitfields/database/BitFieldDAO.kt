package com.hciware.bitfields.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update

@Dao
interface BitFieldDAO {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertBitField(bitfields: BitField) : Long

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertBitFieldSection(bitFieldSection: BitFieldSection)

    @Update
    fun updateBitField(bitfield: BitField)

    @Update
    fun updateBitFieldSection(bitfield: BitFieldSection)

    @Delete
    fun delete(bitField: BitField)

    @Query("DELETE FROM bitfield_section where bitFieldId = :bitFieldId")
    fun deleteFieldSections(bitFieldId: Long)

    @Query("SELECT * FROM bitfields")
    fun loadAllBitFields() : Array<BitField>

    @Transaction
    @Query("SELECT * FROM bitfields")
    suspend fun getBitfieldsWithSections(): List<BitFieldWithSections>

}