package edu.msu.cse.monopoly.scribbles;

import java.util.ArrayList;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
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
    
    
    private Line currentLine = null;
    
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
	
	public int getColor(){
		return currentColor;
	}
	
	public void setMoveFlag(boolean b){
		moveFlag = b;
	}
	
	public boolean getMoveFlag(){
		return moveFlag;
	}
	
	/** Sets the current color
	 * @return */
	public void setThickness(int thickness){
		currentThickness = thickness;
		invalidate();
	}
	
    private static class Point{
    	private float x = 0.0f;
    	private float y = 0.0f;
    	
		public float getX() {
			return x;
		}
		public void setX(float x) {
			this.x = x;
		}
		public float getY() {
			return y;
		}
		public void setY(float y) {
			this.y = y;
		}
    }
	
	/** Line class used for drawing 
	 * The drawing will be composed of a bunch of lines. Start coordinates will be set
	 * when the user initially presses (touch 1). The end coordinates will be set when they move
	 * their finger. Also, upon moving a new line will be created, with the beginning coordinates
	 * matching the end of the last line. Rapid drawing of lines will allow for free form drawing.
	 * The last line's ending coordinates will be set when the user picks up their finger.*/
    private static class Line implements Parcelable{
        /**
         * line color
         */
        public int color;
        
        /**
         * line thickness
         */
        public int thickness;
        
        private ArrayList<Point> points = new ArrayList <Point>();
        
        public void addPoint(Point p){
        	points.add(p);
        }
        
        public void draw(Canvas canvas){
        	for(int i = 0; i < points.size()-1; i++){
        		Point p1 = points.get(i);
        		Point p2 = points.get(i+1);
        		
        		linePaint.setColor(color);
        		linePaint.setStrokeWidth(thickness);
        		
        		canvas.drawLine(p1.getX(), p1.getY(), p2.getX(), p2.getY(), linePaint);
        	}
        }
        
        public void setColor(int c)
        {
        	color = c;
        }
        
        public void setThickness(int t)
        {
        	thickness = t;
        }

		@Override
		public int describeContents() {
			return 0;
		}

		@Override
		public void writeToParcel(Parcel arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}
    }

	/** Prototype: Curve class used for drawing 
	 * So the idea here is the drawing contains an arraylist of curves, and each curve contains
	 * an array list of lines, as well as the instructinos to draw those lines, i.e. the line
	 * color and thickness. This is nice because then you don't save several bytes of information
	 * in every line, since there are easily many thousands of lines. It would also make it easy
	 * to erase a single curve if an eraser tool were to be implemented.
	 * NOT IMPLEMENTED*/
//    private static class Curve {
//        /**
//         * line color
//         */
//        public int color;
//        
//        /**
//         * line thickness
//         */
//        public int thickness;
//        
//    	/**
//    	 * Stores all of the lines that make up the curve.
//    	 */
//    	private ArrayList<Line> lines;
//    }
    
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
        bundle.putParcelableArrayList(LINES, lines);
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
        lines = bundle.getParcelableArrayList(LINES);
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
    
    @Override 
    public boolean onTouchEvent(MotionEvent e) { 
        if(!moveFlag){ 
            Point point = new Point();
            point.setX(e.getX());
            point.setY(e.getY());
            
            switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
            	isTouched = true;
	            currentLine = new Line();
	            currentLine.addPoint(point);
	            
	            currentLine.setColor(currentColor);
	            currentLine.setThickness(currentThickness);
                break;
            case MotionEvent.ACTION_MOVE:
                if(isTouched){
                	currentLine.addPoint(point);
                	lines.add(currentLine);
                	currentLine = new Line();
                	currentLine.addPoint(point);
                	currentLine.setColor(currentColor);
                	currentLine.setThickness(currentThickness);
                }
                break;
            case MotionEvent.ACTION_UP:
                if(isTouched)
                	currentLine.addPoint(point);
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
