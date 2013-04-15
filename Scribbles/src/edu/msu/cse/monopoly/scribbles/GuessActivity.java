package edu.msu.cse.monopoly.scribbles;

import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.util.Xml;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class GuessActivity extends Activity {
    private static final String PLAYER1SCORE = "player1Score";
    private static final String HINT = "hint";
    private static final String TOPIC = "topic";
    private static final String ANSWER = "answer";
    private static final String TIMER = "timer";
    private static final String GUESS = "guess";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String DRAWFLAG = "drawflag";
    
    
    /**
     * The drawing view object
     */
    private DrawingView guessingView = null;
    
    private int player1Score;
    
    private String user;
    private String pw;
    private String Hint;
    private String Answer;
    private String drawingName;
    private String Category;
    private long currentTime;
        
    /**
     * Flag to indicate if the hint has been shown (25 pts)
     */
    private boolean hintShown = false;
    
    /**
     * Flag to indicate if the user ran out of time (0 pts)
     */
    private boolean timeExpired = false;
    
	@Override
	protected synchronized void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guess);
		
		Bundle bundle = getIntent().getExtras();
		
		user = bundle.getString(USERNAME);
		pw = bundle.getString(PASSWORD);
		
		// final versions to be used in the new thread.
		final String username = user;
		final String password = pw;
		
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
                    	xml.nextTag();
                    	if(xml.nextTag() == XmlPullParser.START_TAG){
                    		Hint = xml.getAttributeValue(null, "hint");
                    		Answer = xml.getAttributeValue(null, "solution");
                    		Category = xml.getAttributeValue(null, "category");
                    		drawingName = xml.getAttributeValue(null, "name");
                    		
                    		final TextView categoryText = (TextView) findViewById(R.id.Category);
                    		
                    		// Updates the text for the category/topic. The others are handled after loading.
                    		categoryText.post(new Runnable(){
                    			@Override
                    			public void run(){
                    				categoryText.setText("Topic: " + Category);
                    			}
                    		});
                    	}
                    	Cloud.skipToEndTag(xml);
                        while(xml.nextTag() == XmlPullParser.START_TAG) {
                            if(xml.getName().equals("") || xml.getName().equals("proj02")) {                                
                                break;
                            }
                            
                            // Initialize the parameters for this line.
                            int color = Integer.parseInt(xml.getAttributeValue(null, "color")); 
                            int thickness = Integer.parseInt(xml.getAttributeValue(null, "thickness"));
                            float startX = Float.parseFloat(xml.getAttributeValue(null, "startX"));
                            float startY = Float.parseFloat(xml.getAttributeValue(null, "startY"));
                            float endX = Float.parseFloat(xml.getAttributeValue(null, "endX"));
                            float endY = Float.parseFloat(xml.getAttributeValue(null, "endY"));
                            
                            // Load this line of XML
                            guessingView.loadXml(color, thickness, startX, startY, endX, endY);
                            guessingView.postInvalidate();
                            
                            Cloud.skipToEndTag(xml);
                        }
                    } else {
                        fail = true;
                    }
                    
                } catch(IOException ex) {
                    fail = true;
                } catch(XmlPullParserException ex) {
                    fail = true;
                    Log.e("xml error", ex.getMessage());
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
                    }
                    }  
            });
        }  
    }).start();
        
		
		final TextView myTimer = (TextView) findViewById(R.id.theTimer);
		final TextView hintText = (TextView) findViewById(R.id.Hint);
		
		

		long myBegin = 130000;
		
        if(user != null)
        {
            TextView player1ScoreText = (TextView) findViewById(R.id.player1ScoreText);
            
            	player1ScoreText.setText(Integer.toString(player1Score) + ": " + user);
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
			
			final int points = pointsAwarded;
		
			new Thread(new Runnable(){
				@Override
				public void run(){
					Cloud cloud = new Cloud();
					
					cloud.guessCloud(user, pw, drawingName, points);
				}
			}).start();
			
			Intent intent;
			intent = new Intent(this, Leaderboard.class);
				
			// Put the player's info in the bundle
			intent.putExtra(USERNAME, user);
			intent.putExtra(PASSWORD, pw);
			intent.putExtra(DRAWFLAG, "1"); // This will of course be 1, since the user has guessed.
				
				
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
		
		// Put the player's score in the bundle
		outState.putInt(PLAYER1SCORE, player1Score);
		
		// Put the topic string into the bundle
		outState.putString(TOPIC, Category);
		
		// Put the player's names in the bundle
		outState.putString(USERNAME, user);
		outState.putString(PASSWORD, pw);
		
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
                	
                	new Thread(new Runnable() {

                        @Override
                        public void run() {
                            // Create a cloud object and get the XML
                            Cloud cloud = new Cloud();
                            //InputStream stream = cloud.openFromCloud()                   
                            
                            cloud.logOut(user, pw);
                        }
               	 }).start();
                	
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
