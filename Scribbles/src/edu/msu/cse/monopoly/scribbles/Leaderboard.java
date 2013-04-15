package edu.msu.cse.monopoly.scribbles;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Xml;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Leaderboard extends Activity {
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String DRAWFLAG = "drawflag";
	
	
	private TextView first, second, third, fourth, fifth, 
	sixth, seventh, eighth, ninth, tenth;

	// Username
	private String user = "";
	
	// Password
	private String password = "";
	
	// Flag for the user's turn
	private String drawFlag = "";
	
	private TextView firstScore, secondScore, thirdScore, fourthScore, fifthScore, 
	sixthScore, seventhScore, eighthScore, ninthScore, tenthScore;

	// List containing all of the TextViews.
	private ArrayList <TextView> tvList = null;
	
	// List containing all of the scores.
	private ArrayList <TextView> scoreList = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_leaderboard);

		//set them all in here with the row given from the table as first etc
		
		first=(TextView) findViewById(R.id.number1);
		second=(TextView) findViewById(R.id.number2); 
		third =(TextView) findViewById(R.id.number3);
		fourth =(TextView) findViewById(R.id.number4);
		fifth =(TextView) findViewById(R.id.number5);
		sixth =(TextView) findViewById(R.id.number6);
		seventh= (TextView) findViewById(R.id.number7);
		eighth =(TextView) findViewById(R.id.number8);
		ninth =(TextView) findViewById(R.id.number9);
		tenth= (TextView) findViewById(R.id.number10);
		
		firstScore=(TextView) findViewById(R.id.score1);
		secondScore=(TextView) findViewById(R.id.score2); 
		thirdScore =(TextView) findViewById(R.id.score3);
		fourthScore =(TextView) findViewById(R.id.score4);
		fifthScore =(TextView) findViewById(R.id.score5);
		sixthScore =(TextView) findViewById(R.id.score6);
		seventhScore= (TextView) findViewById(R.id.score7);
		eighthScore =(TextView) findViewById(R.id.score8);
		ninthScore =(TextView) findViewById(R.id.score9);
		tenthScore= (TextView) findViewById(R.id.score10);
		
		Bundle bundle = getIntent().getExtras();
		
		user = bundle.getString(USERNAME);
		password = bundle.getString(PASSWORD);
		drawFlag = bundle.getString(DRAWFLAG);
		
		// Sets the username above the Play button.
		if (user != null){
			TextView userText = (TextView) findViewById(R.id.textViewUser);
			userText.setText(getString(R.string.user) + user);
		}
		
		tvList = new ArrayList <TextView>(Arrays.asList(first, second, third, fourth, fifth,
				sixth, seventh, eighth, ninth, tenth));
		
		scoreList = new ArrayList <TextView>(Arrays.asList(firstScore, secondScore, thirdScore, 
				fourthScore, fifthScore, sixthScore, seventhScore, eighthScore, ninthScore, tenthScore));
		
		scoreUpdate();
		
	}

	public void scoreUpdate(){
		// Add the stuff to the cloud.
		new Thread(new Runnable() {

            @Override
            public void run() {
				Cloud cloud = new Cloud();
					
				final InputStream stream = cloud.scoreboardCloud();
			
				// Test for an error
	            boolean fail = stream == null;
	            if(!fail) {
	                try {
	                    XmlPullParser xml = Xml.newPullParser();
	                    xml.setInput(stream, "UTF-8");       
	                    
	                    xml.nextTag();      // Advance to first tag
	                    xml.require(XmlPullParser.START_TAG, null, "proj02");
	                    String status = xml.getAttributeValue(null, "status");
	                    int scoreCount = 0;
	                    if(status.equals("yes")) {
	                    	
	                        while(xml.nextTag() == XmlPullParser.START_TAG) {
	                            if(xml.getName().equals("") || scoreCount >= 10) {                                
	                                break;
	                            }
	                            String user = xml.getAttributeValue(null, "user");
	                            String score = xml.getAttributeValue(null, "score");
	                            
	                            updateText(tvList.get(scoreCount), scoreList.get(scoreCount), user, score, scoreCount);
	                            
                            	scoreCount++;
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
	            final boolean fail1=fail;
	            first.post(new Runnable() {

	                @Override
	                public void run() {
	                    if(fail1) {
	                        Toast.makeText(first.getContext(), R.string.leaderboard_fail, Toast.LENGTH_SHORT).show();
	                    }else {
	                        // Success!
	                        // Need to update the UI, load the picture.
	                        }
	                    }  
	            });
	            

            }
	}).start();
	}
	
	
    public void onBack(View view){
    	finish(); // This destroys the activity, which is what we want.
    }

    /**
     * Updates an entry in the scoreboard
     * @param userText The username textview
     * @param scoreText The score textview
     * @param user The username
     * @param score Their score
     */
	public void updateText(final TextView userText, final TextView scoreText, final String user, final String score, final int scoreCount)
	{
		userText.post(new Runnable(){
			 @Override
			 public void run(){
				 userText.setText(Integer.toString(scoreCount + 1) + ". " + user + ": ");
			 }
		});
		
		scoreText.post(new Runnable(){
			 @Override
			 public void run(){
				 scoreText.setText(score);
			 }
		});
	}
	
	/**
	 * Handle play button press
	 * @param view The button
	 */
	public void onPlay(View view){
		if(drawFlag.equals("1")){ // It's their turn to draw
			Intent intent = new Intent(this, DrawActivity.class);
			
			intent.putExtra(USERNAME, user);
			intent.putExtra(DRAWFLAG, drawFlag);
			intent.putExtra(PASSWORD, password);
			
			startActivity(intent);
		}
		else{ // It's their turn to guess.
			Intent intent = new Intent(this, GuessActivity.class);
			
			intent.putExtra(USERNAME, user);
			intent.putExtra(DRAWFLAG, drawFlag);
			intent.putExtra(PASSWORD, password);
			
			startActivity(intent);
		}
	}
}
