package com.hciware.bitfields;

import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.widget.EditText;

public class BinaryEditText extends EditText {
	
	private List<BitField.Section> mSections = null;
	final private Paint linePaint;
	
	public BinaryEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		linePaint = new Paint();
		linePaint.setColor(Color.BLUE);
		linePaint.setStyle(Style.STROKE);
	}
	
	public void setBitField(final List<BitField.Section> sections) {
		mSections = sections;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		
		Paint p = getPaint();
		String str = getText().toString();		
		if(str.length()>0){
			float widths[] = new float[str.length()];
			float awidths[] = new float[str.length()];
			p.getTextWidths(str, 0,str.length(), widths);		
			int totalWidth = 0;
			for(int index = widths.length;index > 0;index--) {
				totalWidth+=widths[index-1];
				awidths[widths.length-index] = totalWidth;
			}
			
			int currentRightEdge = (getWidth()-getPaddingRight()) + getScrollX() + 5;
			int currentLeftEdge = (getPaddingLeft()) + getScrollX() - 5;
			
			if(mSections!=null) {
				for( BitField.Section section : mSections) {				
					
					int startPos = section.getStart() - 1;
					int endPos = section.getEnd();
					
					if(startPos < endPos && awidths.length > startPos) {
						
						float xStartOffset = 0;
						float xEndOffset = 0;
						
						if(startPos>=0){
							xStartOffset = awidths[startPos];
						}
						
						if(endPos>=0){
							xEndOffset = awidths[(endPos < awidths.length) ? endPos : awidths.length-1];
						}
						
						float rightedge = (getPaddingLeft() + totalWidth) - xStartOffset;
						float leftedge = (getPaddingLeft()+totalWidth)-xEndOffset;
						
						if(rightedge > currentRightEdge){
							rightedge = currentRightEdge;
						}
						boolean drawLeftLine = true;
						if(leftedge < currentLeftEdge){
							leftedge = currentLeftEdge;
							drawLeftLine = false;
						}
						
						Paint secPaint = new Paint();
						secPaint.setStyle(Style.FILL_AND_STROKE);
						secPaint.setColor(section.getColour());												
						
						canvas.drawRect(leftedge,getPaddingTop()+5,
								rightedge, getHeight() - (getPaddingBottom()+5),secPaint);
						
						if(drawLeftLine) {
							canvas.drawLine(leftedge, getPaddingTop()+4, leftedge+20, getPaddingTop()+4, linePaint);
							canvas.drawLine(leftedge, getPaddingTop()+4, leftedge, getPaddingTop()+25, linePaint);
							canvas.drawLine(leftedge, getHeight() - (getPaddingBottom()+5), leftedge+20, getHeight() - (getPaddingBottom()+5), linePaint);
							canvas.drawLine(leftedge, getHeight() - (getPaddingBottom()+25), leftedge, getHeight() - (getPaddingBottom()+5), linePaint);
						}
						
					}				
				}
			}
		}
		super.onDraw(canvas);
	}

}
