package com.zenstore.order;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

public class WSManager {
	
	public static final int ACTION_UPDATE_ALL = 0;
	public static final int ACTION_UPDATE_NEW = 1;
	public static final int ACTION_UPDATE_WAIT = 2;
	public static final int ACTION_UPDATE_DONE = 3;
	public static final int ACTION_UPDATE_FAIL = 4;
	public static final int ACTION_UPDATE_BACKGROUND = 5;
	
	public static final int ACTION_GET_CUSTOMER = 10;
	public static final int ACTION_GET_PRODUCT = 11;
	
	public static final int ACTION_ORDER_UPDATE_STATUS = 20;
	
	
	static final String URL_ALL_ORDERS = "http://zenstore.vn/zen_get_orders.php?status=0,1,2,3,4";
	static final String URL_NEW_ORDERS = "http://zenstore.vn/zen_get_orders.php?status=0";
	static final String URL_WAIT_ORDERS = "http://zenstore.vn/zen_get_orders.php?status=1";
	static final String URL_DONE_ORDERS = "http://zenstore.vn/zen_get_orders.php?status=2";
	static final String URL_FAIL_ORDERS = "http://zenstore.vn/zen_get_orders.php?status=3,4";
	
	static final String URL_GET_CUSTOMER = "http://zenstore.vn/zen_get_customer.php";
	static final String URL_GET_PRODUCT = "http://zenstore.vn/zen_get_product_info.php";
	static final String URL_ORDER_UPDATE = "http://zenstore.vn/zen_update_order_status.php";
	
	static final String PHP_PREFIX = "?";
	static final String PHP_EQUAL = "=";
	static final String PHP_AND = "&";
	
	static final String EXTEND_CUSTOMER_ID = "id";
	static final String EXTEND_PRODUCT_ID = "product_id";
	static final String EXTEND_ORDER_STATUS = "status_id";
	static final String EXTEND_ORDER_ID = "order_id";
	
	public static final String MESSAGE_DONE = "OK";
	
	//Context context;
	WSListener wsListener;
	
	public WSManager(WSListener wsl){
		wsListener = wsl;
		//context = c;
	}
	
	public void execute(int code) {
		if (code == ACTION_UPDATE_ALL) {
			run(ACTION_UPDATE_ALL, URL_ALL_ORDERS);
		} 
		else if (code == ACTION_UPDATE_NEW || code == ACTION_UPDATE_BACKGROUND) {
			run(ACTION_UPDATE_NEW, URL_NEW_ORDERS);
		} 
		else if (code == ACTION_UPDATE_WAIT) {
			run(ACTION_UPDATE_WAIT, URL_WAIT_ORDERS);
		} 
		else if (code == ACTION_UPDATE_DONE) {
			run(ACTION_UPDATE_DONE, URL_DONE_ORDERS);
		} 
		else if (code == ACTION_UPDATE_FAIL) {
			run(ACTION_UPDATE_FAIL, URL_FAIL_ORDERS);
		}
	}
	
	public void executeGetProduct(String id) {
		String url = URL_GET_PRODUCT;
		url += PHP_PREFIX + EXTEND_PRODUCT_ID + PHP_EQUAL + id;
		run(ACTION_GET_PRODUCT, url);
	}
	
	public void executeGetCustomer(String id) {
		String url = URL_GET_CUSTOMER;
		url += PHP_PREFIX + EXTEND_CUSTOMER_ID + PHP_EQUAL + id;
		run(ACTION_GET_CUSTOMER, url);
	}
	
	public void executeOrderUpdate(String order_id, int status) {
		String url = WSManager.URL_ORDER_UPDATE;
		url += PHP_PREFIX + EXTEND_ORDER_ID + PHP_EQUAL + order_id 
				+ PHP_AND + EXTEND_ORDER_STATUS + PHP_EQUAL + status;
		run(ACTION_ORDER_UPDATE_STATUS, url);
	}
	
	public void run(int code, String url) {
		ServiceRunner runner = new ServiceRunner(code, url);
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			runner.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		} else {
			runner.execute();
		}
	}
	
	private void returnResult(int code, String dataResult) {
		wsListener.onRespond(code, dataResult);
	}
	
	public static String getData(String url){
		Log.d("wsservice","getting " + url);
		String response = "";
		
		HttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters, Constants.TIME_OUT_CONNECTION);
		HttpConnectionParams.setSoTimeout(httpParameters, Constants.TIME_OUT_SOCKET);
		
		DefaultHttpClient client = new DefaultHttpClient(httpParameters);
		HttpGet httpGet = new HttpGet(url);
		try {
			HttpResponse execute = client.execute(httpGet);
			InputStream content = execute.getEntity().getContent();
			BufferedReader buffer = new BufferedReader(new InputStreamReader(
					content));
			String s = "";
			while ((s = buffer.readLine()) != null) {
				response += s;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return response;
	}
	
	private class ServiceRunner extends AsyncTask<String, String, String>{
		
		String url;
		int code;
		
		public ServiceRunner(int code, String url) {
			this.url = url;
			this.code = code;
		}
		
		@Override
	    protected String doInBackground(String... params) {
			return getData(url);
		}
		
		@Override
	    protected void onPostExecute(String result) {
			returnResult(code, result);
	    }
	}

}
