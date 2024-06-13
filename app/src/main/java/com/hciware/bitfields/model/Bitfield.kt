package com.hciware.bitfields.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableLongStateOf

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

private fun getFirstBitOf(mask: Long): Int {
    var firstBit = 0
    while( ((1L shl firstBit) and mask) == 0L) {
        firstBit += 1
    }
    return firstBit
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

private fun up(mask: Long, field: Field) {
    val currentMaskedValue =  field.getValue(mask)
    val firstBit = getFirstBitOf(mask)
    val currentMaskedShiftedValue = currentMaskedValue shr firstBit
    val newMaskedShiftedValue = currentMaskedShiftedValue + 1

    val maxMaskedValue = mask shr firstBit

    if(newMaskedShiftedValue in (currentMaskedShiftedValue + 1)..maxMaskedValue) {
        field.setValue(newMaskedShiftedValue shl firstBit, mask)
    }
}

private fun down(mask: Long, field: Field) {
    val currentMaskedValue = field.getValue(mask)
    val firstBit = getFirstBitOf(mask)
    val currentMaskedShiftedValue = currentMaskedValue shr firstBit
    val newMaskedShiftedValue = currentMaskedShiftedValue - 1

    if(newMaskedShiftedValue in 0..<currentMaskedShiftedValue) {
        field.setValue(newMaskedShiftedValue shl firstBit, mask)
    }
}

data class BitfieldDescription (val id: Long, val name: String)

data class BitfieldSection (val bitField: BitField, val name: String, override val startBit: Int, override val endBit: Int, var value: MutableState<Long> = mutableLongStateOf(0L)) : Field {
    override fun setValue(newValue: Long, mask: Long) {

        val unShiftedBitMask = (( 2L shl  (endBit-startBit) ) - 1)
        val bitmask = unShiftedBitMask shl startBit
        val maskedMask = unShiftedBitMask and mask
        val shiftedValue = newValue shl getFirstBitOf(bitmask)

        val valueWithoutMaskedBits = (value.value and maskedMask.inv() shl getFirstBitOf(bitmask))
        val valueWithNewValueBits = (valueWithoutMaskedBits or shiftedValue) and bitmask

        value.value = valueWithNewValueBits shr startBit
        this.bitField.recalculateValue()
        println("Section $name Set value $newValue with mask $mask Section Value ${value.value} Bitfield Value ${bitField.value.value}")
    }

    internal fun recalculateValue() {
        var mask = 0L
        for(i in 0..63) {
            if(i in startBit..endBit) {
                mask = mask or (1L shl i)
            }
        }
        val maskedValue = bitField.value.value and mask
        value.value = maskedValue shr startBit
    }

    override fun getValue(mask: Long): Long {
        return value.value and mask
    }
    override fun getHexString(mask: Long): String {
        return getHexStringForField(this, mask)
    }

    override fun up(mask: Long) {
        println("Increase field $name mask $mask field value is ${value.value} start bit $startBit - end bit $endBit")
        up(mask, this)
    }

    override fun down(mask: Long) {
        println("Decrease field $name mask $mask field value is $startBit - $endBit")
        down(mask, this)
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

    internal fun recalculateValue() {
        var newValue = 0L
        for(s in sections) {
            newValue = newValue or (s.value.value shl s.startBit)
        }
        value.value = newValue
    }

    override fun setValue(newValue: Long, mask: Long) {

        val valueWithoutMaskedBits = value.value and mask.inv()
        val valueWithNewValueBits = (valueWithoutMaskedBits or newValue)// and mask

        value.value = valueWithNewValueBits
        for (s in sections) {
            s.recalculateValue()
        }
    }

    override fun getValue(mask: Long): Long {
        return value.value and mask
    }

    override fun getHexString(mask: Long): String {
        return getHexStringForField(this, mask)
    }

    override fun up(mask: Long) {
        up(mask, this)
    }

    override fun down(mask: Long) {
        down(mask, this)
    }

    override val startBit: Int
        get() = 0
    override val endBit: Int
        get() = sections.maxOf { section -> section.endBit }
    override val enabled: Boolean
        get() = true
}