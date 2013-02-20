package edu.msu.cse.monopoly.scribbles;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

public class FinalScoreActivity extends Activity {
    private static final String PLAYER1 = "player1";
    private static final String PLAYER2 = "player2";
    private static final String PLAYER1SCORE = "player1Score";
    private static final String PLAYER2SCORE = "player2Score";
    
	/**
	 * Player 1's score
	 */
	private int player1Score;
	
	/**
	 * Player 2's score
	 */
	private int player2Score;
	
	/**
	 * Player 1's name
	 */
	private String player1Name;
	
	/**
	 * Player 2's name
	 */
	private String player2Name;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_final_score);
		
        Bundle bundle = getIntent().getExtras();
        
		player1Score = bundle.getInt(PLAYER1SCORE);
        player2Score = bundle.getInt(PLAYER2SCORE);
        player1Name = bundle.getString(PLAYER1);
        player2Name = bundle.getString(PLAYER2);
        
        TextView player1ScoreText = (TextView) findViewById(R.id.player1ScoreText);
        TextView player2ScoreText = (TextView) findViewById(R.id.player2ScoreText);
        
        player1ScoreText.setText(player1Name + ": " + Integer.toString(player1Score));
        player2ScoreText.setText(player2Name + ": " + Integer.toString(player2Score));
	}

    /**
     * Handle a New Game button press
     * @param view
     */
    public void onNewGame(View view) {
		Intent intent = new Intent(this, WelcomeScreen.class); 
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
    }

}
