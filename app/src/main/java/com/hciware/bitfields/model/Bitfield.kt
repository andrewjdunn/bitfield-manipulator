package com.hciware.bitfields.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.graphics.Color

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
    val isValueValid: Boolean
}

private fun getFirstBitOf(mask: ULong): Int {
    var firstBit = 0
    while( ((1UL shl firstBit) and mask) == 0UL) {
        firstBit += 1
    }
    return firstBit
}

private fun getULongValueOrZero(field: Field, mask: ULong): ULong {
    val longValue = try {
        field.getValue(10, mask).toULong()
    } catch (exception: NumberFormatException) {
        0UL
    }
    return longValue
}

private fun getULongValueOrZero(value: String, radix: Int): ULong {
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

    val currentMaskedValue = getULongValueOrZero(field, mask)
    val firstBit = getFirstBitOf(mask)
    val currentMaskedShiftedValue = currentMaskedValue shr firstBit
    val newMaskedShiftedValue = currentMaskedShiftedValue + 1UL

    val maxMaskedValue = mask shr firstBit
    if(newMaskedShiftedValue in (currentMaskedShiftedValue + 1UL)..maxMaskedValue) {
        field.setValue((newMaskedShiftedValue).toString(), 10, mask)
    }
}

private fun down(mask: ULong, field: Field) {
    val currentMaskedValue = getULongValueOrZero(field, mask)
    val newMaskedShiftedValue = currentMaskedValue - 1UL

    if(newMaskedShiftedValue in 0UL..<currentMaskedValue) {
        val firstBit = getFirstBitOf(mask)
        field.setValue((newMaskedShiftedValue shl firstBit).toString(), 10, mask)
    }
}

data class BitfieldDescription (val id: Long, var name: MutableState<String>)

