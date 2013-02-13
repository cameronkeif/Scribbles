package edu.msu.cse.monopoly.scribbles;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class WelcomeScreen extends Activity {
    private static final String PLAYER1 = "player1";
    private static final String PLAYER2 = "player2";
    
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
    	EditText player1Name = (EditText) findViewById(R.id.player1TextField);
    	EditText player2Name = (EditText) findViewById(R.id.player2TextField);
    	
    	// Start the drawing activity
		Intent intent = new Intent(this, DrawActivity.class);
		intent.putExtra(PLAYER1, (String) player1Name.getText().toString());
		intent.putExtra(PLAYER2, (String) player2Name.getText().toString());
		startActivity(intent);
    }

}
