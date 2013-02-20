package edu.msu.cse.monopoly.scribbles;

import java.io.Serializable;
import java.util.ArrayList;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class DrawingView extends View {
    private static final String COLOR = "color";
    private static final String THICKNESS = "thickness";
    private static final String MOVEFLAG = "moveFlag";
    private static final String XLOCATION = "xLocation";
    private static final String YLOCATION = "yLocation";
    private static final String SCALE = "scale";
    private static final String ROTATION = "rotation";
    private static final String LINES = "lines";
    
    /**
     * The line currently being drawn
     */
    private Line currentLine = null;
    
    /**
     * Flag indicated if we have touched
     */
    private boolean isTouched = false;
    
	/**
	 * Paint for the line
	 */
	private static Paint linePaint;
	
	/** 
	 * Flag to indicate if we are moving the picture or drawing it.
	 * Will be set to true when guessing with no way to change it
	 */
	private boolean moveFlag = false;
	
	/** 
	 * Current line color to draw in
	 */
	private int currentColor;
	
	/** 
	 * Current line thickness to draw in
	 */
	private int currentThickness;
	
	/** 
	 * current x location of drawing
	 */
	private int drawingX;
	
	/** 
	 * current y location of drawing
	 */
	private int drawingY;
	
	/** 
	 * current scale of drawing
	 */
	private float drawingScale;
	
	/** 
	 * current angle of rotation of drawing
	 */
	private float drawingAngle;
    
    /**
     * First touch status
     */
    private Touch touch1 = new Touch();
    
    /**
     * Second touch status
     */
    private Touch touch2 = new Touch();
	
	/**
	 * Stores all of the lines that make up the drawing.
	 */
	private ArrayList<Line> lines = new ArrayList<Line>();
    
	/**
	 * Paint used to draw the circle (creates a nice, circular
	 * line instead of a blocky one)
	 */
	private static Paint circlePaint = new Paint();
	
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
	
	/**
	 * Initializes the view
	 */
	private void init(){
		linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		currentColor = Color.BLACK;
		currentThickness = 1;
		circlePaint.setStyle(Paint.Style.FILL);
	}
	
	/** Sets the drawing's x location
	 * @return */
	public void setDrawingX(int x){
		drawingX = x;
		invalidate();
	}
	
	public int getDrawingX(){
		return drawingX;
	}
	
	/** Sets the drawing's y location
	 * @return */
	public void setDrawingY(int y){
		drawingY = y;
		invalidate();
	}
	
	public int getDrawingY(){
		return drawingY;
	}
	
	/** Sets the drawing's scale
	 * @return */
	public void setDrawingScale(float s){
		drawingScale = s;
		invalidate();
	}
	
	public float getDrawingScale(){
		return drawingScale;
	}
	
	/** Sets the drawing's rotation
	 * @return */
	public void setDrawingAngle(float a){
		drawingAngle = a;
		invalidate();
	}
	
	public float getDrawingAngle(){
		return drawingAngle;
	}
	
	/** Sets the current color
	 * @return */
	public void setColor(int color){
		currentColor = color;
		invalidate();
	}
	
	/**
	 * Returns the current color
	 * @return the current color
	 */
	public int getColor(){
		return currentColor;
	}
	
	/**
	 * Sets the flag indicating if we are manipulating with multitouch
	 * @param b The flag
	 */
	public void setMoveFlag(boolean b){
		moveFlag = b;
	}
	
	/**
	 * return the flag indicating if we are manipulating with multitouch
	 * @return the flag indicating if we are manipulating with multitouch
	 */
	public boolean getMoveFlag(){
		return moveFlag;
	}
	
	/** Sets the current thickness
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
    private static class Line implements Serializable{
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
         * line color
         */
        public int color;
        
        /**
         * line thickness
         */
        public int thickness;
        
        /**
         * beginning x position
         */
        private float startX;
        
        /**
         * beginning y position
         */
        private float startY;
        
        /**
         * final x position
         */
        private float endX;
        
        /**
         * final y position
         */
        private float endY;
        
        /**
         * Constructor
         * @param x X pos
         * @param y Y pos
         */
        public Line(float x, float y) {
			startX = x;
			startY = y;
		}

		/**
         * Draws a line. Draws a circle at the end point to make round.
         * @param canvas The canvas
         */
        public void draw(Canvas canvas){
        		linePaint.setColor(color);
        		linePaint.setStrokeWidth(thickness);
        		circlePaint.setColor(color);
        		canvas.drawLine(startX, startY, endX, endY, linePaint);
        		
        		canvas.drawCircle(endX, endY, thickness/2, circlePaint);
        	}
        
        /**
         * Set the color of the line
         * @param c The color
         */
        public void setColor(int c)
        {
        	color = c;
        }
        
        /**
         * Set the thickness of the line
         * @param t Thickness
         */
        public void setThickness(int t)
        {
        	thickness = t;
        }
    }
    
    /**
     * Handle a draw event
     * @param canvas canvas to draw on.
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        
    	for(Line line:lines){
    		line.draw(canvas);
    	}
    	
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
        bundle.putInt(XLOCATION, drawingX);
        bundle.putInt(YLOCATION, drawingY);
        bundle.putFloat(SCALE, drawingScale);
        bundle.putFloat(ROTATION, drawingAngle);
        bundle.putBoolean(MOVEFLAG, moveFlag);
        bundle.putSerializable(LINES, lines);
    }
    
    /**
     * Get the view state from a bundle
     * @param bundle bundle to load from
     */
    public void getFromBundle(Bundle bundle) {         
        // Ensure the options are all set
        setColor(bundle.getInt(COLOR));
        setThickness(bundle.getInt(THICKNESS));
        setDrawingX(bundle.getInt(XLOCATION));
        setDrawingY(bundle.getInt(YLOCATION));
        setDrawingScale(bundle.getFloat(SCALE));
        setDrawingAngle(bundle.getFloat(ROTATION));
        setMoveFlag(bundle.getBoolean(MOVEFLAG));
    }
    
    
    
    /**
     * Local class to handle the touch status for one touch.
     * We will have one object of this type for each of the 
     * two possible touches.
     */
    private class Touch {
        /**
         * Touch id
         */
        public int id = -1;
        
        /**
         * Current x location
         */
        public float x = 0;
        
        /**
         * Current y location
         */
        public float y = 0;
        
        /**
         * Previous x location
         */
        public float lastX = 0;
        
        /**
         * Previous y location
         */
        public float lastY = 0;
        
        /**
         * Change in x value from previous
         */
        public float dX = 0;
        
        /**
         * Change in y value from previous
         */
        public float dY = 0;
        
        /**
         * Copy the current values to the previous values
         */
        public void copyToLast() {
            lastX = x;
            lastY = y;
        }
        
        /**
         * Compute the values of dX and dY
         */
        public void computeDeltas() {
            dX = x - lastX;
            dY = y - lastY;
        }
    }
    
    /**
     * Handles a touch event
     * @param e Motion event
     */
    @Override 
    public boolean onTouchEvent(MotionEvent e) { 
        if(!moveFlag){ 
            
            switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
            	isTouched = true;
	            currentLine = new Line(e.getX(), e.getY());
	            
	            currentLine.setColor(currentColor);
	            currentLine.setThickness(currentThickness);
                break;
            case MotionEvent.ACTION_MOVE:
                if(isTouched){
                	currentLine.endX = e.getX();
                	currentLine.endY = e.getY();
                	lines.add(currentLine);
                	
    	            currentLine = new Line(e.getX(), e.getY());
    	            
                	currentLine.setColor(currentColor);
                	currentLine.setThickness(currentThickness);
                }
                break;
            case MotionEvent.ACTION_UP:
                if(isTouched)
                	currentLine.endX = e.getX();
                	currentLine.endY = e.getY();
                isTouched = false;
                lines.add(currentLine);
                currentLine = null;
                break;
            }
            invalidate();
            return true; 
        } 
        return false; 
    }
}