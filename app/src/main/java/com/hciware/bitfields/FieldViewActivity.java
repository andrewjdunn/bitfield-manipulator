package com.hciware.bitfields;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.Dialog;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.os.Bundle;
import android.os.Message;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hciware.bitfields.BitField.Section;
import com.hciware.keyboards.BFKeyboardView;

public class FieldViewActivity extends Activity implements OnKeyboardActionListener, OnKeyListener, OnFocusChangeListener {
	
	/* TODO: For version 1.1
	 * Things to fix / add for 1.1
	 * -
	 * Improve icons / market image consistency	 * 
	 * Can't delete sections
	 */
	
	/* TODO: For version 1.2
	 * 
	 * Public fields - creating a library of down loadable fields 
	 * 
	 */
	
	private final static String Bundle_Section_Id = "sectionid";
	private final static String Bundle_Name = "name";
	private final static String Bundle_Start = "start";
	private final static String Bundle_End = "end";
	
	
	private final static int EditSectionDialog = 1;
	private final static int EditFieldNameDialog = 2;
	
	public final static String FieldId = "fieldid";
	public final static String NewFieldFlag = "newfield";
	private final static String Tag = "BitFields.FieldViewActivity";
	private boolean updateHack = false;
	
	/* Keyboard view for number inputs */
	private BFKeyboardView mInputView;
	private Keyboard mCurrentKeyboard = null;
	private Keyboard mHexKeyboard = null;
	private Keyboard mDecKeyboard = null;
	private Keyboard mBinKeyboard = null;
	
