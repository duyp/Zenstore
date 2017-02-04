package com.zenstore.order;

import android.content.Context;
import android.util.Log;

public class BackgroundUpdater extends Thread{
	
	Context context;
	WSManager wsManager;
	
	public BackgroundUpdater(Context context, WSManager wsm) {
		this.context = context;
		this.wsManager = wsm;
	}
	
	@Override
	public void run() {
		while (!interrupted()){
			Log.d("thread", "BACKGROUND Updating from server...");
			wsManager.execute(WSManager.ACTION_UPDATE_BACKGROUND);
			try {
				Thread.sleep(Constants.TIME_UPDATE);
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
