package edu.msu.cse.monopoly.scribbles;

import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import edu.msu.keifcame.hattercloud.HatterActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Html;
import android.util.Xml;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class GuessActivity extends Activity {
    private static final String PLAYER1 = "player1";
    private static final String PLAYER2 = "player2";
    private static final String PLAYER1SCORE = "player1Score";
    private static final String HINT = "hint";
    private static final String TOPIC = "topic";
    private static final String ANSWER = "answer";
    private static final String TIMER = "timer";
    private static final String GUESS = "guess";
    private static final String WHOSDRAWING = "whosDrawing";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    
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
    
    private int whosDrawing;
    
    /**
     * Flag to indicate if the hint has been shown (25 pts)
     */
    private boolean hintShown = false;
    
    /**
     * Flag to indicate if the user ran out of time (0 pts)
     */
    private boolean timeExpired = false;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guess);
		/* Code from when guess activity was called from draw activity.
		/*
		
		player1Score = bundle.getInt(PLAYER1SCORE);
        player1Name = bundle.getString(PLAYER1);
        player2Name = bundle.getString(PLAYER2);
        whosDrawing = bundle.getInt(WHOSDRAWING);
        Hint = bundle.getString(HINT);
        Answer = bundle.getString(ANSWER);
        Category = bundle.getString(TOPIC);
        whosDrawing = bundle.getInt(WHOSDRAWING);
        */
		
		/* Need to fill:
		 * username/score
		 * topic
		 * hint
		 */
		
		Bundle bundle = getIntent().getExtras();
		
		final String username = bundle.getString(USERNAME);
		final String password = bundle.getString(PASSWORD);
		
		guessingView = (DrawingView) findViewById(R.id.guessingView);
	    guessingView.setMoveFlag(true); // Always moving in guessing activity
		
		// Pull the stuff from the cloud.
		new Thread(new Runnable() {

            @Override
            public void run() {
			Cloud cloud = new Cloud();
			
			final InputStream stream = cloud.openFromCloud(username, password);

			// Test for an error
            boolean fail = stream == null;
            if(!fail) {
                try {
                    XmlPullParser xml = Xml.newPullParser();
                    xml.setInput(stream, "UTF-8");       
                    
                    xml.nextTag();      // Advance to first tag
                    xml.require(XmlPullParser.START_TAG, null, "proj02");
                    String status = xml.getAttributeValue(null, "status");
                    if(status.equals("yes")) {
                    	if(xml.nextTag() == XmlPullParser.START_TAG){
                    		Hint = xml.getAttributeValue(null, "hint");
                    		Answer = xml.getAttributeValue(null, "solution");
                    		Category = xml.getAttributeValue(null, "category");
                    	}
                        while(xml.nextTag() == XmlPullParser.START_TAG) {
                            if(xml.getName().equals("")) {                                
                                // do something with the hatting tag...
                            	guessingView.loadXml(xml);
                                break;
                            }
                            
                            Cloud.skipToEndTag(xml);
                        }
                    } else {
                        fail = true;
                    }
                    
                } catch(IOException ex) {
                    fail = true;
                } catch(XmlPullParserException ex) {
                    fail = true;
                } finally {
                    try {
                        stream.close(); 
                    } catch(IOException ex) {
                    }
                }
            }
            final boolean fail1 = fail;
            guessingView.post(new Runnable() {

                @Override
                public void run() {
                    if(fail1) {
                        Toast.makeText(guessingView.getContext(), R.string.loading_fail, Toast.LENGTH_SHORT).show();
                    }else {
                        // Success!
                        // Need to update the UI, load the picture.
                        }
                    }  
            });
        }  
    }).start();
        
		final TextView myTimer = (TextView) findViewById(R.id.theTimer);
		final TextView hintText = (TextView) findViewById(R.id.Hint);
		
		TextView categoryText = (TextView) findViewById(R.id.Category);
		categoryText.setText(Category);

		long myBegin = 130000;
		
        if(player1Name != null)
        {
            TextView player1ScoreText = (TextView) findViewById(R.id.player1ScoreText);
            
            	player1ScoreText.setText(Integer.toString(player1Score) + ": " + player1Name);
        }
		
		if(savedInstanceState != null) { // We're rotating, load the relevent strings
			myBegin = savedInstanceState.getLong(TIMER);
			EditText guessBox = (EditText) findViewById(R.id.guessBox);
			guessBox.setText(savedInstanceState.getString(GUESS));	
		}
		
		new CountDownTimer(myBegin, 1000) {
		     public void onTick(long msTillDone) {
		    	 currentTime = msTillDone;
		    	 long temp = msTillDone/1000;
		    	 if ((msTillDone / 70000) == 2) {
		    		 temp -= 120;
		    		 myTimer.setText("2:" + (temp < 10 ? ("0"+temp) : temp));
		    	 } else if ((msTillDone / 70000 ) == 1){
		    		 temp -= 70;
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
		         myTimer.setText("0:00");
		         EditText guessBox = (EditText) findViewById(R.id.guessBox);
		         guessBox.setText(Answer);
		         guessBox.setEnabled(false);
		         
		         hintShown = false;
		         timeExpired = true;
		     }
		  }.start();
	}
	
	public void onDone(View view) {
		EditText guessBox = (EditText) findViewById(R.id.guessBox);
		
		if (!guessBox.getText().toString().toLowerCase().equals(Answer.toLowerCase()) 
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
				
				// Put the player's names in the bundle
				intent.putExtra(PLAYER1, player1Name);
				intent.putExtra(PLAYER2, player2Name);
				
			}else{ // Nobody won, start a new round
				intent = new Intent(this, DrawActivity.class);
				
				// Put the player's scores in the bundle
				intent.putExtra(PLAYER1SCORE, player1Score);
				
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
		
		// Put the topic string into the bundle
		outState.putString(TOPIC, Category);
		
		// Put the player's names in the bundle
		outState.putString(PLAYER1, player1Name);
		outState.putString(PLAYER2, player2Name);
		
		// Put the Timer in the bundle
		outState.putLong(TIMER, currentTime);
		
		EditText guessBox = (EditText) findViewById(R.id.guessBox);
		String Guess = guessBox.getText().toString();
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