data class BitfieldSection (
    val bitField: BitField,
    val name: String,
    override val startBit: Int,
    override val endBit: Int,
    private val _color: Color = bitField.getNextColour(),
    var value: MutableState<String> = mutableStateOf("0")) : Field {
    override fun setValue(newValue: String, radix: Int, mask: ULong) {

        if (newValue.isNotEmpty()) {
            val shiftedValue = getULongValueOrZero(newValue, radix) shl getFirstBitOf(mask)
            val valueWithoutMaskedBits = (getULongValueOrZero(value.value, 10) and mask.inv())
            val valueWithNewValueBits = (valueWithoutMaskedBits or shiftedValue)

            value.value = (valueWithNewValueBits).toString()
        } else {
            value.value = (getULongValueOrZero(value.value, 10) and mask.inv()).toString().replace("0", "")
        }
        this.bitField.recalculateValue()

        println("Section $name Set value $newValue with mask $mask Section Value ${value.value} Bitfield Value ${bitField.value.value}")
    }

    fun sectionMask(): ULong {
        var mask = 0UL

        for (i in 0..63) {
            if (i in startBit..endBit) {
                mask = mask or (1UL shl i)
            }
        }
        return mask
    }

    internal fun recalculateValue() {
        val mask = sectionMask()
        val maskedValue = getULongValueOrZero(bitField.value.value, 10) and mask
        value.value = (maskedValue shr startBit).toString()
    }

    override fun getValue(radix: Int, mask: ULong): String {
        return if (value.value.isNotEmpty()) {
            ((getULongValueOrZero(
                value.value,
                10
            ) and mask) shr getFirstBitOf(mask)).toString(radix)
        } else {
            value.value
        }
    }

    override fun getInvalidValueText(radix: Int): String {
        return if (isValueValid || !enabled) "" else "> ${getMaxValueStr(radix)}"
    }

    override fun getMaxValueStr(radix: Int): String {
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

    // TODO:Add unit test for these editors
    fun delete() {
        bitField.removeBitfieldSection(this)
    }

    fun addMe() {
        // TODO; Add string resource for new... or generate a name based on bits? or index (section 2..)/
        bitField.replaceBitfieldSection("new", this)
    }

    fun addBitLeft() {
        bitField.expandFieldLeft(this)
    }

    fun addBitRight() {
        bitField.expandFieldRight(this)
    }

    fun setName(newName: String) {
        bitField.replaceBitfieldSection(newName, this)
    }

    override val enabled: Boolean
        get() = name.isNotEmpty()

    override val isValueValid: Boolean
        get() = enabled && getULongValueOrZero(
            this,
            ULong.MAX_VALUE
        ) <= getMaxValueStr(10).toULong()

    val colorConstant: Color get() = _color

    val color: Color
        get() =
            when (enabled) {
                true -> _color
                false -> Color.Transparent
            }
}



data class BitField(val description: BitfieldDescription, val value: MutableState<String> = mutableStateOf("0")) : Field {

    private val sectionsState = listOf<BitfieldSection>().toMutableStateList()
    val sections: MutableList<BitfieldSection> get() = sectionsState

    fun addBitfieldSection(name: String, startBit: Int, endBit: Int) : BitField {
        sections.add(BitfieldSection(this,name, startBit, endBit))
        refreshGapFieldSections()
        return this
    }

    fun removeBitfieldSection(section: BitfieldSection) {
        sections.remove(section)
        refreshGapFieldSections()
        recalculateValue()
    }

    fun replaceBitfieldSection(newName:String, section: BitfieldSection) {
        sections[sections.indexOf(section)] = BitfieldSection(this, newName, section.startBit, section.endBit, section.colorConstant)
        refreshGapFieldSections()
    }

    fun addSectionLeft() {
        var newBit = 0
        if(sections.size > 0)
        {
            val currentLeft = sections[0]
            newBit = currentLeft.endBit + 1
        }

        sections.add(0, BitfieldSection(this, "new",  newBit, newBit, getNextColour()))
    }

    private var colorIndex: Int = 0
    fun getNextColour() : Color {
        return when ( colorIndex++ % 7) {
            0 -> Color.Red.copy(alpha = 0.05f)
            1 -> Color.Green.copy(alpha = 0.05f)
            2 -> Color.Cyan.copy(alpha = 0.05f)
            4 -> Color.Blue.copy(alpha = 0.05f)
            5 -> Color.Magenta.copy(alpha = 0.05f)
            6 -> Color.Yellow.copy(alpha = 0.05f)
            else -> Color.Yellow.copy(alpha = 0.05f)
        }
    }

    private fun refreshGapFieldSections() {
        if(sections.size > 1)
        {
            sections.forEach{ println(" section $it") }
            sections.removeIf { !it.enabled }

            val updateMap: MutableMap<Int, BitfieldSection> = mutableMapOf()

            var lastStart = sections.first().startBit
            sections.subList(1, sections.size).forEach { section ->
                val gap =  lastStart - section.endBit
                println("Start before ${section.name} [${section.startBit} : ${section.endBit}] is $lastStart gap is $gap")
                if(gap>1) {
                    println("Adding gap after ${section.name} Start ${lastStart-1} endBit ${section.endBit + 1}")
                updateMap[sections.indexOf(section)] = BitfieldSection(this,"", section.endBit + 1, lastStart - 1, getNextColour())
                }
                lastStart = section.startBit
            }

            val endGap = lastStart - 0
            if(endGap > 0) {
                println("Adding end gap  Start ${lastStart-1} endBit ${lastStart -1}")
                updateMap[sections.size] = BitfieldSection(this,"",  0,  lastStart - 1, getNextColour())
            }

            updateMap.forEach { (t, u) -> sections.add(t, u) }
            sections.forEach{ println(" section $it") }
        }
    }

    internal fun recalculateValue() {
        var newValue = 0UL
        for(s in sections) {
            newValue = newValue or ( (getULongValueOrZero(s.value.value, 10) shl s.startBit) and s.sectionMask())
        }
        value.value = newValue.toString()
    }

    override fun setValue(newValue: String, radix: Int, mask: ULong) {

        if(newValue.isNotEmpty()) {
            val valueWithoutMaskedBits = getULongValueOrZero(value.value, 10) and mask.inv()
            val longValue = getULongValueOrZero(newValue, radix) shl getFirstBitOf(mask)
            val valueWithNewValueBits = (valueWithoutMaskedBits or (longValue and mask))

            value.value = valueWithNewValueBits.toString()
        } else {
            value.value = (getULongValueOrZero(value.value, 10) and mask.inv()).toString().replace("0", "")
        }

        for (s in sections) {
            s.recalculateValue()
        }
    }

    override fun getValue(radix: Int, mask: ULong): String {
        return if(value.value.isNotEmpty()) {
            val firstBit = getFirstBitOf(mask)
            ((getULongValueOrZero(value.value, 10) and mask) shr firstBit).toString(radix)
        } else {
            value.value
        }
    }

    override fun getInvalidValueText(radix: Int): String {
        return if(isValueValid) "" else "> ${getMaxValueStr(10, this)}"
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

    fun expandFieldLeft(section: BitfieldSection) {
        val sectionIndex = sections.indexOf(section)
        val sectionToLeftIndex = sectionIndex - 1
        if (sectionToLeftIndex >= 0) {
            val sectionToLeft = sections[sectionToLeftIndex]

            sections[sectionToLeftIndex] = BitfieldSection(
                this,
                sectionToLeft.name,
                sectionToLeft.startBit + 1,
                sectionToLeft.endBit,
                sectionToLeft.color
            )

            // Purge empty field sections
            sections.removeIf { it.endBit - it.startBit < 0 }
        }
        sections[sections.indexOf(section)] =
            BitfieldSection(this, section.name, section.startBit , section.endBit + 1, section.color)
    }

    fun expandFieldRight(section: BitfieldSection) {

        val sectionIndex = sections.indexOf(section)
        val sectionToRightIndex = sectionIndex + 1
        if (sections.size > sectionToRightIndex) {
            val sectionToRight = sections[sectionToRightIndex]

            sections[sectionToRightIndex] = BitfieldSection(
                this,
                sectionToRight.name,
                sectionToRight.startBit,
                sectionToRight.endBit - 1,
                sectionToRight.color
            )
            sections[sectionIndex] =
                BitfieldSection(this, section.name, section.startBit - 1, section.endBit, section.color)

            // Purge empty field sections
            sections.removeIf { it.endBit - it.startBit < 0 }
        }
    }

    override val startBit: Int
        get() = 0
    override val endBit: Int
        get() = if(sections.isNotEmpty()) sections.maxOf { section -> section.endBit } else 0
    override val enabled: Boolean
        get() = true
    override val isValueValid: Boolean
        get() = getULongValueOrZero(this, ULong.MAX_VALUE) <= ((1UL shl (endBit - startBit) + 1) -1UL)
}