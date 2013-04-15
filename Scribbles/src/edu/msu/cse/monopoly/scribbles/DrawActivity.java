package edu.msu.cse.monopoly.scribbles;

import java.util.ArrayList;
import java.util.Random;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Html;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;



public class DrawActivity extends Activity {
    private static final String COLOR = "color";
    private static final String PLAYER1 = "player1";
    private static final String PLAYER2 = "player2";
    private static final String PLAYER1SCORE = "player1Score";
    private static final String HINT = "hint";
    private static final String TOPIC = "topic";
    private static final String ANSWER = "answer";
    private static final String MOVETOGGLE = "moveToggle";
    private static final String WHOSDRAWING = "whosDrawing";
    //private static final String PARAMETERS = "parameters";
    
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    
    
	/**
     * The drawing view object
     */
    private DrawingView drawingView = null;
    
    /**
     * Request code when selecting a color
     */
    private static final int SELECT_COLOR = 1;

    /**
     * The thickness choice spinner
     */
    private Spinner spinner;
    
    private ArrayList<String> possibleTopics = new ArrayList<String>();
    
    private int player1Score;
    
    private String username = "";
    private String player2Name = "";
    
    private String password = "";
    
    private int whosDrawing;
    
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_draw);

        spinner = (Spinner) findViewById(R.id.spinnerThickness);
		drawingView = (DrawingView) findViewById(R.id.drawingView);

		possibleTopics.add(getString(R.string.animal));
		possibleTopics.add(getString(R.string.building));
		possibleTopics.add(getString(R.string.object));
		possibleTopics.add(getString(R.string.action));
		possibleTopics.add(getString(R.string.msu));

        Bundle bundle = getIntent().getExtras();

        // Displays player names and their scores
        // bundle.getInt returns 0 by default, so coming from the welcome screen is not a problem
        
        player1Score = bundle.getInt(PLAYER1SCORE);
        
        username = bundle.getString(USERNAME); // changed for cloud.
        
        password = bundle.getString(PASSWORD);
        
        if(username != null)
        {
            TextView player1ScoreText = (TextView) findViewById(R.id.player1ScoreText);
            
            if (username.equals("")){ // User enters nothing
            	username = getString(R.string.player1);
            	player1ScoreText.setText(getString(R.string.player1) + ": " + Integer.toString(player1Score));
            }
            player1ScoreText.setText(Integer.toString(player1Score) + ": " + username);
        }
        
        TextView topicText = (TextView) findViewById(R.id.topicText);
        
        if(savedInstanceState != null){ // We're rotating, load the relevent strings
        	 topicText.setText(savedInstanceState.getString(TOPIC));
        	 
        	 EditText answerBox = (EditText) findViewById(R.id.answerBox);
        	 answerBox.setText(savedInstanceState.getString(ANSWER));
        	 
        	 EditText hintBox = (EditText) findViewById(R.id.hintBox);
        	 hintBox.setText(savedInstanceState.getString(HINT));
        	 
             drawingView.getFromBundle(savedInstanceState);
     		Button colorButton = (Button) findViewById(R.id.buttonColor);
     		
     		colorButton.setTextColor(drawingView.getColor());
     		
     		Button buttonMoveToggle = (Button) findViewById(R.id.buttonMoveToggle);
     		buttonMoveToggle.setText(savedInstanceState.getString(MOVETOGGLE));
        }
        else{
        // Pick a random topic
        Random rand = new Random();

        topicText.setText(getString(R.string.topic) + " " + possibleTopics.get(rand.nextInt(possibleTopics.size())));
        }
        
        /* NOTE: spinner currently filled with some random default values.
         */
        
        /*
         * Set up the spinner
         */

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
             R.array.thickness_spinner, android.R.layout.simple_spinner_item);
        
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        
        spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View view,
                    int pos, long id) {
            	if (pos == 0){
                    drawingView.setThickness(4);
            	}
            	if (pos == 1){
                    drawingView.setThickness(10);
            	}
            	if (pos == 2){
                    drawingView.setThickness(20);
            	}
            	if (pos == 3){
                    drawingView.setThickness(50);
            	}
            	if (pos == 4){
                    drawingView.setThickness(100);
            	}

            }

			@Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        
        
	}

    /**
     * Handle a Done button press. Should start the guessing activity.
     * @param view
     */
    public void onDone(View view) {
    	
    	EditText hintBox = (EditText) findViewById(R.id.hintBox);
    	EditText answerBox = (EditText) findViewById(R.id.answerBox);
    	
    	if (hintBox.getText().toString().equals("") || answerBox.getText().toString().equals("")){
    		// User needs to answer the hint/answer
    		// Display a dialog box letting them know
    		AlertDialog.Builder builder = new AlertDialog.Builder(this);

        	builder.setMessage(Html.fromHtml("<font color='#000000'>"+getString(R.string.answer_hint_dialog_text)+"</font>"));
        	builder.setTitle(Html.fromHtml("<font color='#000000'>"+getString(R.string.answer_hint_dialog_title)+"</font>"));

        	AlertDialog answerHintDialog = builder.create();
        	
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                	// No need to actually *do* anything. Just want to let the user know what's up.
                }
            });
            if (!((Activity) this).isFinishing()) {
            	builder.show();
            }
            
            answerHintDialog.dismiss();
    		
    	}else if(drawingView.isEmpty()){
    		AlertDialog.Builder builder = new AlertDialog.Builder(this);

        	builder.setMessage(Html.fromHtml("<font color='#000000'>"+getString(R.string.drawing_warning_text)+"</font>"));
        	builder.setTitle(Html.fromHtml("<font color='#000000'>"+getString(R.string.drawing_warning_title)+"</font>"));

        	AlertDialog answerHintDialog = builder.create();
        	
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                	// No need to actually *do* anything. Just want to let the user know what's up.
                }
            });
            if (!((Activity) this).isFinishing()) {
            	builder.show();
            }
            
            answerHintDialog.dismiss();
    	}else{
	    	// Start the guessing activity
	    	// Change DrawActivity.class to the guess activity class when it is created.
	    	/*
			Intent intent = new Intent(this, GuessActivity.class);
			
			String hint = hintBox.getText().toString();
			intent.putExtra(HINT, hint);
			
			String answer = answerBox.getText().toString();
			intent.putExtra(ANSWER, answer);
			
			// Put the player's scores in the bundle
			intent.putExtra(PLAYER1SCORE, player1Score);
			
			// Put the topic string into the bundle
			TextView topicBox = (TextView) findViewById(R.id.topicText);
			String topic = topicBox.getText().toString();
			intent.putExtra(TOPIC, topic);
			
			// Put the player's names in the bundle
			intent.putExtra(PLAYER1, username);
			intent.putExtra(PLAYER2, player2Name);
			
			// Put the current artist number into the bundle
			intent.putExtra(WHOSDRAWING, whosDrawing);
			
			intent.putExtra(PARAMETERS, drawingView.getParams());
			*/
    		
			// Add the stuff to the cloud.
			new Thread(new Runnable() {

	            @Override
	            public void run() {
				Cloud cloud = new Cloud();
				
				EditText hintBox = (EditText) findViewById(R.id.hintBox);
		    	EditText answerBox = (EditText) findViewById(R.id.answerBox);
				TextView topicBox = (TextView) findViewById(R.id.topicText);
				
				String hint = hintBox.getText().toString();
				String answer = answerBox.getText().toString();
				String topic = topicBox.getText().toString();
				
				// Remove the "Topic: " part of the string.
				topic = topic.substring(7);
				
				final boolean ok = cloud.saveToCloud(drawingView, username, password, hint, answer, topic);

                if(!ok) {
                    /*
                     * If we fail to save, display a toast 
                     */
                	
                	drawingView.post(new Runnable() {

                    @Override
                    public void run() {
                        if(!ok) {
                            Toast.makeText(drawingView.getContext(), R.string.saving_fail, Toast.LENGTH_SHORT).show();
                        }
                        	
                    }
                    
                    });
                }
                else{
                	//Toast.makeText(drawingView.getContext(), R.string.saving_success, Toast.LENGTH_SHORT).show();
                	finish(); // Exit the activity
                }
            }
    	}).start();
			/*
			// Start the guessing activity
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
			*/
			
			
			
    	}
    }
    
    /**
     * Handle a Color button press
     * @param view
     */
    public void onColor(View view) {
    	// Start the color select activity
		Intent intent = new Intent(this, ColorSelectActivity.class);
		startActivityForResult(intent, SELECT_COLOR); 
    }
    
    /**
     * Handle a move/draw toggle button press
     * @param view
     */
    public void onMoveToggle(View view) {
    	Button buttonMoveToggle = (Button) findViewById(R.id.buttonMoveToggle);
    	
    	if(!drawingView.getMoveFlag()){ // Set to move mode.
    		buttonMoveToggle.setText(R.string.draw);
    		drawingView.setMoveFlag(true);
    	}
    	else{ // Set to draw mode
    		buttonMoveToggle.setText(R.string.move);
    		drawingView.setMoveFlag(false);
    	}
    }
    
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == SELECT_COLOR && resultCode == Activity.RESULT_OK) {
			int color = data.getIntExtra(COLOR, 0);
            
            // set the member variable in the draw view to whatever color we get from above.
            drawingView.setColor(color);
            
    		Button colorButton = (Button) findViewById(R.id.buttonColor);
    		colorButton.setTextColor(color);
        }
	}
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        
		// Put the artist's hint in the bundle
		EditText hintBox = (EditText) findViewById(R.id.hintBox);
		String hint = hintBox.getText().toString();
		outState.putString(HINT, hint);
		
		// Put the artist's answer in the bundle
		EditText answerBox = (EditText) findViewById(R.id.answerBox);
		String answer = answerBox.getText().toString();
		outState.putString(ANSWER, answer);
		
		// Put the player's scores in the bundle
		outState.putInt(PLAYER1SCORE, player1Score);
		
		// Put the topic string into the bundle
		TextView topic = (TextView) findViewById(R.id.topicText);
		outState.putString(TOPIC, topic.getText().toString());
		
		// Put the player's names in the bundle
		outState.putString(PLAYER1, username);
		outState.putString(PLAYER2, player2Name);
		
		// Put the current artist number in the bundle
		outState.putInt(WHOSDRAWING, whosDrawing);
		
		Button buttonMoveToggle = (Button) findViewById(R.id.buttonMoveToggle);
		outState.putString(MOVETOGGLE, buttonMoveToggle.getText().toString());
		
		drawingView.putToBundle(outState);
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
                             
                             cloud.logOut(username, password);
                         }
                	 }).start();
                	
            		Intent intent = new Intent(DrawActivity.this, WelcomeScreen.class);
            		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            		startActivity(intent);
                }
            });
            builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User wants to stay here, do nothing.
                }
            });
        			
            builder.show();
            warningDialog.dismiss();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_draw, menu);
		return true;
	}
	
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.clear:
        	AlertDialog.Builder builder = new AlertDialog.Builder(this);

        	builder.setMessage(Html.fromHtml("<font color='#000000'>"+getString(R.string.clear_warning_text)+"</font>"));
        	
        	builder.setTitle(Html.fromHtml("<font color='#000000'>"+getString(R.string.clear_warning_title)+"</font>"));

        	AlertDialog warningDialog = builder.create();
        	
            builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                	// User wants to clear
                	drawingView.clearDrawing();
                }
            });
            builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User changed their mind
                }
            });
        			
            builder.show();
            warningDialog.dismiss();
            return true;
            
        default:
            return super.onOptionsItemSelected(item);
        }
    }
}
