package com.hciware.bitfields.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

interface Field {
    fun setValue(newValue: String, radix: Int = 10, mask:ULong = ULong.MAX_VALUE)
    fun getValue(radix: Int, mask: ULong = ULong.MAX_VALUE): String
    fun getInvalidValueText(radix: Int) : String
    fun getMaxValueStr(radix: Int) : String
    fun up(mask: ULong = ULong.MAX_VALUE)
    fun down(mask:ULong = ULong.MAX_VALUE)
    val startBit: Int
    val endBit: Int
    val enabled: Boolean
}

private fun getFirstBitOf(mask: ULong): Int {
    var firstBit = 0
    while( ((1UL shl firstBit) and mask) == 0UL) {
        firstBit += 1
    }
    return firstBit
}

private fun getLongValueOrZero(field: Field, mask: ULong): ULong {
    val longValue = try {
        field.getValue(10, mask).toULong()
    } catch (exception: NumberFormatException) {
        0UL
    }
    return longValue
}

private fun getLongValueOrZero(value: String, radix: Int): ULong {
    val longValue = try {
        value.toULong(radix)
    } catch (exception: NumberFormatException) {
        0UL
    }
    return longValue
}

@OptIn(ExperimentalStdlibApi::class)
private fun getHexStringForField(field: Field, mask: ULong) : String {
    var hexString = field.getValue(16, mask)

        //.toHexString(HexFormat{
        //    number.removeLeadingZeros = true
        //    upperCase = true
        //})
    val bitCount = field.endBit-field.startBit
    val nibbles = ((bitCount) / 4) + if (bitCount % 4 > 0) 1  else 0
    println("${field.endBit} ${field.startBit} has $nibbles")
    if(hexString.length < nibbles) {
        hexString = "0".repeat(nibbles - hexString.length) + hexString
    }
    return hexString
}

private fun getMaxValueStr(radix: Int, field: Field) : String {
    return if (field.enabled)  ((1 shl (field.endBit - field.startBit) + 1) -1).toString(radix) else ""
}

private fun up(mask: ULong, field: Field) {

    val currentMaskedValue =  getLongValueOrZero(field, mask)
    val firstBit = getFirstBitOf(mask)
    val currentMaskedShiftedValue = currentMaskedValue shr firstBit
    val newMaskedShiftedValue = currentMaskedShiftedValue + 1UL

    val maxMaskedValue = mask shr firstBit
    if(newMaskedShiftedValue in (currentMaskedShiftedValue + 1UL)..maxMaskedValue) {
        field.setValue((newMaskedShiftedValue).toString(), 10, mask)
    }
}

private fun down(mask: ULong, field: Field) {
    val currentMaskedValue = getLongValueOrZero(field, mask)
    val newMaskedShiftedValue = currentMaskedValue - 1UL

    if(newMaskedShiftedValue in 0UL..<currentMaskedValue) {
        val firstBit = getFirstBitOf(mask)
        field.setValue((newMaskedShiftedValue shl firstBit).toString(), 10, mask)
    }
}

data class BitfieldDescription (val id: Long, val name: String)

data class BitfieldSection (val bitField: BitField, val name: String, override val startBit: Int, override val endBit: Int, var value: MutableState<String> = mutableStateOf("0")) : Field {
    override fun setValue(newValue: String, radix: Int, mask: ULong) {

        if(newValue.isNotEmpty()) {
            val shiftedValue = getLongValueOrZero(newValue, radix) shl getFirstBitOf(mask)
            val valueWithoutMaskedBits = (getLongValueOrZero(value.value, 10) and mask.inv())
            val valueWithNewValueBits = (valueWithoutMaskedBits or shiftedValue)

            value.value = (valueWithNewValueBits).toString()
        } else {
            value.value = (getLongValueOrZero(value.value, 10) and mask.inv()).toString().replace("0", "")
        }
        this.bitField.recalculateValue()

        println("Section $name Set value $newValue with mask $mask Section Value ${value.value} Bitfield Value ${bitField.value.value}")
    }

    fun sectionMask() : ULong {
        var mask = 0UL

        for(i in 0..63) {
            if(i in startBit..endBit) {
                mask = mask or (1UL shl i)
            }
        }
        return mask
    }

    internal fun recalculateValue() {
        val mask = sectionMask()
        val maskedValue =  getLongValueOrZero(bitField.value.value, 10) and mask
        value.value = (maskedValue shr startBit).toString()
    }

    override fun getValue(radix: Int, mask: ULong): String {
        return if(value.value.isNotEmpty()) {
            ((getLongValueOrZero(value.value, 10) and mask) shr getFirstBitOf(mask)).toString(radix)
        } else {
            value.value
        }
    }

    override fun getMaxValueStr(radix: Int) : String {
        return getMaxValueStr(radix, this)
    }


    override fun up(mask: ULong) {
        println("Increase field $name mask $mask field value is ${value.value} start bit $startBit - end bit $endBit")
        up(mask, this)
    }

    override fun down(mask: ULong) {
        println("Decrease field $name mask $mask field value is $startBit - $endBit")
        down(mask, this)
    }

    override val enabled: Boolean
        get() = name.isNotEmpty()
}

data class BitField(val description: BitfieldDescription, val value: MutableState<String> = mutableStateOf("0")) : Field {

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
        var newValue = 0UL
        for(s in sections) {
            newValue = newValue or ( (getLongValueOrZero(s.value.value, 10) shl s.startBit) and s.sectionMask())
        }
        value.value = newValue.toString()
    }

    override fun setValue(newValue: String, radix: Int, mask: ULong) {

        if(newValue.isNotEmpty()) {
            val valueWithoutMaskedBits = getLongValueOrZero(value.value, 10) and mask.inv()
            val longValue = getLongValueOrZero(newValue, radix) shl getFirstBitOf(mask)
            val valueWithNewValueBits = (valueWithoutMaskedBits or (longValue and mask))

            value.value = valueWithNewValueBits.toString()
        } else {
            value.value = (getLongValueOrZero(value.value, 10) and mask.inv()).toString().replace("0", "")
        }

        for (s in sections) {
            s.recalculateValue()
        }
    }

    override fun getValue(radix: Int, mask: ULong): String {
        return if(value.value.isNotEmpty()) {
            val firstBit = getFirstBitOf(mask)
            ((getLongValueOrZero(value.value, 10) and mask) shr firstBit).toString(radix)
        } else {
            value.value
        }
    }
    override fun getMaxValueStr(radix: Int): String {
        return getMaxValueStr(radix, this)
    }

    override fun up(mask: ULong) {
        println("Increase overall field mask $mask field value is ${value.value} start bit $startBit - end bit $endBit")
        up(mask, this)
    }

    override fun down(mask: ULong) {
        down(mask, this)
    }

    override val startBit: Int
        get() = 0
    override val endBit: Int
        get() = sections.maxOf { section -> section.endBit }
    override val enabled: Boolean
        get() = true
}