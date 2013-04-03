package edu.msu.cse.monopoly.scribbles;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;

public class WelcomeScreen extends Activity {
    private static final String PLAYER1 = "player1";
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome_screen);
	}
	
    /**
     * Handle a Begin button press
     * @param view
     */
    public void onBegin(View view) {
    	EditText usernameEditText = (EditText) findViewById(R.id.usernameEditText);
    	
    	// Start the drawing activity
		Intent intent = new Intent(this, DrawActivity.class);
		intent.putExtra(PLAYER1, (String) usernameEditText.getText().toString());
		startActivity(intent);
    }

    /**
     * Handle a How To Play button press
     * @param view
     */
    public void onHowToPlay(View view) {
		Intent intent = new Intent(this, HowToPlayActivity.class);
		startActivity(intent);
    }
    
    /**
     * Handle a How To Play button press
     * @param view
     */
    public void onNewUser(View view) {
		Intent intent = new Intent(this, NewUserActivity.class);
		startActivity(intent);
    }
}
