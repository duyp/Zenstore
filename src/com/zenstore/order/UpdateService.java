package com.zenstore.order;
import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

public class UpdateService extends IntentService {
	    public UpdateService() {
			super("update_service");
		}
	    
		@Override
		protected void onHandleIntent(Intent workIntent) {
			int code = workIntent.getIntExtra("code", WSManager.ACTION_UPDATE_BACKGROUND);
			Log.d("service", "working...CODE = " + code);
			//Toast.makeText(this, "Background - updating...", Toast.LENGTH_SHORT).show();
			String dataResult = "";
			if (code == WSManager.ACTION_UPDATE_BACKGROUND) {
				dataResult = WSManager.getData(WSManager.URL_NEW_ORDERS);
				if (dataResult != null) {
					returnResult(code, dataResult);
				} else {
					Log.d("service", "Background updater: null result !");
				}
			}
		}
		
		private void returnResult(int code, String dataResult) {
			//Log.d("service", dataResult);
			Intent localIntent = new Intent(Constants.ACTION_RESPOND)
			.putExtra("data", dataResult)
			.putExtra("code", code);
			LocalBroadcastManager.getInstance(UpdateService.this).sendBroadcast(localIntent);
		}
		
	}