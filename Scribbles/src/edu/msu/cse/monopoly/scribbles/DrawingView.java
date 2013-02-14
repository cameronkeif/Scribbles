package edu.msu.cse.monopoly.scribbles;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;

public class DrawingView extends View {

	/** 
	 * Current color to draw in
	 */
	private int currentColor = Color.BLACK;
	
	/**
	 * Stores all of the lines that make up the drawing.
	 */
	private ArrayList<Line> lines;
	
	public DrawingView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public DrawingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public DrawingView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	
	/** Sets the current color
	 * @return */
	public void setColor(int color){
		currentColor = color;
	}
	
	/** Line class used for drawing */
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

}
