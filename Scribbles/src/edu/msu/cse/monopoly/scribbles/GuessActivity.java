package edu.msu.cse.monopoly.scribbles;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

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
    private static final String WHOSDRAWING = "whosDrawing";
    private static final String SCORE = "Score: ";
    
    /**
     * The drawing view object
     */
    private DrawingView guessingView = null;
    
    private EditText theGuessBox = null;
    
    private int player1Score;
    private int player2Score;
    
    private String player1Name;
    private String player2Name;
    private String Hint;
    private String Answer;
    private String Category;
    private long currentTime;
    
    private int whosDrawing;
    
    /**
     * Flag to indicate if the hint has been shown (25 pts)
     */
    private boolean hintShown = false;
    
    //Must hide keyboard before losing focus to editText, or it's a bug!
    private void hideKeyboard() {
    	InputMethodManager imm = (InputMethodManager)getSystemService(
  		      Context.INPUT_METHOD_SERVICE);
    	imm.hideSoftInputFromWindow(theGuessBox.getWindowToken(), 0);
    }
    /**
     * Flag to indicate if the user ran out of time (0 pts)
     */
    private boolean timeExpired = false;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guess);
		
		Bundle bundle = getIntent().getExtras();
		
		player1Score = bundle.getInt(PLAYER1SCORE);
        player2Score = bundle.getInt(PLAYER2SCORE);
        player1Name = bundle.getString(PLAYER1);
        player2Name = bundle.getString(PLAYER2);
        whosDrawing = bundle.getInt(WHOSDRAWING);
        Hint = bundle.getString(HINT);
        Answer = bundle.getString(ANSWER);
        Category = bundle.getString(TOPIC);
        whosDrawing = bundle.getInt(WHOSDRAWING);
        
        guessingView = (DrawingView) findViewById(R.id.guessingView);
        theGuessBox = (EditText) findViewById(R.id.guessBox);
        

        guessingView.getFromBundle(bundle);
        guessingView.setMoveFlag(true); // Always moving in guessing activity
        
		final TextView myTimer = (TextView) findViewById(R.id.theTimer);
		final TextView hintText = (TextView) findViewById(R.id.Hint);
		long myBegin = 130000;
		
		TextView categoryText = (TextView) findViewById(R.id.Category);
		categoryText.setText(Category);
		
        if(player1Name != null)
        {
            TextView player1ScoreText = (TextView) findViewById(R.id.player1ScoreText);
            TextView player1NameText = (TextView) findViewById(R.id.player1NameText);
            
            	player1ScoreText.setText(SCORE + Integer.toString(player1Score));
            	player1NameText.setText(player1Name);
        }
        
        if(player2Name != null){
        	
            TextView player2ScoreText = (TextView) findViewById(R.id.player2ScoreText);
            TextView player2NameText = (TextView) findViewById(R.id.player2NameText);

            player2ScoreText.setText(SCORE + Integer.toString(player2Score));
        	player2NameText.setText(player2Name);
        }
		
		if(savedInstanceState != null) { // We're rotating, load the relevant strings
			myBegin = savedInstanceState.getLong(TIMER);
			if (myBegin == 0) {timeExpired = true;}
			theGuessBox.setText(savedInstanceState.getString(GUESS));	
		}
		
		// Set the guesser text view
    	TextView whosDrawingText = (TextView) findViewById(R.id.whosGuessingText);
		if (whosDrawing == 1){
        	whosDrawingText.setText(whosDrawingText.getText().toString() + " " + player2Name);
		}
		else{
        	whosDrawingText.setText(whosDrawingText.getText().toString() + " " + player1Name);
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
		    		 hintShown = true;
		    	 } 
		     }

		     public void onFinish() {
		    	 currentTime = 0;
		         myTimer.setText("0:00");
		         hintShown = false;
		         hideKeyboard();
		         theGuessBox.clearFocus();
		         if (!timeExpired) {
			         AlertDialog.Builder builder = new AlertDialog.Builder(GuessActivity.this);
			        	
			         builder.setMessage(Html.fromHtml("<font color='#000000'>"+"You have run out of time!<br /><br />Answer: "+Answer+"</font>"));
			         builder.setTitle(Html.fromHtml("<font color='#000000'>"+"Time!"+"</font>"));
			         theGuessBox.setText(Answer);
			         
			         AlertDialog warningDialog = builder.create();
		        	
			         builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			        	 public void onClick(DialogInterface dialog, int id) {
			        		 // No need to do anything, just letting them know they FAILED
			        	 }
			         });
		            
			         builder.show();
			         warningDialog.dismiss();
		         
		         }
		         timeExpired = true;
		     }
		  }.start();
		  
		  theGuessBox.setOnEditorActionListener(new OnEditorActionListener() {        
			    @Override
			    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			        if(actionId==EditorInfo.IME_ACTION_DONE){
			            //Clear focus here from the guess box
			        	hideKeyboard();
			            theGuessBox.clearFocus();
			            
			        }
			    return false;
			    }
			});
		  
		  // If Time has already expired, then can't change the Guess Text Box!
		  theGuessBox.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				if(timeExpired & arg1) {
					AlertDialog.Builder builder = new AlertDialog.Builder(GuessActivity.this);
		        	
		        	builder.setMessage(Html.fromHtml("<font color='#000000'>"+"Sorry, but time has already expired! Please click done to continue."+"</font>"));
		        	builder.setTitle(Html.fromHtml("<font color='#000000'>"+"Done!"+"</font>"));
		        	
		        	AlertDialog warningDialog = builder.create();
		        	
		            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
		                public void onClick(DialogInterface dialog, int id) {
		                	// No need to do anything, just letting them know they FAILED
		                }
		            });
		            
		            builder.show();
		            warningDialog.dismiss();
		            theGuessBox.clearFocus();
				}
			}		
		  });

	}
	
	public void onDone(View view) {	
		if (!theGuessBox.getText().toString().toLowerCase().equals(Answer.toLowerCase()) 
				&& !timeExpired){ 
			
			// Incorrect guess, let them know of their failure, but don't start new activity
	        AlertDialog.Builder builder = new AlertDialog.Builder(GuessActivity.this);
        	
        	builder.setMessage(Html.fromHtml("<font color='#000000'>"+getString(R.string.incorrect_guess_warning)+"</font>"));
        	builder.setTitle(Html.fromHtml("<font color='#000000'>"+getString(R.string.incorrect_guess_title)+"</font>"));
        	
        	AlertDialog warningDialog = builder.create();
        	
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                	// No need to do anything, just letting them know they FAILED
                }
            });
            
            builder.show();
            warningDialog.dismiss();
		}
		// Correct guess - score it
		else{
			// Calculate points to award
			int pointsAwarded = 50; // 50 points for correct guess without hint
			
			if (hintShown){
				pointsAwarded = 25; // 25 points for correct guess with hint
			}
			else if (timeExpired){
				pointsAwarded = 0;  // 0 points for expired time
			}
			
			if (whosDrawing == 1){
				player2Score += pointsAwarded;
			}
			if (whosDrawing == 2){
				player1Score += pointsAwarded;
			}
		
			Intent intent;
			if (player1Score >= 500 || player2Score >= 500){ // Someone won!!
				intent = new Intent(this, FinalScoreActivity.class);
				
				// Put the player's scores in the bundle
				intent.putExtra(PLAYER1SCORE, player1Score);
				intent.putExtra(PLAYER2SCORE, player2Score);
				
				// Put the player's names in the bundle
				intent.putExtra(PLAYER1, player1Name);
				intent.putExtra(PLAYER2, player2Name);
				
			}else{ // Nobody won, start a new round
				intent = new Intent(this, DrawActivity.class);
				
				// Put the player's scores in the bundle
				intent.putExtra(PLAYER1SCORE, player1Score);
				intent.putExtra(PLAYER2SCORE, player2Score);
				
				// Put the player's names in the bundle
				intent.putExtra(PLAYER1, player1Name);
				intent.putExtra(PLAYER2, player2Name);
				
				// Switch the drawer
				if (whosDrawing == 1){
					intent.putExtra(WHOSDRAWING, 2);
				}
				else{
					intent.putExtra(WHOSDRAWING, 1);
				}
			}
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
		}
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
		
		String Guess = theGuessBox.getText().toString();
		outState.putString(GUESS, Guess);
    }
    
    /** Handles key presses
     * @param keycode The code of the key
     * @param event The key event.
     * 
     * Used to disable back button
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {	
        	/* Create a warning dialog box. User can't go back to draw, but they can exit
        	 * to the home screen.
        	 */
        	AlertDialog.Builder builder = new AlertDialog.Builder(this);
        	
        	builder.setMessage(Html.fromHtml("<font color='#000000'>"+getString(R.string.dialog_box_warning)+"</font>"));
        	builder.setTitle(Html.fromHtml("<font color='#000000'>"+getString(R.string.dialog_box_title)+"</font>"));

        	AlertDialog warningDialog = builder.create();
        	
            builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                	// User wants to go back to the welcome screen
            		Intent intent = new Intent(GuessActivity.this, WelcomeScreen.class);
            		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            		startActivity(intent);
                }
            });
            builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User wants to stay here, do nothing.
                }
            });
        			
            if (!((Activity) this).isFinishing()) {
            	builder.show();
            }
            warningDialog.dismiss();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

}
