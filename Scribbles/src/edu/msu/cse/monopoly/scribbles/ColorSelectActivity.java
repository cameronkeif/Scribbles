package edu.msu.cse.monopoly.scribbles;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

public class ColorSelectActivity extends Activity {
    private static final String COLOR = "color";
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_color_select);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_color_select, menu);
		return true;
	}

    public void selectColor(int color) {
    	Intent colorData = new Intent();
    	colorData.putExtra(COLOR, color);
    	setResult(Activity.RESULT_OK, colorData);
    	finish();
    }
}
