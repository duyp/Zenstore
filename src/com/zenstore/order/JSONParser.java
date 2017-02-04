package com.zenstore.order;

import java.text.ParseException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.zenstore.order.object.ZenCustomer;
import com.zenstore.order.object.ZenOrder;
import com.zenstore.order.object.ZenProduct;

import android.util.Log;

public class JSONParser {
	
	public static final String ARRAY_NAME = "data";
	
	// order
	public static final String TAG_ORDER_ID = "order_id";
	public static final String TAG_ORDER_STATUS_ID = "status_id";
	public static final String TAG_ORDER_PRODUCT_ID = "product_id";
	public static final String TAG_ORDER_PRICE = "price";
	public static final String TAG_ORDER_CUSTOMER_ID = "customer_id";
	public static final String TAG_ORDER_TIME = "time";
	public static final String TAG_ORDER_EXTRA = "extra_info";
	
	// customer
	public static final String TAG_CUSTOMER_ID = "id";
	public static final String TAG_CUSTOMER_NAME = "name";
	public static final String TAG_CUSTOMER_PHONE = "phone";
	public static final String TAG_CUSTOMER_ADDRESS = "address";
	public static final String TAG_CUSTOMER_EMAIL = "email";
	
	// product
	public static final String TAG_PRODUCT_ID = "id";
	public static final String TAG_PRODUCT_NAME = "title";
	public static final String TAG_PRODUCT_PRICE = "price";
	public static final String TAG_PRODUCT_LINK = "permalink";
	
	public static final String TAG_UNKNOWN = "unknown";
	
	public static ArrayList<ZenOrder> JSONGetOrders(String jsonData) {
		//Log.d("parser", jsonData);
		ArrayList<ZenOrder> result = new ArrayList<ZenOrder>();
		JSONObject mainObject;
		try {
			mainObject = new JSONObject(jsonData);
			if (mainObject != null) {
				JSONArray jsonArr = mainObject.getJSONArray(ARRAY_NAME);
				for(int i = 0; i < jsonArr.length(); i++) {
					JSONObject obj = jsonArr.getJSONObject(i);
					ZenOrder o = new ZenOrder();
					o.id = obj.getString(TAG_ORDER_ID);
					o.status_id = Integer.valueOf(obj.getString(TAG_ORDER_STATUS_ID));
					o.customer_id = obj.getString(TAG_ORDER_CUSTOMER_ID);
					o.product_id = obj.getString(TAG_ORDER_PRODUCT_ID);
					o.price = Integer.valueOf(obj.getString(TAG_ORDER_PRICE));
					o.time = obj.getString(TAG_ORDER_TIME);
					o.extra = obj.getString(TAG_ORDER_EXTRA);
					result.add(o);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	public static ZenProduct[] JSONGetProduct(String jsonData) {
		Log.d("parser", jsonData);
		JSONObject mainObject;
		try {
			mainObject = new JSONObject(jsonData);
			JSONArray jsonArr = mainObject.getJSONArray(ARRAY_NAME);
			
			ZenProduct[] result = new ZenProduct[jsonArr.length()];
			for(int i = 0; i < jsonArr.length(); i++) {
				result[i] = new ZenProduct();
				JSONObject obj = jsonArr.getJSONObject(i);
				result[i].id = obj.getString(TAG_PRODUCT_ID);
				result[i].name = obj.getString(TAG_PRODUCT_NAME);
				result[i].link = obj.getString(TAG_PRODUCT_LINK);
				String price = obj.getString(TAG_PRODUCT_PRICE);
				result[i].price = Integer.valueOf(price.substring(0,price.length() - 3));
			}
			return result;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static ZenCustomer[] JSONGetCustomer(String jsonData) {
		Log.d("parser", jsonData);
		JSONObject mainObject;
		try {
			mainObject = new JSONObject(jsonData);
			JSONArray jsonArr = mainObject.getJSONArray(ARRAY_NAME);
			
			ZenCustomer[] result = new ZenCustomer[jsonArr.length()];
			for(int i = 0; i < jsonArr.length(); i++) {
				result[i] = new ZenCustomer();
				JSONObject obj = jsonArr.getJSONObject(i);
				result[i].id = obj.getString(TAG_CUSTOMER_ID);
				result[i].name = obj.getString(TAG_CUSTOMER_NAME);
				result[i].phone = obj.getString(TAG_CUSTOMER_PHONE);
				result[i].address = obj.getString(TAG_CUSTOMER_ADDRESS);
				//result.email = obj.getString(TAG_CUSTOMER_EMAIL);
			}
			return result;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String insertJsonData(String currentJsonData, String newJsonData) {
		newJsonData = newJsonData.substring(9, newJsonData.length() - 2);
		String first = currentJsonData.substring(0, 9);
		String last = currentJsonData.substring(9, currentJsonData.length());
		return first + newJsonData + "," + last;
	}
}
