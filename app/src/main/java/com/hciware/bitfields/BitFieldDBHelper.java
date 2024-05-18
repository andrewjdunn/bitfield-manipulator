/**
 * 
 */
package com.hciware.bitfields;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.hciware.bitfields.BitField.Section;

/**
 * 
 */
public class BitFieldDBHelper extends SQLiteOpenHelper {

	private final static int DatabaseVersion = 1;
	private final static String DatabaseName = "bitfields";

	private final static String Table_BitFields = "bitfields";
	private final static String Table_BitFieldSections = "bitfield_sections";

	private final static String Field_Name = "name";
	private final static String Field_Description = "description";
	private final static String CreateBitfieldDB = "create table "
			+ Table_BitFields + " (" + Field_Name + " text,"
			+ Field_Description + " text) ";

	private final static String Field_Id = "id";
	private final static String Section_Name = "name";
	private final static String Section_StartBit = "start";
	private final static String Section_EndBit = "end";

	private final static String CreateBitFieldSections = "create table "
			+ Table_BitFieldSections + " (" + Field_Id + " int," + Section_Name
			+ " text," + Section_StartBit + " int," + Section_EndBit + " int) ";

	public BitFieldDBHelper(Context context) {
		super(context, DatabaseName, null, DatabaseVersion);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CreateBitfieldDB);
		db.execSQL(CreateBitFieldSections);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Not yet

	}

	public long createEmptyBitfield() {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(Field_Name, "New Bitfield");
		long id = db.insert(Table_BitFields, "", values);
		db.close();
		return id;
	}

	public void setBitField(final BitField updatedBitField) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(Field_Name, updatedBitField.getName());
		values.put(Field_Description, updatedBitField.getName());
		db.update(Table_BitFields, values, "rowid = ?",
				new String[] { String.valueOf(updatedBitField.getId()) });
		db.close();
	}

	public BitField getBitField(final long id) {
		List<BitField> fields = getBitFields(id);
		BitField retVal = null;
		if (fields.size() > 0) {
			retVal = fields.get(0);
			getBitfieldSections(retVal);
		}
		return retVal;
	}

	public List<BitField> getBitFields() {
		return getBitFields(-1);
	}

	public List<BitField> getBitFields(final long id) {
		List<BitField> fieldList = new ArrayList<BitField>();
		SQLiteDatabase db = getReadableDatabase();
		String selection = null;
		if (-1 != id) {
			selection = "rowid =" + id;
		}
		Cursor cursor = db.query(Table_BitFields, new String[] { "rowid",
				Field_Name, Field_Description }, selection, null, null, null,
				null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			fieldList.add(new BitField(cursor.getString(cursor
					.getColumnIndex(Field_Name)), cursor.getString(cursor
					.getColumnIndex(Field_Description)), cursor.getLong(cursor
					.getColumnIndex("rowid"))));
			cursor.moveToNext();
		}
		// TODO: Should the sections in the fields be populated here?

		cursor.close();
		db.close();
		return fieldList;
	}

	public boolean deleteBitField(final long id) {
		boolean deleted = false;
		SQLiteDatabase db = getWritableDatabase();
		if (0 != db.delete(Table_BitFields, "rowid=?",
				new String[] { Long.toString(id) })) {
			db.delete(Table_BitFieldSections, Field_Id + "=?",
					new String[] { Long.toString(id) });
			deleted = true;
		}
		db.close();
		return deleted;
	}

	public boolean deleteSection(long id, long sectionId) {
		boolean deleted = false;
		SQLiteDatabase db = getWritableDatabase();
		if (0 != db.delete(Table_BitFieldSections, "rowid=? and " + Field_Id
				+ "=?",
				new String[] { Long.toString(sectionId), Long.toString(id) })) {
			deleted = true;
		}
		db.close();
		return deleted;
	}

	private List<BitField.Section> getBitfieldSections(final BitField field) {

		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db
				.query(Table_BitFieldSections, new String[] { "rowid",
						Field_Id, Section_Name, Section_StartBit,
						Section_EndBit }, Field_Id + " = ?",
						new String[] { Long.toString(field.getId()) }, null,
						null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			field.createSection(cursor.getLong(cursor.getColumnIndex("rowid")),
					cursor.getString(cursor.getColumnIndex(Section_Name)),
					cursor.getInt(cursor.getColumnIndex(Section_StartBit)),
					cursor.getInt(cursor.getColumnIndex(Section_EndBit)));
			cursor.moveToNext();
		}
		cursor.close();
		db.close();
		return field.getSections();
	}

	public long addFieldSection(final long id, final Section section) {
		long newId;
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(Field_Id, id);
		values.put(Section_Name, section.getName());
		values.put(Section_StartBit, section.getStart());
		values.put(Section_EndBit, section.getEnd());
		newId = db.insert(Table_BitFieldSections, null, values);
		db.close();
		return newId;
	}

	public void updateFieldSection(long id, Section section) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(Section_Name, section.getName());
		values.put(Section_StartBit, section.getStart());
		values.put(Section_EndBit, section.getEnd());
		db.update(Table_BitFieldSections, values, "rowid=? and " + Field_Id
				+ "=?", new String[] { Long.toString(section.getSectionId()),
				Long.toString(id) });

		db.close();

	}

}
