package com.zenstore.order;
//package com.zencase.order;
//import java.io.BufferedReader;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//
//import org.apache.http.HttpResponse;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.params.BasicHttpParams;
//import org.apache.http.params.HttpConnectionParams;
//import org.apache.http.params.HttpParams;
//
//import com.zencase.order.object.ZenOrder;
//
//import android.app.IntentService;
//import android.content.Intent;
//import android.os.AsyncTask;
//import android.os.Build;
//import android.support.v4.content.LocalBroadcastManager;
//import android.util.Log;
//import android.widget.Toast;
//
//
//public class UpdateService_Backup extends IntentService {
//	    public UpdateService_Backup() {
//			super("update_service");
//		}
//
//		@Override
//		protected void onHandleIntent(Intent workIntent) {
//			Log.d("service", "working...");
//			int code = workIntent.getIntExtra("code", WSManager.ACTION_UPDATE_ALL);
//			String dataResult = "";
//			if (code == WSManager.ACTION_UPDATE_ALL || code == WSManager.ACTION_UPDATE_NEW) {
//				dataResult = execute(code);
//			}
//			else if (code == WSManager.ACTION_UPDATE_BACKGROUND) {
//				dataResult= execute(WSManager.ACTION_UPDATE_NEW);
//			}
//			else if (code == WSManager.ACTION_GET_PRODUCT) {
//				String productCode = workIntent.getStringExtra("product_code");
//				dataResult = executeGetProduct(productCode);
//			}
//			else if (code == WSManager.ACTION_ORDER_UPDATE_STATUS) {
//				String orderCode = workIntent.getStringExtra("order_code");
//				int orderStatus = workIntent.getIntExtra("order_status", ZenOrder.STATUS_DONE);
//				dataResult = executeOrderUpdate(orderCode, orderStatus);
//			}
//			returnResult(code, dataResult);
//		}
//		
//		private void returnResult(int code, String dataResult) {
//			//Log.d("service", dataResult);
//			Intent localIntent = new Intent(Constants.ACTION_RESPOND)
//			.putExtra("data", dataResult)
//			.putExtra("code", code);
//			LocalBroadcastManager.getInstance(UpdateService_Backup.this).sendBroadcast(localIntent);
//		}
//		
//		public String execute(int code) {
//			if (code == WSManager.ACTION_UPDATE_ALL) {
//				return run(WSManager.URL_ALL_ORDERS);
//			} else if (code == WSManager.ACTION_UPDATE_NEW) {
//				return run(WSManager.URL_NEW_ORDERS);
//			}
//			return "";
//		}
//		
//		public String executeGetProduct(String productId) {
//			String url = WSManager.URL_GET_PRODUCT;
//			url += WSManager.PHP_PREFIX + WSManager.EXTEND_PRODUCT_ID + WSManager.PHP_AND + productId;
//			return run(url);
//		}
//		
//		public String executeOrderUpdate(String order_code, int status) {
//			String url = WSManager.URL_ORDER_UPDATE;
//			url += WSManager.PHP_PREFIX + WSManager.EXTEND_ORDER_CODE + WSManager.PHP_AND + order_code 
//					+ WSManager.PHP_AND + WSManager.EXTEND_ORDER_STATUS + WSManager.PHP_AND + status;
//			return run(url);
//		}
//		
//		public String run(String url) {
//			String response = "";
//			
//			HttpParams httpParameters = new BasicHttpParams();
//			HttpConnectionParams.setConnectionTimeout(httpParameters, Constants.TIME_OUT_CONNECTION);
//			HttpConnectionParams.setSoTimeout(httpParameters, Constants.TIME_OUT_SOCKET);
//			
//			DefaultHttpClient client = new DefaultHttpClient(httpParameters);
//			HttpGet httpGet = new HttpGet(url);
//			try {
//				HttpResponse execute = client.execute(httpGet);
//				InputStream content = execute.getEntity().getContent();
//				BufferedReader buffer = new BufferedReader(new InputStreamReader(
//						content));
//				String s = "";
//				while ((s = buffer.readLine()) != null) {
//					response += s;
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			return response;
//		}
//	}