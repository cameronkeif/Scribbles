package edu.msu.cse.monopoly.scribbles;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Xml;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;

public class WelcomeScreen extends Activity {
    //private static final String PLAYER1 = "player1";
    private String message = "";

    CheckBox remember;
    EditText username;    
    EditText password;
    
    
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String DRAWFLAG = "drawflag";
    private String drawFlag;
    private String regId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome_screen);

		username = (EditText) findViewById(R.id.usernameEditText);
        password = (EditText) findViewById(R.id.passwordEditText);
        remember = (CheckBox)findViewById(R.id.checkBoxRemember);

        SharedPreferences settings = getSharedPreferences("STORED_PREFS_FILE", 0);
       
    	username.setText(settings.getString("username", ""));
    	password.setText(settings.getString("password", ""));
    	remember.setChecked(true);
        
        
        GCMRegistrar.checkDevice(this);
        GCMRegistrar.checkManifest(this);
        regId = GCMRegistrar.getRegistrationId(this);
        if(regId.equals("")){
        	GCMRegistrar.register(this,GCMIntentService.SENDER_ID);
        } else {
        	// do something with regId�
        }
	}
	
    /**
     * Handle a Begin button press
     * @param view
     */
    public void onBegin(final View view) {
        
        new Thread(new Runnable() {

            @Override
            public void run() {
                // Create a cloud object and get the XML
                Cloud cloud = new Cloud();
                //InputStream stream = cloud.openFromCloud()                   
                
                InputStream stream = cloud.loginToCloud(username.getText().toString(), password.getText().toString());
                
                // Test for an error
                boolean fail = stream == null;
                
                if(!fail) {
                    try {
                        XmlPullParser xml = Xml.newPullParser();
                        xml.setInput(stream, "UTF-8");       
                        
                        xml.nextTag();      // Advance to first tag
                        xml.require(XmlPullParser.START_TAG, null, "proj02");
                        String status = xml.getAttributeValue(null, "status");
                        drawFlag = xml.getAttributeValue(null, "draw");
                        message = xml.getAttributeValue(null, "msg");

                        
                        if(status.equals("yes")) {
                        	if(remember.isChecked()){
                                SharedPreferences settings = getSharedPreferences("STORED_PREFS_FILE", 0);
                                SharedPreferences.Editor editor = settings.edit();
                                editor.putString("username", username.getText().toString());
                                editor.putString("password", password.getText().toString());
                                editor.putBoolean("remember", remember.isChecked());    
                                
                                editor.commit();
                        	}
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
                    	String errorMessage = "";
                    	
                    	if (message.equals("user")){
                    		errorMessage = "Username not found.";
                    	}else if (message.equals("password error")){
                    		errorMessage = "Invalid password/username combination.";
                    	}else if (message.equals("magic")){
                    		errorMessage = "Invalid magic number.";
                    	}
                    	
                        Toast.makeText(view.getContext(), getText(R.string.login_fail) + " " + errorMessage, Toast.LENGTH_SHORT).show();
                    }else {
                        // Success!
                		onLogin(username.getText().toString(), password.getText().toString());
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
    
    public void onLogin(String username, String password){
        
 	final String tempUser = username;
        
        new Thread(new Runnable() {

            String url = "https://www.cse.msu.edu/~smaletho/cse476/proj02/connect.php?gcmid=" + regId + "&user=" + tempUser;
        	
            @Override
            public void run() {
            	        
		        //String url = "http://your.domain.com/path/to/file.php";
		        HttpClient client = new DefaultHttpClient();
		
		        try {
		          client.execute(new HttpGet(url));
		        } catch(IOException e) {
		          //do something here
		        }
            }
        }).start();
        
    	Intent intent = new Intent(this, Leaderboard.class);
    	intent.putExtra(USERNAME, username);
    	intent.putExtra(PASSWORD, password);
    	intent.putExtra(DRAWFLAG, drawFlag);
		startActivity(intent);
		
    }
}
