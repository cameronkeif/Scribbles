package edu.msu.cse.monopoly.scribbles;

import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Xml;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class WelcomeScreen extends Activity {
    //private static final String PLAYER1 = "player1";
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome_screen);
	}
	
    /**
     * Handle a Begin button press
     * @param view
     */
    public void onBegin(final View view) {
    	/* Old code.
    	EditText usernameEditText = (EditText) findViewById(R.id.usernameEditText);
    	
    	// Start the drawing activity
		Intent intent = new Intent(this, DrawActivity.class);
		intent.putExtra(PLAYER1, (String) usernameEditText.getText().toString());
		startActivity(intent);
		*/
    	
    	/*
         * Create a thread to load the hatting from the cloud
         */
        new Thread(new Runnable() {

            @Override
            public void run() {
                // Create a cloud object and get the XML
                Cloud cloud = new Cloud();
                //InputStream stream = cloud.openFromCloud();
                EditText usernameEditText = (EditText) findViewById(R.id.usernameEditText);
                EditText passwordEditText = (EditText) findViewById(R.id.passwordEditText);
                
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                InputStream stream = cloud.loginToCloud(username, password);
                
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
                        
                            while(xml.nextTag() == XmlPullParser.START_TAG) {
                                
                                
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
            view.post(new Runnable() {

                @Override
                public void run() {
                    if(fail1) {
                        Toast.makeText(view.getContext(), R.string.login_fail, Toast.LENGTH_SHORT).show();
                    }else {
                        // Success!
                    	// Temporary toast just to show it's working. This will move to the next activity later.
                    	Toast.makeText(view.getContext(), R.string.login_success, Toast.LENGTH_SHORT).show();
                    	}
                    
                	}
            
            	});
            }
        }).start();
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
     * Handle a New User button press
     * @param view
     */
    public void onNewUser(View view) {
		Intent intent = new Intent(this, NewUserActivity.class);
		startActivity(intent);
    }
}
