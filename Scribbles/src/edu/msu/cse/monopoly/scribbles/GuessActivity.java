package edu.msu.cse.monopoly.scribbles;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class GuessActivity extends Activity {
    private static final String PLAYER1 = "player1";
    private static final String PLAYER2 = "player2";
    private static final String PLAYER1SCORE = "player1Score";
    private static final String PLAYER2SCORE = "player2Score";
    private static final String HINT = "hint";
    private static final String TOPIC = "topic";
    private static final String ANSWER = "answer";
    private static final String TIMER = "timer";
    private static final String GUESS = "guess";
    
    /**
     * The drawing view object
     */
    private DrawingView guessingView = null;
    
    private int player1Score;
    private int player2Score;
    
    private String player1Name;
    private String player2Name;
    private String Hint;
    private String Answer;
    private String Category;
    private long currentTime;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guess);
		
		
		Bundle bundle = getIntent().getExtras();
		
		player1Score = bundle.getInt(PLAYER1SCORE);
        player2Score = bundle.getInt(PLAYER2SCORE);
        player1Name = bundle.getString(PLAYER1);
        player2Name = bundle.getString(PLAYER2);
        Hint = bundle.getString(HINT);
        Answer = bundle.getString(ANSWER);
        Category = bundle.getString(TOPIC);
        
        //guessingView = (DrawingView) findViewById(R.id.drawingView);
        //guessingView.setMoveFlag(true);
		final TextView myTimer = (TextView) findViewById(R.id.theTimer);
		final TextView hintText = (TextView) findViewById(R.id.Hint);
		
		TextView categoryText = (TextView) findViewById(R.id.Category);
		categoryText.setText(Category);
		long myBegin = 130000;
		
		if(savedInstanceState != null) { // We're rotating, load the relevent strings
			myBegin = savedInstanceState.getLong(TIMER);
			EditText guessBox = (EditText) findViewById(R.id.myGuess);
			guessBox.setText(savedInstanceState.getString(GUESS));	
		}
		
		
		new CountDownTimer(myBegin, 1000) {
		     public void onTick(long msTillDone) {
		    	 currentTime = msTillDone;
		    	 long temp = msTillDone/1000;
		    	 if ((msTillDone / 60000) == 2) {
		    		 temp -= 120;
		    		 myTimer.setText("2:" + (temp < 10 ? ("0"+temp) : temp));
		    	 } else if ((msTillDone / 60000 ) == 1){
		    		 temp -= 60;
		    		 myTimer.setText("1:" + (temp < 10 ? ("0"+temp) : temp));
		    	 } else {
		    		 myTimer.setText("0:" + (temp < 10 ? ("0"+temp) : temp));
		    	 }
		    	 
		    	 if (msTillDone<=70000) {
		    		 hintText.setText("Hint: "+ Hint);
		    	 } 
		     }

		     public void onFinish() {
		         myTimer.setText("Out of Time!");
		     }
		  }.start();
		  
	

	}
	
	public void onDone(View view) {
    	// Start the guessing activity
    	// Change DrawActivity.class to the guess activity class when it is created.
    	
		Intent intent = new Intent(this, DrawActivity.class);
		
		// Put the player's scores in the bundle
		intent.putExtra(PLAYER1SCORE, player1Score);
		intent.putExtra(PLAYER2SCORE, player2Score);
		
		// Put the player's names in the bundle
		intent.putExtra(PLAYER1, player1Name);
		intent.putExtra(PLAYER2, player2Name);
		
		/* NEED TO PUT THE ARRAY OF LINES IN DRAWINGVIEW INTO THE BUNDLE! */
		
		// Back to the Drawing activity
		startActivity(intent);
    	
    }
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_guess, menu);
		return true;
	}
	
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        
		// Put the artist's hint in the bundle
		outState.putString(HINT, Hint);
		
		// Put the artist's answer in the bundle
		outState.putString(ANSWER, Answer);
		
		// Put the player's scores in the bundle
		
		outState.putInt(PLAYER1SCORE, player1Score);
		outState.putInt(PLAYER2SCORE, player2Score);
		
		// Put the topic string into the bundle
		outState.putString(TOPIC, Category);
		
		// Put the player's names in the bundle
		outState.putString(PLAYER1, player1Name);
		outState.putString(PLAYER2, player2Name);
		
		// Put the Timer in the bundle
		outState.putLong(TIMER, currentTime);
		
		EditText guessBox = (EditText) findViewById(R.id.myGuess);
		String Guess = guessBox.getText().toString();
		outState.putString(GUESS, Guess);
		
		
		guessingView.putToBundle(outState);
    }

}
