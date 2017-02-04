package com.zenstore.order.ui;
//package com.zencase.order.ui;
//package com.zencase.order;
//
//import java.util.ArrayList;
//import java.util.Calendar;
//
//import android.app.Activity;
//import android.app.AlarmManager;
//import android.app.IntentService;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.graphics.Color;
//import android.media.RingtoneManager;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.SystemClock;
//import android.support.v4.app.NotificationCompat;
//import android.support.v4.content.LocalBroadcastManager;
//import android.util.Log;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ListView;
//import android.widget.Toast;
//
//public class ZenActivity1 extends Activity {
//
//	WSManager wsManager;
//	WSListener listener;
//	
//	OrderManager orderManager;
//	
//	ListView ordersListview;
//	OrdersListAdapter adapter;
//	
//	boolean paused = false;
//	boolean pausedTemp = false;
//	
//	Thread checker;
//	
//	ServiceReceiver receiver;
//	
//	private class ServiceReceiver extends BroadcastReceiver {
//
//		@Override
//		public void onReceive(Context c, Intent i) {
//			String jsonData = i.getStringExtra("data");
//			Log.d("service", "received : " + jsonData.substring(0, jsonData.length() < 50 ? jsonData.length() : 50) + "...");
//			int code = i.getIntExtra("code", WSManager.CODE_GET_PRODUCT);
//			//Log.d("service", "CODE: " + code);
//			onServiceRespond(code, jsonData);
//		}
//	}
//	
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_zen);
//		Log.d("activity", "creating...");
//		
//		receiver = new ServiceReceiver();
//		IntentFilter filter = new IntentFilter(Constants.ACTION_RESPOND);
//		LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter);
//		
//		ordersListview = (ListView) findViewById(R.id.listview);
//		ordersListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
//				listViewItemClicked(position);
//			}
//		});
//		
//		orderManager = new OrderManager();
//		
//		if (!loadOrderData()) {
//			update(WSManager.CODE_UPDATE_ALL);
//		} else {
//			update(WSManager.CODE_UPDATE_NEW);
//		}
//		
//		if (!MyApplication.isStarted()) {
//			MyApplication.applicationStarted();
//			runBackgroundChecker();
//			Log.d("app", "application is starting");
//		} else {
//			Log.d("app", "application already started");
//		}
//	}
//
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		getMenuInflater().inflate(R.menu.zen, menu);
//		return true;
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		int id = item.getItemId();
//		if (id == R.id.action_settings) {
//			update(WSManager.CODE_UPDATE_ALL);
//			return true;
//		}
//		return super.onOptionsItemSelected(item);
//	}
//	
//	@Override
//	public void onSaveInstanceState(Bundle savedInstanceState) {
//		savedInstanceState.putParcelable("order_manager", orderManager);
//	    super.onSaveInstanceState(savedInstanceState);
//	}
//	
//	public void onRestoreInstanceState(Bundle savedInstanceState) {
//	    super.onRestoreInstanceState(savedInstanceState);
//		orderManager = savedInstanceState.getParcelable("order_manager");
//		//displayData();
//	}
//
//	void runBackgroundChecker() {
//		Thread checker = new Thread() {
//			@Override
//			public void run() {
//				while (true){
//				try {
//					Thread.sleep(Constants.TIME_UPDATE);
//				} catch(InterruptedException e) {
//					e.printStackTrace();
//				}
//				Log.d("thread", "BACKGROUND Updating from server...");
//				Intent serviceIntent = new Intent(ZenActivity.this, UpdateService.class);
//				serviceIntent.putExtra("code", WSManager.CODE_UPDATE_BACKGROUND);
//				ZenActivity.this.startService(serviceIntent);
//				}
//			}
//		};
//		checker.start();
//		
//		
////		Intent serviceIntent = new Intent(this, UpdateService.class);
////		serviceIntent.putExtra("code", WSManager.CODE_UPDATE_BACKGROUND);
////		
////		PendingIntent aIntent = PendingIntent.getService(this, 0, serviceIntent, 0);
////		
////		AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
////		am.setRepeating(AlarmManager.RTC, System.currentTimeMillis() + TIME_UPDATE, TIME_UPDATE, aIntent);
//		
//		Log.d("service", "checker started!");
//	}
//	
//	@Override
//	protected void onPause() {
//		super.onPause();
//		MyApplication.activityPaused();
//	}
//	
//	@Override
//	protected void onResume() {	
//		super.onResume();
//		MyApplication.activityResumed();
//	}
//	
//	@Override
//	protected void onStop() {
//		Log.d("activity", "stopping...");
//		super.onStop();
//	}
//	
//	@Override
//	protected void onRestart() {
//		Log.d("activity", "restarting...");
//		super.onStart();
//	}
//	
//	@Override
//	protected void onDestroy() {
//		Log.d("activity", "destroying...");
//		LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
//		super.onDestroy();
//	}
//	
//	void update(int code) {
//		Toast.makeText(this, "Updating from server..." + code, Toast.LENGTH_SHORT).show();
//		Log.d("activity", "Updating from server..." + code);
//		
//		Intent serviceIntent = new Intent(this, UpdateService.class);
//		serviceIntent.putExtra("code", code);
//		startService(serviceIntent);
//	}
//	
//	boolean loadOrderData() {
//		String jsonData = DataTools.loadData();
//		if (jsonData != null) {
//			Toast.makeText(this, "Loading data from storage...", Toast.LENGTH_SHORT).show();
//			ArrayList<Order> orders = JSONParser.JSONGetOrders(jsonData);
//			orderManager.set(orders);
//			Log.d("data", "Loading data from storage... size = " + orderManager.getOrderCount());
//			displayData();
//			return true;
//		} else {
//			return false;
//		}
//	}
//	
//	void onServiceRespond(int code, String jsonData) {
//		ArrayList<Order> orders = JSONParser.JSONGetOrders(jsonData);
//		
//		//Log.d("data", "ORDERS SIZE: "+orderManager.getOrderCount());
//		if (code == WSManager.CODE_UPDATE_ALL) {
//			orderManager.set(orders);
//			displayData();
//			Toast.makeText(this, "Saving data to storage...", Toast.LENGTH_SHORT).show();
//			DataTools.saveData(jsonData);
//		} else if (code == WSManager.CODE_UPDATE_NEW) {
//			newOrdersUpdated(orders);
//		} else if (code == WSManager.CODE_UPDATE_BACKGROUND) {
//			backgroundUpdated(orders);
//		}
//	}
//	
//	void displayData() {
//		if (orderManager != null){
//			adapter = new OrdersListAdapter(this, orderManager.getQuickView(), orderManager);
//			ordersListview.setAdapter(adapter);
//		}
//	}
//
//	void newOrdersUpdated(ArrayList<Order> newOrders) {
//		Log.d("info", "UPDATED - new size: "+ newOrders.size() + " current new size: " + orderManager.getNewCount());
//		if (newOrders.size() > orderManager.getNewCount()) {
//			update(WSManager.CODE_UPDATE_ALL);
//		}
//		newOrders.clear();
//	}
//	
//	void backgroundUpdated(ArrayList<Order> newOrders) {
//		if (newOrders.size() > orderManager.getNewCount()) {
////			if (MyApplication.isActivityVisible()){
//				update(WSManager.CODE_UPDATE_ALL);
////			} else {
////				Log.d("info", "notifying (new size: "+ newOrders.size() + "new from data: " + orderManager.getNewCount());
//				notifyNewOrders(newOrders);
////			}
//		}
//	}
//	
//	void notifyNewOrders(ArrayList<Order> orders) {
//		String content = "" + orders.size() + " new orders: \n";
//		for (Order o : orders) {
//			content += o.product + " - " + o.address + " | ";
//		}
//		
//		Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
//		
//		Intent intent = new Intent(this, ZenActivity.class);
//		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//		PendingIntent pIntent =
//	            PendingIntent.getActivity(
//	            this,
//	            0,
//	            intent,
//	            PendingIntent.FLAG_UPDATE_CURRENT
//	        );
//
//		NotificationCompat.Builder mBuilder =
//			    new NotificationCompat.Builder(this)
//			    .setSmallIcon(R.drawable.ic_launcher)
//			    .setContentTitle("ZENCASE - new orders")
//			    .setContentText(content)
//			    .setSound(soundUri)
//			    .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
//			    .setLights(Color.RED, 1000, 1000)
//			    .setAutoCancel(true);
//		
//		mBuilder.setContentIntent(pIntent);
//		
//		NotificationManager mNotifyMgr = 
//		        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//		mNotifyMgr.notify(001, mBuilder.build());
//	}
//	
//	void listViewItemClicked(int pos) {
//		int i = orderManager.getOrderCount() - pos - 1;
//		Order o = orderManager.getOrder(i);
//		Intent myIntent = new Intent(ZenActivity.this, OrderActivity.class);
//		myIntent.putExtra("order", o);
//		ZenActivity.this.startActivity(myIntent);
//		pausedTemp = true;
//	}
//	
//}
