package edu.msu.cse.monopoly.scribbles;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class Leaderboard extends Activity {

	private TextView first, second, third, fourth, fifth, 
	sixth, seventh, eighth, ninth, tenth;

	private TextView firstScore, secondScore, thirdScore, fourthScore, fifthScore, 
	sixthScore, seventhScore, eighthScore, ninthScore, tenthScore;

	
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
		
		
	}

	public void scoreUpdate(View view){
		//update score in here
		//order scores in the table by highest then pull row to match place
	}
	
	
    public void onBack(View view){
    	Intent intent = new Intent (this, WelcomeScreen.class);
    	startActivity(intent);
    }

	
	
}
