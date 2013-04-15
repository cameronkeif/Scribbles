package edu.msu.cse.monopoly.scribbles;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService{
	public static final String SENDER_ID = "80901488701";
	
	
	
	public GCMIntentService(){
		super(SENDER_ID);
	}
	
	public GCMIntentService(String... senderIds) {
		super(senderIds);
		}
	
	@Override
	protected void onError(Context context, String errorId){
	}
	
	@Override
	protected void onMessage(Context context, Intent message){
		String msg = message.getStringExtra("message");
		if(!(msg.equals("new_drawing"))) {
			break;
		}
		Intent intent = new Intent(this, Leaderboard.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
		
		NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
		builder.setSmallIcon(R.drawable.ic_launcher);
		builder.setContentTitle(context.getString(R.string.app_name));
		builder.setContentText(context.getString(R.string.notification_request));
		builder.setAutoCancel(true);
		builder.setContentIntent(pendingIntent);
		
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		
		mNotificationManager.notify(0, builder.build());
	}	
	
	@Override
	protected void onRegistered(Context context, String regId) {
		
	}
	
	@Override
	protected void onUnregistered(Context context, String regId) {
	}

}
