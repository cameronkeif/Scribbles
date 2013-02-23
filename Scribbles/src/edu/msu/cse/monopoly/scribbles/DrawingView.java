package edu.msu.cse.monopoly.scribbles;

import java.io.Serializable;
import java.util.ArrayList;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;

public class DrawingView extends View {
    private static final String PARAMETERS = "parameters";
    
    private Parameters params = new Parameters();
    
    /**
     * The line currently being drawn
     * changed line draw method
     */
    private Line currentLine = null;
    
	/**
	 * Paint for the line
	 */
	private static Paint linePaint;
    
	/**
	 * Paint for the border
	 */
	private static Paint borderPaint;
	
	private static class Parameters implements Serializable
	{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

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
		private float drawingX;
		
		/** 
		 * current y location of drawing
		 */
		private float drawingY;
		
		/** 
		 * current scale of drawing
		 */
		private float drawingScale;
		
		/** 
		 * current angle of rotation of drawing
		 */
		private float drawingAngle;
    
		
		/**
		 * Stores all of the lines that make up the drawing.
		 */
		private ArrayList<Line> lines = new ArrayList<Line>();
	}
	
    /**
     * First touch status
     */
    private Touch touch1 = new Touch();
    
    /**
     * Second touch status
     */
    private Touch touch2 = new Touch();

    
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
		borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		borderPaint.setColor(Color.MAGENTA);
		borderPaint.setStrokeWidth(2);
		borderPaint.setStyle(Style.STROKE);
		linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		params.currentColor = Color.BLACK;
		params.currentThickness = 1;
		circlePaint.setStyle(Paint.Style.FILL);

