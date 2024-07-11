package com.hciware.bitfields.model

import androidx.compose.runtime.mutableStateOf
import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe

lateinit var bitfield: BitField

data class BitFieldInput(
    val target: Int,
    val setValue: String,
    val mask: ULong,
    val expected: List<Long>
)

enum class InputMethod(val radix: Int) { Dec(10), Hex(16), Bin(2) }

class BitFieldTests2 : FunSpec({
    context("Result") {
        beforeTest {
            bitfield = BitField(
            BitfieldDescription(2, mutableStateOf("Result"))
        )
            .addBitfieldSection("Stored", 12, 12)
            .addBitfieldSection("Success", 11, 11)
            // Gap
            .addBitfieldSection("Label 1", 5, 9)
            .addBitfieldSection("Box Color", 0, 4)
        }

        withData(
            mapOf(
                "Set Zero" to BitFieldInput(0, "0", ULong.MAX_VALUE, listOf(0, 0, 0, 0)),
                "Set One" to BitFieldInput(0, "1", ULong.MAX_VALUE, listOf(1, 0, 0, 0)),
                "Section 1 bit 1" to BitFieldInput(5, "1", 0b1u, listOf(32, 0, 0, 0, 1)),
                "Section 1 bit 2" to BitFieldInput(5, "1", 0b10u, listOf(64, 0, 0, 0, 2)),
                "Set Bit 2" to BitFieldInput(0, "1", 0b10u, listOf(2, 0, 0, 0, 2)),
                "Set zero" to BitFieldInput(0, "0", ULong.MAX_VALUE, listOf(0, 0, 0, 0, 0)),
                "Overfill Field" to BitFieldInput(6, "63", ULong.MAX_VALUE, listOf(31, 0, 0, 0, 0, 63))
            )
        ) { input ->
            withData(InputMethod.entries) { method ->
                testSettingValues(bitfield, method, input)
            }
        }
    }
})

fun testSettingValues(bitfield: BitField, method: InputMethod, input: BitFieldInput) {
    if(input.target == 0) {
        bitfield.setValue(input.setValue, 10, input.mask)
    } else {
        bitfield.sections[input.target - 1].setValue(input.setValue, 10, input.mask)
    }

    bitfield.getValue(method.radix) shouldBe input.expected[0].toString(method.radix)

    for (section in 1..input.expected.size - 2) {
        bitfield.sections[section - 1].getValue(method.radix) shouldBe input.expected[section].toString()
    }
}
