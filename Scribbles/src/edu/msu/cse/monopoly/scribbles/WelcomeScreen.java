package edu.msu.cse.monopoly.scribbles;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class WelcomeScreen extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome_screen);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_welcome_screen, menu);
		return true;
	}
	
    /**
     * Handle a Begin button press
     * @param view
     */
    public void onBegin(View view) {
    	// Start the color select activity
		Intent intent = new Intent(this, DrawActivity.class);
		startActivity(intent);
    }

}
