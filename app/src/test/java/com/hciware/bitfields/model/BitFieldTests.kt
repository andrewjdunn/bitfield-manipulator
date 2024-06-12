package com.hciware.bitfields.model

import junit.framework.TestCase.assertEquals
import org.junit.Test

class BitFieldTests {

    @Test
    fun bitfield() {

        // Setup
        val bitfield = BitField(
            BitfieldDescription(0, "Test Bitfield"))

        // Test
        assertEquals(0, bitfield.value.value)
    }

    @Test
    fun bitfield_setBitOne_UsingSectionOne() {

        // Setup
        val bitfield = BitField(
            BitfieldDescription(0, "Test Bitfield"))
        bitfield.addBitfieldSection("Bit 1",0,0)

        bitfield.sections[0].setValue(1)

        // Test
        assertEquals(1, bitfield.value.value)
    }

    @Test
    fun bitfield_setBitOne_UsingOverall() {

        // Setup
        val bitfield = BitField(
            BitfieldDescription(0, "Test Bitfield"))
        bitfield.addBitfieldSection("Bit 1",0,0)

        bitfield.setValue(1)

        // Test
        assertEquals(1, bitfield.value.value)
        assertEquals(1, bitfield.sections[0].value.value)
    }

    @Test
    fun bitfield_setValue_MultipleSections() {

        // Setup
        val bitfield = BitField(
            BitfieldDescription(0, "Test Bitfield"))
        bitfield.addBitfieldSection("Nibble 1",1,4)
        bitfield.addBitfieldSection("Bit 1",0,0)

        bitfield.setValue(0b1010_1)

        // Test
        assertEquals(21, bitfield.value.value)
        assertEquals(10, bitfield.sections[0].value.value)
        assertEquals(1, bitfield.sections[1].value.value)
    }

    @Test
    fun bitfield_getBits() {

        // Setup
        val bitField = BitField(BitfieldDescription(0, "Two Fields"))
        bitField.addBitfieldSection("Nibble 2", 4, 7)
        bitField.addBitfieldSection("Nibble 1", 0, 3)

        // Test
        val startField = bitField.startBit
        val endField = bitField.endBit
        val startFields = bitField.sections[1].startBit
        val endFields = bitField.sections[0].endBit

        // Verify
        assertEquals(startField, startFields)
        assertEquals(endField, endFields)
    }

    @Test
    fun bitfield_up_result_data() {
        // Setup
        val bitfield = BitFieldsViewModel().bitfields[1]

        // Test & Verify
        assertEquals(0, bitfield.value.value)
        bitfield.sections[4].up(0b1)
        assertEquals(1, bitfield.value.value)
        assertEquals(1, bitfield.sections[4].value.value)
        assertEquals(0, bitfield.sections[3].value.value)

        bitfield.sections[3].up(0b1)
        assertEquals(33, bitfield.value.value)
        assertEquals(1, bitfield.sections[4].value.value)
        assertEquals(1, bitfield.sections[3].value.value)
    }

    @Test
    fun bitfield_up_binary() {
        // Setup
        val bitfield = BitField(BitfieldDescription(0, "Some Fields"))

        bitfield.addBitfieldSection("Section 4", 6,7)
        bitfield.addBitfieldSection("Section 3", 4,5)
        bitfield.addBitfieldSection("Section 2", 2,3)
        bitfield.addBitfieldSection("Section 1", 0,1)

        // Test & Verify
        assertEquals(0, bitfield.value.value)
        // Increase the first bit
        bitfield.sections[3].up(0b1)
        assertEquals(1, bitfield.sections[3].value.value)
        assertEquals(1, bitfield.value.value)

        // Increase the first bit again
        bitfield.sections[3].up(0b1)
        assertEquals(1, bitfield.sections[3].value.value)
        assertEquals(1, bitfield.value.value)

        // Increase the second bit
        bitfield.sections[3].up(0b10)
        assertEquals(3, bitfield.sections[3].value.value)
        assertEquals(3, bitfield.value.value)

        // Increase the second bit again
        bitfield.sections[3].up(0b10)
        assertEquals(3, bitfield.sections[3].value.value)
        assertEquals(3, bitfield.value.value)

        // Now the first bit in the second field (third bit (bit 2))
        bitfield.sections[2].up(0b1)
        assertEquals(1, bitfield.sections[2].value.value)
        assertEquals(7, bitfield.value.value)

        // And Again
        bitfield.sections[2].up(0b1)
        assertEquals(1, bitfield.sections[2].value.value)
        assertEquals(7, bitfield.value.value)

        // Increase the highest bit
        bitfield.sections[0].up(0b10)
        assertEquals(2, bitfield.sections[0].value.value)
        assertEquals(135, bitfield.value.value)

        // Increase the second highest bit
        bitfield.sections[0].up(0b1)
        assertEquals(3, bitfield.sections[0].value.value)
        assertEquals(199, bitfield.value.value)

    // What does contempory kotlin unit testing offer in terms of data driven testing?
    // TODO:I think I could supply a bunch of commands and expected values to check..

    }

}
