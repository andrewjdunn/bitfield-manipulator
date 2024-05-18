/**
 * 
 */
package com.hciware.bitfields;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.graphics.Color;

/**
 * @author andrew
 *
 */
public class BitField {
	
	private final String name;
	private final String description;
	private final long id;
	private final List<Section> sections = new ArrayList<Section>();
	
	private final static int MaxColours = 6;
	//private final static int mColours[] = {Color.rgb(0xAA, 0x0, 0xCC),Color.rgb(0x33, 0xDD, 0x11),Color.rgb(0x00, 0xEE, 0xFF)};
	private final static int mColours[] = {
		Color.rgb(0xF5,0xBD,0xB5),
		Color.rgb(0xE3,0xB5,0xF5),
		Color.rgb(0xB5,0xC9,0xF5),
		Color.rgb(0xB5,0xF5,0xE2),
		Color.rgb(0xF5,0xED,0xB5),
		Color.rgb(0xFF,0xD4,0xF5)	
	};
	
	private long value;
	
	BitField(final String name,final String description,final long id) {
		this.name = name;
		this.description = description;
		this.id = id;
	}
	
	@Override
	public String toString() {
		return getName();
	}
		
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}


	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}


	/**
	 * @return the sections
	 */
	public List<Section> getSections() {
		return Collections.unmodifiableList(sections);
	}
	
	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	
	public Section createSection(final long sectionId, final String name,final int start,final int end) {
		Section ns =  new Section(sectionId, name, start, end);
		if(sectionId!=-1){
			//TODO: Another section /id smell
			int old = sections.indexOf(ns);
			if(old!=-1){
				sections.set(old, ns);
			} else {
				sections.add(ns);
			}
			
		} 
			
		return ns;
	}
	
	/**
	 * Set the full value of this bitfield
	 * @param value
	 */
	public boolean setValue(final long value ) {
		boolean changed = value != this.value;
		this.value = value;
		return changed;
	}
	
	/**
	 * 
	 * @return Value
	 */
	public long getValue() {
		return value;
	}



	public class Section {
		
		private final long sectionId;
		private final String name;
		private final int start;
		private final int end;
		
		Section(final long sectionId, final String name,final int start,final int end) {
			this.sectionId = sectionId;
			this.name = name;
			this.start = start;
			this.end = end;
		}
		
		@Override
		public String toString() {
			return name+" ["+start+":"+end+"]";
		}
		
		@Override
		public boolean equals(Object o) {		
			return this.sectionId == ((Section)o).getSectionId();
		}
		
		@Override
		public int hashCode() {
		
			return (int) getSectionId();
		}
		
		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}

		/**
		 * @return the start
		 */
		public int getStart() {
			return start;
		}

		/**
		 * @return the end
		 */
		public int getEnd() {
			return end;
		}

		/**
		 * @return the sectionId
		 */
		public long getSectionId() {
			return sectionId;
		}
		
		public long getMaxValue() {
			return  (( 2 << (end-start) ) - 1);			
		}
		
		/**
		 * Set the value of this section in it's bit field
		 * @param value
		 * @return true if the fields value changed
		 */
		public boolean setValue(final long value) {			
			long bitmask = (long)(( 2 << (end-start) ) - 1)<<start;		
			long temp = value << start;
			long forComp = BitField.this.value;
			BitField.this.value = BitField.this.value &~ bitmask;
			BitField.this.value |= (temp&bitmask);
			return forComp != BitField.this.value;
		}

		/**
		 * 
		 * @return This sections value
		 */
		public long getValue() {		
			// Shift the top bits off
			long ourValue = BitField.this.value << (63 - end );
			// And shift to zero
			return ourValue >>> (64-((end-start)+1));
		}

		public int getColour() {			
			return mColours[ (int) ( Math.abs(sections.indexOf(this)) % MaxColours ) ];
		}
	}

}
