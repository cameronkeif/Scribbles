package edu.msu.cse.monopoly.scribbles;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;



public class DrawActivity extends Activity {
    private static final String COLOR = "color";
    
    /**
     * Request code when selecting a color
     */
    private static final int SELECT_COLOR = 1;

    /**
     * The thickness choice spinner
     */
    private Spinner spinner;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_draw);
		
        spinner = (Spinner) findViewById(R.id.spinnerThickness);
        
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
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_draw, menu);
		return true;
	}

    /**
     * Handle a Done button press. Should start the guessing activity.
     * @param view
     */
    public void onDone(View view) {
    	// Start the guessing activity
    	// Change DrawActivity.class to the guess activity class when it is created.
    	
		/*Intent intent = new Intent(this, DrawActivity.class);
		startActivity(intent);*/
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
    
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == SELECT_COLOR && resultCode == Activity.RESULT_OK) {
            @SuppressWarnings("unused")// IMPORTANT!! Take this out when the below code is uncommented.
            
			int color = data.getIntExtra(COLOR, 0);
            
            // set the member variable in the draw view to whatever color we get from above.
            //drawView.setColor(color);
        }
	}
}
