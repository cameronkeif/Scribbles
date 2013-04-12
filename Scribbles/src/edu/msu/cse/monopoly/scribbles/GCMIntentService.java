package edu.msu.cse.monopoly.scribbles;

import android.content.Context;
import android.content.Intent;

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
	}	
	
	@Override
	protected void onRegistered(Context context, String regId) {
		
	}
	
	@Override
	protected void onUnregistered(Context context, String regId) {
	}

}
