package edu.msu.cse.monopoly.scribbles;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import android.util.Log;
import android.util.Xml;

public class Cloud {
    private static final String MAGIC = "NechAtHa6RuzeR8x";
    private static final String LOGIN_URL = "https://www.cse.msu.edu/~smaletho/cse476/proj02/login.php";
    private static final String NEW_USER_URL = "https://www.cse.msu.edu/~smaletho/cse476/proj02/new-user.php";
    private static final String SAVE_URL = "https://www.cse.msu.edu/~smaletho/cse476/proj02/save-pic.php";
    private static final String LOAD_URL = "https://www.cse.msu.edu/~smaletho/cse476/proj02/load-pic.php";
    private static final String GUESS_URL = "https://www.cse.msu.edu/~smaletho/cse476/proj02/guess.php";
	private static final String UTF8 = "UTF-8";
	
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
    	//,
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
    
    /**
     * Save a hatting to the cloud.
     * This should be run in a thread.
     * @param name name to save under
     * @param view view we are getting the data from
     * @param user The user's username
     * @param password The user's password.
     * @param hint The drawing hint
     * @param solution The drawing solution
     * @param category The drawing category
     * @return true if successful
     */
    public boolean saveToCloud(DrawingView view, String user, String password, String hint, String solution, String category) {
        // Create an XML packet with the information about the current image
        XmlSerializer xml = Xml.newSerializer();
        StringWriter writer = new StringWriter();
        
        try {
            xml.setOutput(writer);
            
            xml.startDocument("UTF-8", true);
            
            xml.startTag(null, "proj02");
            xml.attribute(null, "magic", MAGIC);
            xml.attribute(null, "user", user);
            xml.attribute(null, "pw", password);
            
            // Drawing info
            xml.startTag(null, "proj02_drawing");
            xml.attribute(null, "hint", hint);
            xml.attribute(null, "solution", solution);
            xml.attribute(null, "category", category);
            xml.endTag(null, "proj02_drawing");
            view.saveXml(xml);
            
            xml.endTag(null, "proj02");
            
            xml.endDocument();

        } catch (IOException e) {
            // This won't occur when writing to a string
            return false;
        }
        
        final String xmlStr = writer.toString();
        
        /*
         * Convert the XML into HTTP POST data.
         */
        String postDataStr;
        try {
            postDataStr = "xml=" + URLEncoder.encode(xmlStr, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return false;
        }
        
        /*
         * Send the data to the server
         */
        byte[] postData = postDataStr.getBytes();
        
        InputStream stream = null;
        try {
            URL url = new URL(SAVE_URL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Length", Integer.toString(postData.length));
            conn.setUseCaches(false);

            OutputStream out = conn.getOutputStream();
            out.write(postData);
            out.close();

            int responseCode = conn.getResponseCode();
            if(responseCode != HttpURLConnection.HTTP_OK) {
                return false;
            } 
            
            stream = conn.getInputStream();
            
            /**
             * Create an XML parser for the result
             */
            try {
                XmlPullParser xmlR = Xml.newPullParser();
                xmlR.setInput(stream, UTF8);
                
                xmlR.nextTag();      // Advance to first tag
                xmlR.require(XmlPullParser.START_TAG, null, "proj02");
                
                String status = xmlR.getAttributeValue(null, "status");
                if(status.equals("no")) {
                    return false;
                }
                
                // We are done
            } catch(XmlPullParserException ex) {
                return false;
            } catch(IOException ex) {
                return false;
            }
            
        } catch (MalformedURLException e) {
            return false;
        } catch (IOException ex) {
            return false;
        } finally {
            if(stream != null) {
                try {
                    stream.close();
                } catch(IOException ex) {
                    // Fail silently
                }
            }
        }
        
        return true;
    }
    
    public static void logStream(InputStream stream) {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(stream));
    
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                Log.e("476", line);
            }
        } catch (IOException ex) {
            return;
        }
    }
    
    /**
     * Open a connection to a drawing in the cloud.
     * @param user User who is opening this drawing
     * @param password User's password
     * @return reference to an input stream or null if this fails
     */
    public InputStream openFromCloud(String user, String password) {
        // Create a get query
        String query = LOAD_URL + "?user=" + user + "&magic=" + MAGIC + "&pw=" + password;
        
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
    
    /**
     * Open a connection to a drawing in the cloud.
     * @param user The user who is guessing
     * @param password The user's password
     * @param drawingID The ID of the user guessing this drawing
     * @return reference to an input stream or null if this fails
     */
    public InputStream guessCloud(String user, String password, String drawingID ) {
        // Create a get query
        String query = GUESS_URL + "?user=" + user + "&magic=" + MAGIC + "&pw=" + password + "drawing_id" + drawingID;
        
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

