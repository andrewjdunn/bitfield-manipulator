package com.hciware.bitfields.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableLongStateOf

// ... Add unit test and fix model..
// .. Test and fix compose event stuff..

// Commit .. after this will need to improve the visuals - add boxes around stuff and colour
// based on bits etc,, and then finally - how do we edit a bit field (add/update/delete sections)


// TODO: MAX_VALUE is not all FFFFs need a ULONG For That.
interface Field {
    fun setValue(newValue: Long, mask:Long = Long.MAX_VALUE)
    fun getValue(mask: Long = Long.MAX_VALUE): Long
    fun getHexString(mask: Long = Long.MAX_VALUE): String
    fun up(mask: Long = Long.MAX_VALUE)
    fun down(mask:Long = Long.MAX_VALUE)
    val startBit: Int
    val endBit: Int
    val enabled: Boolean
}

@OptIn(ExperimentalStdlibApi::class)
private fun getHexStringForField(field: Field, mask: Long) : String {
    var hexString = field.getValue(mask)
        .toHexString(HexFormat{
            number.removeLeadingZeros = true
            upperCase = true
        })
    val bitCount = field.endBit-field.startBit
    val nibbles = ((bitCount) / 4) + if (bitCount % 4 > 0) 1  else 0
    println("${field.endBit} ${field.startBit} has $nibbles")
    if(hexString.length < nibbles) {
        hexString = "0".repeat(nibbles - hexString.length) + hexString
    }
    return hexString
}


data class BitfieldDescription (val id: Long, val name: String)

data class BitfieldSection (val bitField: BitField, val name: String, override val startBit: Int, override val endBit: Int, var value: MutableState<Long> = mutableLongStateOf(0L)) : Field {
    override fun setValue(newValue: Long, mask: Long) {
        // TODO: Not using mask... needed?
        val bitmask = (( 2L shl  (endBit-startBit) ) - 1) shl startBit
        value.value = newValue shl startBit
        // TODO: value.value... names a bit odd!
        this.bitField.value.value and bitmask.inv()
        this.bitField.value.value = this.bitField.value.value or (value.value and bitmask)
        println("Section $name Set value $newValue with mask $mask Section Value ${value.value} Bitfield Value ${bitField.value.value}")
    }

    override fun getValue(mask: Long): Long {
        return value.value and mask
    }
    override fun getHexString(mask: Long): String {
        return getHexStringForField(this, mask)
    }

    private fun getFirstBitOf(mask: Long): Int {
        var firstBit = 0
        while( ((1L shl firstBit) and mask) == 0L) {
            firstBit += 1
        }
        return firstBit
    }

    override fun up(mask: Long) {
        println("Increase field $name mask $mask field value is $startBit - $endBit")
        val currentMaskedValue = value.value and mask
        // We are incrementing the masked part of the field not the entire field..
        // But is the mask relative to the field??
        val firstBit = getFirstBitOf(mask)
        val currentMaskedShiftedValue = currentMaskedValue shr firstBit
        val newMaskedShiftedValue = currentMaskedShiftedValue + 1
        if(newMaskedShiftedValue >  currentMaskedShiftedValue) {
            setValue(newMaskedShiftedValue shl firstBit, mask)
        }
    }

    override fun down(mask: Long) {
        println("Decrease field $name mask $mask field value is $startBit - $endBit")
        val currentMaskedValue = value.value and mask
        // We are incrementing the masked part of the field not the entire field..
        // But is the mask relative to the field??
        val firstBit = getFirstBitOf(mask)
        val currentMaskedShiftedValue = currentMaskedValue shr firstBit
        val newMaskedShiftedValue = currentMaskedShiftedValue - 1
        if(newMaskedShiftedValue <  currentMaskedShiftedValue) {
            setValue(newMaskedShiftedValue shl firstBit, mask)
        }
    }

    override val enabled: Boolean
        // TODO: Not this..
        get() = name.isNotEmpty()
}

// TODO: is BitField one word (Bitfield?)
// TODO: I need to consider where the mutable states are.. setting a field should cause recomposition for that field and the overall value..
// How do I test that this is being done efficiently?
data class BitField(val description: BitfieldDescription, val value: MutableState<Long> = mutableLongStateOf(0L)) : Field {

    val sections: ArrayList<BitfieldSection> = ArrayList()
    fun addBitfieldSection(name: String, startBit: Int, endBit: Int) : BitField {
        if(sections.isNotEmpty())
        {
            val previousStartBit = sections.last().startBit
            if(previousStartBit - endBit > 1)
            {
                println("Gap before $name Prev start bit $previousStartBit")
                sections.add(BitfieldSection(this,"", previousStartBit - 1, endBit + 1))
            }
        }
        sections.add(BitfieldSection(this,name, startBit, endBit))
        return this
    }

    // TODO: Not sure how to use the masks here - when setting we only set bts in the mask, and getting returns only bits in the mask
    // But do we need to shift the value ready for display??... not sure  this is for when we want to get/set from a hex octet or binary bit..
    override fun setValue(newValue: Long, mask: Long) {
        value.value = newValue
        for (s in sections) {
            // Shift the top bits off
            val ourValue: Long = this@BitField.value.value shl (63 - endBit)

            // And shift to zero
            ourValue ushr (64 - ((endBit - startBit) + 1))

            s.setValue(ourValue, Long.MAX_VALUE)
        }
    }

    override fun getValue(mask: Long): Long {
        return value.value
    }

    override fun getHexString(mask: Long): String {
        return getHexStringForField(this, mask)
    }

    override fun up(mask: Long) {
        setValue(value.value + 1, mask)
    }

    override fun down(mask: Long) {
        setValue(value.value - 1, mask)
    }

    override val startBit: Int
        get() = 1
    override val endBit: Int
        get() = sections.maxOf { section -> section.endBit } + 1
    override val enabled: Boolean
        get() = true
}