	private BitField mField = null;
	private final Map<EditText,BitField.Section> editorSections = 
			new HashMap<EditText,BitField.Section>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.field_view);
		long fieldId = getIntent().getLongExtra(FieldId, -1);
		boolean newFieldFlag = getIntent().getBooleanExtra(NewFieldFlag, false);		
		if(fieldId != -1) {
			mField = new BitFieldDBHelper(getApplicationContext()).getBitField(fieldId);
		}
		
		if(mField==null) {
			finish();
		}
		
		updateForm();
		
		mInputView = (BFKeyboardView) findViewById(R.id.bFKeyboardView);
		mHexKeyboard = new Keyboard(getApplicationContext(), R.xml.hexkeyboard);
		mDecKeyboard = new Keyboard(getApplicationContext(), R.xml.deckeyboard);
		mBinKeyboard = new Keyboard(getApplicationContext(), R.xml.binkeyboard);
		mCurrentKeyboard = mHexKeyboard;
		mInputView.setKeyboard(mCurrentKeyboard);
		mInputView.setOnKeyboardActionListener(this);		
		setupListeners();
		if(newFieldFlag && savedInstanceState == null) {
			showDialog(EditFieldNameDialog);			
		}
		
	}
	
	@Override
	protected void onResume() {	
		super.onResume();
		BitFieldDBHelper dbhelper = new BitFieldDBHelper(getApplicationContext());
		mField = dbhelper.getBitField(mField.getId());
		if(null!=mField){
			updateForm();
			afterTextChanged((EditText) findViewById(R.id.editBin));
		} else {
			finish();
		}
		onFocusChange(getCurrentFocus(), true);
	}
	
	@Override
	protected Dialog onCreateDialog(int id,Bundle args) {
		Dialog rdialog;
		final long sectionId = args==null?-1:args.getLong(Bundle_Section_Id);
		switch (id) {
		case EditSectionDialog:
			final Dialog dialog = new Dialog(this);
			dialog.setTitle("Bit field section");
			dialog.setContentView(R.layout.edit_section_dialog);
			if (args != null) {
				((EditText) dialog.findViewById(R.id.editSectionName))
						.setText(args.getString(Bundle_Name));
				((EditText) dialog.findViewById(R.id.startpos)).setText(Long
						.toString(args.getLong(Bundle_Start)));
				((EditText) dialog.findViewById(R.id.endpos)).setText(Long
						.toString(args.getLong(Bundle_End)));

			} else {
				((EditText) dialog.findViewById(R.id.editSectionName)).setText(null);
				((EditText) dialog.findViewById(R.id.startpos)).setText(null);
				((EditText) dialog.findViewById(R.id.endpos)).setText(null);
			}
			// Show the soft keyboard
			dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

			dialog.findViewById(R.id.cancelSection).setOnClickListener(
					new OnClickListener() {

						public void onClick(View v) {
							dialog.dismiss();
							removeDialog(EditSectionDialog);
						}
					});
			dialog.findViewById(R.id.saveSection).setOnClickListener(
					new OnClickListener() {
						public void onClick(View v) {
							String name = ((EditText) dialog
									.findViewById(R.id.editSectionName))
									.getText().toString();
							String start = ((EditText) dialog
									.findViewById(R.id.startpos)).getText()
									.toString();
							String end = ((EditText) dialog
									.findViewById(R.id.endpos)).getText()
									.toString();
							
							try
							{
								Integer startNumber = Integer.parseInt(start);
								Integer endNumber = Integer.parseInt(end);
								saveSection(sectionId, name,
										startNumber,
										endNumber);
								dialog.dismiss();
								removeDialog(EditSectionDialog);
							} catch(NumberFormatException ex) {
								Toast.makeText(getApplicationContext(), R.string.invalidsection, Toast.LENGTH_SHORT).show();
							}						
						}
					});
			rdialog = dialog;
			break;
		case EditFieldNameDialog:
			rdialog = new Dialog(this);
			rdialog.setTitle("Bit field's name");			
			rdialog.setContentView(R.layout.edit_field_name);
			((EditText)rdialog.findViewById(R.id.nameField)).setText(mField.getName());
			((EditText)rdialog.findViewById(R.id.nameField)).selectAll();
			
			// Show the soft keyboard
			rdialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
			
			rdialog.findViewById(R.id.cancel).setOnClickListener(new OnClickListener() {				
				public void onClick(View v) {
					removeDialog(EditFieldNameDialog);
				}
			});
			rdialog.findViewById(R.id.save_fieldname).setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
					BitFieldDBHelper helper = new BitFieldDBHelper(getApplicationContext());
					String name = ((EditText)v.getRootView().findViewById(R.id.nameField)).getText().toString();
					mField = new BitField(name, mField.getDescription(), mField.getId());					
					helper.setBitField(mField);
					mField = helper.getBitField(mField.getId());
					updateForm();
					removeDialog(EditFieldNameDialog);
				}
			});
			break;
		default:
			rdialog = null;
		}
		return rdialog;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.addSubMenu("Add Section");
		menu.addSubMenu("Edit Field name");		
		return true;
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		if(item.getTitle().equals("Add Section")) {
			showDialog(EditSectionDialog);
		} else if(item.getTitle().equals("Edit Field name")) {
			showDialog(EditFieldNameDialog);
		}
		return true;
	}
	
	private void updateHelpText() {
	
		List<Section> sections = mField.getSections();
		
		TextView view = (TextView) findViewById(R.id.viewhelptext);
		switch (sections.size()) {
		case 0:
			view.setVisibility(View.VISIBLE);
			view.setText(R.string.helpsections);
			break;
		case 1:
			view.setVisibility(View.VISIBLE);
			view.setText(R.string.helpsections2);
			break;
		default:
			view.setVisibility(View.INVISIBLE);
			break;
		}		
	}
	
	
	private void setupListeners() {
		
		final EditText decEdit = (EditText) findViewById(R.id.editDec);
		decEdit.setOnFocusChangeListener(this);
		decEdit.setOnKeyListener(this);
		decEdit.setInputType(InputType.TYPE_NULL);
		
		final EditText hexEdit = (EditText) findViewById(R.id.editHex);
		hexEdit.setOnFocusChangeListener(this);
		hexEdit.setOnKeyListener(this);
		hexEdit.setInputType(InputType.TYPE_NULL);
		
		final BinaryEditText editBin = (BinaryEditText) findViewById(R.id.editBin);		
		editBin.setInputType(InputType.TYPE_NULL);
		editBin.setOnKeyListener(this);
		editBin.setOnFocusChangeListener(this);
		
	}
	
	private void updateValues(final View changed) {
		
		EditText decEdit = (EditText) findViewById(R.id.editDec);
		if(changed!=decEdit) {
			decEdit.setText(longToString(mField.getValue(),10));			
		}		
		EditText hexEdit = (EditText) findViewById(R.id.editHex);
		if(changed!=hexEdit) {
			hexEdit.setText(longToString(mField.getValue(),16));
		}
		
		EditText binEdit = (EditText) findViewById(R.id.editBin);
		if(changed!=binEdit) {
			binEdit.setText(longToString(mField.getValue(),2));
		}
		
		ListView list = (ListView) findViewById(R.id.sectionList);
		for(int index = 0;index < list.getAdapter().getCount();index++) {
			View view = list.getChildAt(index);
			if(view!=null) {
				EditText sectionEdit = (EditText) view.findViewById(R.id.sectionValue);
				if(changed!=sectionEdit) {
					BitField.Section section = (Section) list.getAdapter().getItem(index);					
					sectionEdit.setText(longToString(section.getValue(),10));					
					sectionEdit.setError(null);					
				}
				
			}
		}	
	}
	
	private String longToString(final long value,final int base) {
		String displayValue = "";
		if(value>0){
			displayValue = Long.toString(value,base);
		}
		return displayValue;
	}
	
	private void updateForm() {
		final ListView sectionList = (ListView) findViewById(R.id.sectionList);		
		setTitle(getResources().getString(R.string.app_name)+" - "+mField.getName());
		List<BitField.Section> sections = mField.getSections();
		((BinaryEditText)findViewById(R.id.editBin)).setBitField(sections);
		
		sectionList.setAdapter(new ArrayAdapter<BitField.Section>(this	,
				R.layout.section_list_item,sections){
			
			
				@Override
				public View getView(int position, View convertView,
						ViewGroup parent) {
					if(convertView==null) {
						convertView = View.inflate(getApplicationContext(), R.layout.section_list_item, null);
						EditText sectionValue = (EditText) convertView.findViewById(R.id.sectionValue);
						sectionValue.setOnFocusChangeListener(FieldViewActivity.this);
						sectionValue.setOnKeyListener(FieldViewActivity.this);
						convertView.setOnLongClickListener(new OnLongClickListener() {
							
							public boolean onLongClick(View v) {								
								
								BitField.Section section = editorSections.get(v.findViewById(R.id.sectionValue));
								if(section!=null) {
									Bundle b = new Bundle();
									b.putLong(Bundle_Section_Id, section.getSectionId());
									b.putString(Bundle_Name, section.getName());
									b.putLong(Bundle_Start, section.getStart());
									b.putLong(Bundle_End, section.getEnd());									
									showDialog(EditSectionDialog,b);									
								}
								return true;
							}
						});
						convertView.setOnTouchListener(new OnTouchListener() {
							
							public boolean onTouch(View v, MotionEvent event) {
								if(event.getAction() == MotionEvent.ACTION_DOWN) {
									v.setBackgroundResource(android.R.drawable.list_selector_background);									
								} else {
									v.setBackgroundResource(0);									
								}
								
								return false;
							}
						});					
						
					}
					
					TextView sectionName = (TextView) convertView.findViewById(R.id.sectionName); 
					TextView sectionBitRange = (TextView) convertView.findViewById(R.id.sectionBitRange);
					TextView sectionValRange = (TextView) convertView.findViewById(R.id.sectionValRange);
					EditText sectionValue = (EditText) convertView.findViewById(R.id.sectionValue);
					sectionValue.setInputType(InputType.TYPE_NULL);
					sectionName.setText(getItem(position).getName());
					sectionBitRange.setText("["+getItem(position).getEnd()+":"+getItem(position).getStart()+"]");
					sectionValRange.setText(Long.toString(getItem(position).getMaxValue()));
					sectionValue.setText(longToString(getItem(position).getValue(),10));
					((TextView)convertView.findViewById(R.id.sectionName)).setTextColor(getItem(position).getColour());
					// Refresh the editor to section map
					editorSections.put(sectionValue,getItem(position));					
					return convertView;
				}});	
		updateHelpText();
	}
	
	public void afterTextChanged(final EditText editor) {

		if (updateHack) {
			return;
		}
		long value;

		if (editor.getId() == R.id.sectionValue) {
			BitField.Section section = editorSections.get(editor);
			if (section != null) {
				String strValue = editor.getText().toString();

				try {
					value = Long.parseLong(strValue);
				} catch (NumberFormatException ex) {
					value = 0;
				}
				
				if(section.getMaxValue() >= value) {
					section.setValue(value);
					editor.setError(null);
				} else {
					editor.setError("Out of range");
				}				
				
			} else {
				Log.e(Tag, "Could not find section for editor");
			}
		} else {
			String strValue = editor.getText().toString();

			int base = 2;
			if (editor.getId() == R.id.sectionValue
					|| editor.getId() == R.id.editDec) {
				base = 10;
			} else if (editor.getId() == R.id.editHex) {
				base = 16;
			}

			try {
				value = Long.parseLong(strValue, base);
			} catch (NumberFormatException ex) {
				value = 0;
			}
			mField.setValue(value);
		}
		updateHack = true;
		updateValues(editor);
		updateHack = false;
	}
	
	
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		boolean consumed = false;
		if(event.getAction() == KeyEvent.ACTION_DOWN){
			consumed = true;
			if(event.getKeyCode() == KeyEvent.KEYCODE_ENTER){
				onKey(13,new int[]{keyCode});
			} else if (event.getKeyCode() == KeyEvent.KEYCODE_DEL){
				onKey(8,new int[]{keyCode});
			} else if(event.getUnicodeChar()!=0)	{
				onKey(event.getUnicodeChar(),new int[]{keyCode});				
			} else	{
				consumed = false;
			}
		}
		return consumed;
	}


	public void onKey(final int primaryCode, int[] keyCodes) {		
		View view = getCurrentFocus();
		EditText editView = null;
		String text = Character.toString((char) primaryCode);
		if(view instanceof EditText) {
			editView = (EditText) view;			
		}		
		
		if(editView!=null) {
			
			// Get the cursor position
			int start = editView.getSelectionStart();
			int end = editView.getSelectionEnd();
			
			
			if(primaryCode>=48 && primaryCode <=57  && mCurrentKeyboard != mBinKeyboard) {
				editView.getText().insert(start, text);				
				afterTextChanged(editView);
			}
			
			if(primaryCode>=48 && primaryCode <=49  && mCurrentKeyboard == mBinKeyboard) {				
				editView.getText().insert(start, text);
				afterTextChanged(editView);
			}
			
			if( ( (primaryCode>=65 && primaryCode <=70) ||
					primaryCode>=97 && primaryCode<=102)&&
					mCurrentKeyboard == mHexKeyboard) {				
				editView.getText().insert(start, text);
				afterTextChanged(editView);
			}
			
			
			if(primaryCode == 8) {
				int len = editView.getText().length();
				
				if(start==end) {
					if(start > 0 && start <= len) {						
						editView.getText().delete(start-1, end);
						afterTextChanged(editView);
					}
				}
				
				
			} else if (primaryCode == 21) {
				editView.setTextKeepState("");
				afterTextChanged(editView);
			} else if (primaryCode == 13) {
				View field = FieldViewActivity.this.getCurrentFocus();
				int id = field.getId();
				if(id == R.id.editBin) {
					findViewById(R.id.editHex).requestFocus();
				} else if (id == R.id.editHex) {
					findViewById(R.id.editDec).requestFocus();
				} else if (id == R.id.editDec) {
					ListView list = (ListView) FieldViewActivity.this.findViewById(R.id.sectionList);
					View next = list.getChildAt(0);
					if(next!=null){
						next.requestFocus();
					}
				} else {
					ListView list = (ListView) FieldViewActivity.this.findViewById(R.id.sectionList);
					int pos = list.getPositionForView(field);
					list.smoothScrollToPosition(pos+1);
					View next = field.focusSearch(View.FOCUS_DOWN);
					if(next!=null) {
						next.requestFocus();
					} else {
						list.smoothScrollToPosition(0);
						FieldViewActivity.this.findViewById(R.id.editBin).requestFocus();
					}
				}
			}
		}
		
	}

	public void onPress(int primaryCode) {
	}

	public void onRelease(int primaryCode) {
	}

	public void onText(CharSequence text) {
		
	}

	public void swipeDown() {
	
	}

	public void swipeLeft() {
		
	}

	public void swipeRight() {
		
	}

	public void swipeUp() {
		
	}

	public void onFocusChange(View v, boolean hasFocus) {
		if(hasFocus) {
			if(v.getId() == R.id.editBin) {
				mCurrentKeyboard = mBinKeyboard;
				mInputView.setKeyboard(mCurrentKeyboard);
			} else if(v.getId() == R.id.editHex) {
				mCurrentKeyboard = mHexKeyboard;
				mInputView.setKeyboard(mCurrentKeyboard);
			} else {
				mCurrentKeyboard = mDecKeyboard;
				mInputView.setKeyboard(mCurrentKeyboard);
			}
		}	
	}
	
	void saveSection(final long sectionId,final String name,final int start,final int end) {
		BitFieldDBHelper dbhelper = new BitFieldDBHelper(this);
		BitField.Section section = mField.createSection(sectionId , name, start, end);
		long newSectionId;
		if(-1==sectionId) {
			if(-1==(newSectionId = dbhelper.addFieldSection(mField.getId(),section ))) {
				Toast.makeText(this, "Failed to add section", Toast.LENGTH_LONG).show();				
			} else {
				//TODO: Smell - the maintance of the sections and Ids has gone wrong, maybe creating a section should add it to the list?
				mField.createSection(newSectionId , name, start, end);
			}
				
			
		} else {
			dbhelper.updateFieldSection(mField.getId(),section );
		}		
		updateForm();
	}

	
}
