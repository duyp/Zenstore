package com.zenstore.order.ui;

import java.util.ArrayList;

import com.zenstore.order.R;
import com.zenstore.order.Constants;
import com.zenstore.order.DataTools;
import com.zenstore.order.JSONParser;
import com.zenstore.order.MyApplication;
import com.zenstore.order.OrdersListAdapter;
import com.zenstore.order.UpdateService;
import com.zenstore.order.WSListener;
import com.zenstore.order.WSManager;
import com.zenstore.order.ZenNotification;
import com.zenstore.order.object.OrderManager;
import com.zenstore.order.object.ZenOrder;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class ZenActivity extends Activity implements WSListener {

	WSManager wsManager;
	WSListener listener;
	OrderManager orderManager;
	
	ListView ordersListview;
	OrdersListAdapter adapter;
	
	Thread updater;
	
	int currentStatus;
	
	private class ServiceReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context c, Intent i) {
			String jsonData = i.getStringExtra("data");
			Log.d("service", "received : " 
					+ jsonData.substring(0, jsonData.length() < 50 ? jsonData.length() : 50) + "...");
			int code = i.getIntExtra("code", WSManager.ACTION_UPDATE_BACKGROUND);
			//Log.d("service", "CODE: " + code);
			onRespond(code, jsonData);
		}
	}
	
	ServiceReceiver receiver;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_zen);
		
		Log.d("activity", "creating...");
		
		orderManager = new OrderManager();
		wsManager = new WSManager(this);
		currentStatus = WSManager.ACTION_UPDATE_NEW;
		
		ordersListview = (ListView) findViewById(R.id.listview);
		ordersListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				listViewItemClicked(position);
			}
		});
		
		if (!MyApplication.isStarted()) {
			MyApplication.applicationStarted();
			MyApplication.loadCustomer();
			MyApplication.loadProduct();
			registerReceiver();
			startBackgroundService();
			Log.d("app", "application is starting");
		} else {
			Log.d("app", "application already started");
		}
		getOrder(WSManager.ACTION_UPDATE_NEW);
	}
	
	private void registerReceiver () {
		receiver = new ServiceReceiver();
		IntentFilter filter = new IntentFilter(Constants.ACTION_RESPOND);
		LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter);
	}
	
	private void startBackgroundService() {
		Intent serviceIntent = new Intent(this, UpdateService.class);
		serviceIntent.putExtra("code", WSManager.ACTION_UPDATE_BACKGROUND);
		
		PendingIntent aIntent = PendingIntent.getService(this, 0, serviceIntent, 0);
		
		AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
		am.setRepeating(AlarmManager.RTC, System.currentTimeMillis(), Constants.TIME_UPDATE, aIntent);
	}
	
	void update(int code) {	
		//if (code == WSManager.ACTION_UPDATE_ALL)
		Toast.makeText(this, "Updating..." + code, Toast.LENGTH_SHORT).show();
		wsManager.execute(code);
		
	}
	
	void getOrder(int code) {
		if (orderManager != null) {
			if (orderManager.getOrderCount(code) > 0) {
				displayData(code);
			}
			else if (loadOrderData(code)) {
				displayData(code);
			}
			else { 
				update(code);
			}
		}
	}
	
	@Override
	public void onRespond(int code, String jsonData) {
		if (jsonData != null) {
			Log.d("service", "SERVER RESPONED ! Code = " + code + ", data = " 
					+ jsonData.substring(0,jsonData.length() < 50 ? jsonData.length() : 50) + "....");
			ArrayList<ZenOrder> orders = JSONParser.JSONGetOrders(jsonData);
			
			if (code == WSManager.ACTION_UPDATE_BACKGROUND) {
				// if new order appear
				int current = orderManager.getOrderCount(WSManager.ACTION_UPDATE_NEW);
				if (orders.size() != current) {
					orderManager.set(WSManager.ACTION_UPDATE_NEW, orders);
					DataTools.saveData(WSManager.ACTION_UPDATE_NEW, jsonData);
					displayData(WSManager.ACTION_UPDATE_NEW);
					// notify
					if (orders.size() > current) 
						notifyNewOrders(orders);
				}
			} else {
				orderManager.set(code, orders);
				DataTools.saveData(code, jsonData);
				displayData(code);
			}
		}
	}
	
	void displayData(int code) {
		if (orderManager != null){
			adapter = new OrdersListAdapter(this, orderManager, code);
			ordersListview.setAdapter(adapter);
		}
	}
	
	void notifyNewOrders(ArrayList<ZenOrder> orders) {
		ZenNotification.notifyNewOrders(this, orders);
	}
	
	void listViewItemClicked(int pos) {
		try {
			ZenOrder o = orderManager.getOrder(currentStatus, pos);
			Intent myIntent = new Intent(ZenActivity.this, OrderActivity.class);
			myIntent.putExtra("order", o);
			ZenActivity.this.startActivity(myIntent);
		} catch(Exception e) {
			e.printStackTrace();
			Toast.makeText(this,"ZenActivity.initOrderActivity(): " + e.getMessage(), Toast.LENGTH_SHORT).show();
		}
	}
	
	boolean loadOrderData(int code) {
		String jsonData = DataTools.loadData(code);
		Toast.makeText(this, "Loading data from storage...", Toast.LENGTH_SHORT).show();
		if (jsonData != null) {
			ArrayList<ZenOrder> orders = JSONParser.JSONGetOrders(jsonData);
			orderManager.set(code, orders);
			Log.d("data", "Loading data from storage... size = " + orderManager.getOrderCount(code));
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MyApplication.activityPaused();
	}
	
	@Override
	protected void onResume() {	
		super.onResume();
		MyApplication.activityResumed();
	}
	
	@Override
	protected void onStop() {
		Log.d("activity", "stopping...");
		super.onStop();
	}
	
	@Override
	protected void onRestart() {
		Log.d("activity", "restarting...");
		super.onStart();
	}
	
	@Override
	protected void onDestroy() {
		Log.d("activity", "destroying...");
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.zen, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.menu_zen_update) {
			update(currentStatus);
		} else if (id == R.id.menu_zen_new) {
			getOrder(WSManager.ACTION_UPDATE_NEW);
			currentStatus = WSManager.ACTION_UPDATE_NEW;
			return true;
		} else if (id == R.id.menu_zen_wait) {
			getOrder(WSManager.ACTION_UPDATE_WAIT);
			currentStatus = WSManager.ACTION_UPDATE_WAIT;
			return true;
		} else if (id == R.id.menu_zen_done) {
			getOrder(WSManager.ACTION_UPDATE_DONE);
			currentStatus = WSManager.ACTION_UPDATE_DONE;
			return true;
		} else if (id == R.id.menu_zen_fail) {
			getOrder(WSManager.ACTION_UPDATE_FAIL);
			currentStatus = WSManager.ACTION_UPDATE_FAIL;
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
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
	
}
