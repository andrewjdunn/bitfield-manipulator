package com.hciware.bitfields.model

data class BitfieldDescription (val id: Long, val name: String)
data class BitfieldSection (val id: Long, val name: String, val start: Int, val end: Int)
// TODO: is BitField one word (Bitfield?)
class BitField(val description: BitfieldDescription, val sections: List<BitfieldSection>)