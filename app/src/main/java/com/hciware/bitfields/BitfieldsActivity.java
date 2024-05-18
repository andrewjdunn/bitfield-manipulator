package com.hciware.bitfields;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class BitfieldsActivity extends Activity {
	
	private static final String TAG = "BitFields.BitfieldsActivity";
	
	private final static String Bundle_Name = "name";	
	private final static String Bundle_Field_Id = "fieldId";
	
	private final static int DeleteSectionDialog = 2;
	
    /** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		populateListFromDB();

		ListView view = (ListView) findViewById(R.id.fieldList);
		view.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int pos,
					long id) {
				BitField field = (BitField) parent.getItemAtPosition(pos);
				Intent viewIntent = new Intent(getApplicationContext(),
						com.hciware.bitfields.FieldViewActivity.class);
				long fieldId = field.getId();
				if (-1 != fieldId) {
					viewIntent.putExtra(FieldViewActivity.FieldId, fieldId);
					viewIntent.putExtra(FieldViewActivity.NewFieldFlag, false);
					startActivity(viewIntent);
				} else {
					Log.e(TAG, "Bad bit field");
				}
			}
		});
		
		view.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> parent, View arg1,
					int pos, long arg3) {
				BitField field = (BitField) parent.getItemAtPosition(pos);
				Bundle b = new Bundle();
				b.putLong(Bundle_Field_Id, field.getId());
				b.putString(Bundle_Name, field.getName());
				showDialog(DeleteSectionDialog, b);
				return false;
			}
		});
		
		String a = "a";
		String b = "b";
		String c=  "c";
		String d = "d";
		String e = "e";
		String f = "f";
		
		
				
		// String concat
		long s1 = System.nanoTime();
		
		StringBuilder sb2 = new StringBuilder(20);
		sb2.append(a+":").append(b+":").append(c+":").append(d+":").append(e+":").append(f);
		String str2 = sb2.toString();
		
		long s2 = System.nanoTime();		
		
		StringBuilder sb1 = new StringBuilder(20);
		sb1.append(a).append(":").append(b).append(":").append(c).append(":").append(d).append(":").append(e).append(":").append(f);
		String str1 = sb1.toString();		
		
		long end = System.nanoTime();
		System.out.println(" 1 "+(s2-s1)+" s2 "+(end-s2)+" : "+str1+" : "+str2);

	}
    
    @Override
    protected void onResume() {    
    	super.onResume();
    	populateListFromDB();
    }
    
	@Override
	protected Dialog onCreateDialog(int id,Bundle args) {
		
		Dialog rdialog;
		final long fieldId = args==null?-1:args.getLong(Bundle_Field_Id);
		switch (id) {
		case DeleteSectionDialog:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			String name = args.getString(Bundle_Name);

			builder.setMessage("Are you sure you want to delete " + name + "?")
					.setCancelable(true)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									deleteField(fieldId);
									removeDialog(DeleteSectionDialog);
								}
							})
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
									removeDialog(DeleteSectionDialog);
								}
							});
			rdialog = builder.create();
			break;
		default:
			rdialog = null;
		}
		return rdialog;

	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {	
		menu.add("Add Field");
		return true;
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		Intent viewIntent = new Intent(getApplicationContext(),
				com.hciware.bitfields.FieldViewActivity.class);
		long newFieldId = createEmptyBitField();
		if (-1 != newFieldId) {
			viewIntent.putExtra(FieldViewActivity.FieldId, newFieldId);
			viewIntent.putExtra(FieldViewActivity.NewFieldFlag, true);
			startActivity(viewIntent);
		} else {
			Log.e(TAG, "Could not create a new bit field");
		}
		return true;
	}
    
	void deleteField(final long fieldId) {
		BitFieldDBHelper dbhelper = new BitFieldDBHelper(this);
		dbhelper.deleteBitField(fieldId);
		populateListFromDB();
	}
    
    private void populateListFromDB() {
    	BitFieldDBHelper dbHelper = new BitFieldDBHelper(getApplicationContext());
    	assert(null!=dbHelper);
    	List<BitField> fieldList = dbHelper.getBitFields();    	
    	ListView view = (ListView) findViewById(R.id.fieldList);
    	view.setAdapter(new ArrayAdapter<BitField>(getApplicationContext(), R.layout.field_list_item, fieldList));    	
    	
    }
    
    
    private long createEmptyBitField() {
    	BitFieldDBHelper dbHelper = new BitFieldDBHelper(getApplicationContext());
    	assert(null!=dbHelper);
    	return dbHelper.createEmptyBitfield();
    }
}