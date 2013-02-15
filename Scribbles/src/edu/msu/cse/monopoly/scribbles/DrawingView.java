package edu.msu.cse.monopoly.scribbles;

import java.util.ArrayList;



import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

public class DrawingView extends View {
    private static final String COLOR = "color";
    private static final String THICKNESS = "thickness";
    
	/**
	 * Paint for the line
	 */
	
	private Paint linePaint;
	/** 
	 * Current line thickness to draw in
	 */
	private int currentColor;
	
	/** 
	 * Current color to draw in
	 */
	private int currentThickness;
	
	/**
	 * Stores all of the lines that make up the drawing.
	 */
	private ArrayList<Line> lines;
	
	public DrawingView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}

	public DrawingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}

	public DrawingView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init();
	}
	
	private void init(){
		linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		currentColor = Color.BLACK;
		currentThickness = 1;
	}
	/** Sets the current color
	 * @return */
	public void setColor(int color){
		currentColor = color;
		invalidate();
	}
	
	public int getColor(){
		return currentColor;
	}
	
	/** Sets the current color
	 * @return */
	public void setThickness(int thickness){
		currentThickness = thickness;
		invalidate();
	}
	
	/** Line class used for drawing 
	 * The drawing will be composed of a bunch of lines. Start coordinates will be set
	 * when the user initially presses (touch 1). The end coordinates will be set when they move
	 * their finger. Also, upon moving a new line will be created, with the beginning coordinates
	 * matching the end of the last line. Rapid drawing of lines will allow for free form drawing.
	 * The last line's ending coordinates will be set when the user picks up their finger.*/
    private static class Line {
        /**
         * line color
         */
        public int color;
        
        /**
         * line thickness
         */
        public int thickness;
        
        /**
         * line start x
         */
        public float startX;
        /**
         * line start y
         */
        public float startY;
        /**
         * line end x
         */
        public float endX;
        /**
         * line end y
         */
        public float endY;
    }

    /**
     * Handle a draw event
     * @param canvas canvas to draw on.
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        
        /* Drawing a single line for testing */
        /*
        linePaint.setColor(currentColor);
        linePaint.setStrokeWidth(currentThickness);
        canvas.drawLine(0.0f, 0.0f, 100.0f, 100.0f, linePaint);*/
    }
    
    /**
     * Save the view state to a bundle
     * @param bundle bundle to save to
     */
    public void putToBundle(Bundle bundle) {
        bundle.putInt(COLOR, currentColor);
        bundle.putInt(THICKNESS, currentThickness);
    }
    
    /**
     * Get the view state from a bundle
     * @param bundle bundle to load from
     */
    public void getFromBundle(Bundle bundle) {         
        // Ensure the options are all set
        setColor(bundle.getInt(COLOR));
        setThickness(bundle.getInt(THICKNESS));  
    }
}
