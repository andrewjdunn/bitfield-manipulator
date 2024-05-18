/**
 * 
 */
package com.hciware.keyboards;

import android.content.Context;
import android.inputmethodservice.KeyboardView;
import android.util.AttributeSet;

/**
 * @author andrew
 *
 */
public class BFKeyboardView extends KeyboardView {

	
	static final int KEYCODE_OPTIONS = -100;
	
	/**
	 * @param context
	 * @param attrs
	 */
	public BFKeyboardView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public BFKeyboardView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

}
