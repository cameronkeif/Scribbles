package edu.msu.cse.monopoly.scribbles;

import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class NewUserActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_user);
	}

	public void onSubmit(final View view) {
		
        EditText usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        EditText passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        EditText confirmPasswordEditText = (EditText) findViewById(R.id.passwordConfirmEditText);
        
        final String username = usernameEditText.getText().toString();
        final String password = passwordEditText.getText().toString();
        final String confirmPassword = confirmPasswordEditText.getText().toString();
        
        
        if (confirmPassword.equals(password)){
        
		new Thread(new Runnable() {

            @Override
            public void run() {
                // Create a cloud object and get the XML
                Cloud cloud = new Cloud();
                //InputStream stream = cloud.openFromCloud();

                InputStream stream = cloud.createNewUser(username, password);
                
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
                        Toast.makeText(view.getContext(), R.string.new_user_fail, Toast.LENGTH_SHORT).show();
                    }else {
                        // Success!
                    	// Temporary toast just to show it's working. This will move to the next activity later.
                    	Toast.makeText(view.getContext(), R.string.new_user_success, Toast.LENGTH_SHORT).show();
                    	Activity activity = (Activity) view.getContext();
                    	activity.finish();
                    	}
                    
                	}
            
            	});
            }
        }).start();

        }
        else{
        
        Toast.makeText(view.getContext(), R.string.confirm_password_fail, Toast.LENGTH_SHORT).show();
    	passwordEditText.setText("");
    	confirmPasswordEditText.setText("");
        }
	}
}
