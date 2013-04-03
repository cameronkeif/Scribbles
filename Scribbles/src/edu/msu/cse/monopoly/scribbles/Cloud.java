package edu.msu.cse.monopoly.scribbles;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class Cloud {
    private static final String MAGIC = "NechAtHa6RuzeR8x";
    private static final String LOGIN_URL = "https://www.cse.msu.edu/~smaletho/cse476/proj02/login.php";
    private static final String NEW_USER_URL = "https://www.cse.msu.edu/~smaletho/cse476/proj02/new-user.php";
    
    /**
     * Skip the XML parser to the end tag for whatever 
     * tag we are currently within.
     * @param xml the parser
     * @throws IOException
     * @throws XmlPullParserException
     */
    public static void skipToEndTag(XmlPullParser xml) 
            throws IOException, XmlPullParserException {
        int tag;
        do
        {
            tag = xml.next();
            if(tag == XmlPullParser.START_TAG) {
                // Recurse over any start tag
                skipToEndTag(xml);
            }
        } while(tag != XmlPullParser.END_TAG && 
        tag != XmlPullParser.END_DOCUMENT);
    }
    
    public InputStream loginToCloud(String username, String password) {
    	username = username.trim();
    	password = password.trim();
    	
        if(username.length() == 0 || password.length() == 0) { // username/password not entered
            return null;
        }
        
        // Create a get query
        String query = LOGIN_URL + "?user=" + username +  "&pw=" + password + "&magic=" + MAGIC;
    	
        try {
            URL url = new URL(query);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            int responseCode = conn.getResponseCode();
            if(responseCode != HttpURLConnection.HTTP_OK) {
                return null;
            }
            
            InputStream stream = conn.getInputStream();
            return stream;

        } catch (MalformedURLException e) {
            // Should never happen
            return null;
        } catch (IOException ex) {
            return null;
        }
    }
    
    public InputStream createNewUser(String username, String password) {
    	username = username.trim();
    	password = password.trim();
    	
        if(username.length() == 0 || password.length() == 0) { // username/password not entered
            return null;
        }
        
        // Create a get query
        String query = NEW_USER_URL + "?user=" + username +  "&pw=" + password + "&magic=" + MAGIC;
        
        try {
            URL url = new URL(query);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            int responseCode = conn.getResponseCode();
            if(responseCode != HttpURLConnection.HTTP_OK) {
                return null;
            }
            
            InputStream stream = conn.getInputStream();
            return stream;

        } catch (MalformedURLException e) {
            // Should never happen
            return null;
        } catch (IOException ex) {
            return null;
        }
    }
}