		setDrawingX(0);
		setDrawingY(0);
		setDrawingScale(1.0f);
		setDrawingAngle(0.0f);
	}
	
	/** Sets the drawing's x location
	 * @return */
	public void setDrawingX(int x){
		params.drawingX = x;
		invalidate();
	}
	
	public float getDrawingX(){
		return params.drawingX;
	}
	
	/** Sets the drawing's y location
	 * @return */
	public void setDrawingY(int y){
		params.drawingY = y;
		invalidate();
	}
	
	public float getDrawingY(){
		return params.drawingY;
	}
	
	/** Sets the drawing's scale
	 * @return */
	public void setDrawingScale(float s){
		params.drawingScale = s;
		invalidate();
	}
	
	public float getDrawingScale(){
		return params.drawingScale;
	}
	
	/** Sets the drawing's rotation
	 * @return */
	public void setDrawingAngle(float a){
		params.drawingAngle = a;
		invalidate();
	}
	
	public float getDrawingAngle(){
		return params.drawingAngle;
	}
	
	/** Sets the current color
	 * @return */
	public void setColor(int color){
		params.currentColor = color;
		invalidate();
	}
	
	/**
	 * Returns the current color
	 * @return the current color
	 */
	public int getColor(){
		return params.currentColor;
	}
	
	/**
	 * Sets the flag indicating if we are manipulating with multitouch
	 * @param b The flag
	 */
	public void setMoveFlag(boolean b){
		params.moveFlag = b;
	}
	
	/**
	 * return the flag indicating if we are manipulating with multitouch
	 * @return the flag indicating if we are manipulating with multitouch
	 */
	public boolean getMoveFlag(){
		return params.moveFlag;
	}
	
	/** Sets the current thickness
	 * @param thickness the thickness to set 
	 */
	public void setThickness(int thickness){
		params.currentThickness = thickness;
		invalidate();
	}
	
	/**
	 * Returns the thickness
	 * @return the thickness
	 */
	public int getThickness(){
		return params.currentThickness;
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
        public void draw(Canvas canvas, float locX, float locY, float scale, float angle){
    		linePaint.setColor(color);
    		linePaint.setStrokeWidth(thickness);
    		circlePaint.setColor(color);
    		
    		float trueStartX = startX /*+ locX*/;
    		float trueStartY = startY /*+ locY*/;
    		float trueEndX = endX /*+ locX*/;
    		float trueEndY = endY /*+ locY*/;
    		
    		canvas.drawLine(trueStartX, trueStartY,
    				trueEndX, trueEndY, linePaint);
    		
    		canvas.drawCircle(trueEndX, trueEndY,
    				thickness/2, circlePaint);
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
        
        canvas.save();
        canvas.translate(params.drawingX,  params.drawingY);
        canvas.scale(params.drawingScale, params.drawingScale);
        canvas.rotate(params.drawingAngle);
    	for(Line line:params.lines){
    		//this nonsense is because you can't access these variables
    		//directly from the static line class
    		line.draw(canvas, params.drawingX, params.drawingY,
    				params.drawingAngle, params.drawingScale);
    	}
    	canvas.restore();
    	
    	//border
    	canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), borderPaint);
    }
    
    /**
     * Save the view state to a bundle
     * @param bundle bundle to save to
     */
    public void putToBundle(Bundle bundle) {
        bundle.putSerializable(PARAMETERS, params);
    }
    
    /**
     * Get the view state from a bundle
     * @param bundle bundle to load from
     */
    public void getFromBundle(Bundle bundle) {       
    	params = (Parameters) bundle.getSerializable(PARAMETERS);

    	if (params == null){
    		params = new Parameters();
    	}
        invalidate();
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
        if(!params.moveFlag){
        	
        	//this first section adjusts the input according
        	//to transformation
        	
            // Compute the radians angle, reverse of drawing rotation
            double rAngle = -1 * Math.toRadians(params.drawingAngle);
            float ca = (float) Math.cos(rAngle);
            float sa = (float) Math.sin(rAngle);
            float xp = (params.drawingX - e.getX()) * ca - (params.drawingY - e.getY()) * sa + e.getX();
            float yp = (params.drawingX - e.getX()) * sa + (params.drawingY - e.getY()) * ca + e.getY();

            float dxp = params.drawingX - xp;
            float dyp = params.drawingY - yp;
            float x = (e.getX() - params.drawingX + dxp) / params.drawingScale;
            float y = (e.getY() - params.drawingY + dyp) / params.drawingScale;
        	
        	
            
            switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
	            currentLine = new Line(x, y);
	            
	            currentLine.setColor(params.currentColor);
	            currentLine.setThickness(params.currentThickness);
                break;
            case MotionEvent.ACTION_MOVE:
            	currentLine.endX = x;
            	currentLine.endY = y;
            	params.lines.add(currentLine);
            	
	            currentLine = new Line(x, y);
	            
            	currentLine.setColor(params.currentColor);
            	currentLine.setThickness(params.currentThickness);
                break;
            case MotionEvent.ACTION_UP:
            	currentLine.endX = x;
            	currentLine.endY = y;
                params.lines.add(currentLine);
                currentLine = null;
                break;
            }
            invalidate();
            return true;
        }
        else
        {
            int id = e.getPointerId(e.getActionIndex());
            
            switch(e.getActionMasked()) {
	            case MotionEvent.ACTION_DOWN:
	                touch1.id = id;
	                touch2.id = -1;
	                getPositions(e);
	                touch1.copyToLast();
	                invalidate();
	                return true;
	                
	            case MotionEvent.ACTION_POINTER_DOWN:
	            	//if not moving, don't want second touch
	                if(touch1.id >= 0 && touch2.id < 0) {
	                    touch2.id = id;
	                    getPositions(e);
	                    touch2.copyToLast();
	                    return true;
	                }
	                break;
	                
	            case MotionEvent.ACTION_UP:
	            case MotionEvent.ACTION_CANCEL:
	                touch1.id = -1;
	                touch2.id = -1;
	                invalidate();
	                return true;
	                
	            case MotionEvent.ACTION_POINTER_UP:
	                if(id == touch2.id) {
	                    touch2.id = -1;
	                } else if(id == touch1.id) {
	                    // Make what was touch2 now be touch1 by 
	                    // swapping the objects.
	                    Touch t = touch1;
	                    touch1 = touch2;
	                    touch2 = t;
	                    touch2.id = -1;
	                }
	                invalidate();
	                return true;
	                
	            case MotionEvent.ACTION_MOVE:
	                getPositions(e);
	                transform();
	                invalidate();
	                return true;
            }
            
            return super.onTouchEvent(e);
        }
    }
    
    /**
     * Get the positions for the two touches and put them
     * into the appropriate touch objects.
     * @param event the motion event
     */
    private void getPositions(MotionEvent event) {
        for(int i=0;  i<event.getPointerCount();  i++) {
            
            // Get the pointer id
            int id = event.getPointerId(i);
            
            // Get coords, convert to image coordinates
            float x = event.getX(i);
            float y = event.getY(i);
//            float x = (event.getX(i) - marginLeft) / imageScale;
//            float y = (event.getY(i) - marginTop) / imageScale;
            
            if(id == touch1.id) {
            	touch1.copyToLast();
                touch1.x = x;
                touch1.y = y;
            } else if(id == touch2.id) {
            	touch2.copyToLast();
                touch2.x = x;
                touch2.y = y;
            }
        }
        
        invalidate();
    }
    
    /**
     * Handle movement of the touches for move mode
     */
    private void transform() {
        // If no touch1, we have nothing to do
        // This should not happen, but it never hurts
        // to check.
        if(touch1.id < 0) { 
            return;
        }
        
        if(touch1.id >= 0) {
            // At least one touch
            // We are moving
            touch1.computeDeltas();
            
            params.drawingX += touch1.dX;
            params.drawingY += touch1.dY;
        }
        
        if(touch2.id >= 0) {
            // Two touches
            
            /*
             * Rotation
             */
            float angle1 = angle(touch1.lastX, touch1.lastY, touch2.lastX, touch2.lastY);
            float angle2 = angle(touch1.x, touch1.y, touch2.x, touch2.y);
            float da = angle2 - angle1;
            rotate(da, touch1.x, touch1.y);
            
            /*
             * Scaling
             */
            float dist1 = FloatMath.sqrt( (touch1.lastX-touch2.lastX)*(touch1.lastX-touch2.lastX) + (touch1.lastY-touch2.lastY)*(touch1.lastY-touch2.lastY) );
            float dist2 = FloatMath.sqrt( (touch1.x-touch2.x)*(touch1.x-touch2.x) + (touch1.y-touch2.y)*(touch1.y-touch2.y) );
            float sRatio = dist2/dist1;
            scale(sRatio, touch1.x, touch1.y);
        }
    }
        
    /**
     * Rotate the image around the point x1, y1
     * @param dAngle Angle to rotate in degrees
     * @param x1 rotation point x
     * @param y1 rotation point y
     */
    public void rotate(float dAngle, float x1, float y1) {
        params.drawingAngle += dAngle;
        
        // Compute the radians angle
        double rAngle = Math.toRadians(dAngle);
        float ca = (float) Math.cos(rAngle);
        float sa = (float) Math.sin(rAngle);
        float xp = (params.drawingX - x1) * ca - (params.drawingY - y1) * sa + x1;
        float yp = (params.drawingX - x1) * sa + (params.drawingY - y1) * ca + y1;

        params.drawingX = xp;
        params.drawingY = yp;
    }
    
    /**
     * scale the hat
     * @param scaleRatio multiple by which to scale the drawing
     */
    public void scale(float scaleRatio, float x1, float y1) {
        params.drawingScale *= scaleRatio;
        
        // distance from drawing 'origin' to scale pivot
        float dx = params.drawingX - x1;
        float dy = params.drawingY - y1;
        
        // Compute scaled hatX, hatY
        params.drawingX = x1 + dx * scaleRatio;
        params.drawingY = y1 + dy * scaleRatio;
    }
    
    /**
     * Determine the angle for two touches
     * @param x1 Touch 1 x
     * @param y1 Touch 1 y
     * @param x2 Touch 2 x
     * @param y2 Touch 2 y
     * @return computed angle in degrees
     */
    private float angle(float x1, float y1, float x2, float y2) {
        float dx = x2 - x1;
        float dy = y2 - y1;
        return (float) Math.toDegrees(Math.atan2(dy, dx));
    }
    
    public Parameters getParams()
    {
    	return params;
    }

}