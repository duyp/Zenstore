package com.zenstore.order;

import com.zenstore.order.object.CustomerManager;
import com.zenstore.order.object.ProductManager;
import com.zenstore.order.object.ZenCustomer;
import com.zenstore.order.object.ZenProduct;

import android.app.Application;
import android.content.Context;
import android.util.Log;

public class MyApplication extends Application {
	
	static ProductManager pm = new ProductManager();
	static CustomerManager cm = new CustomerManager();
	
	//////////////////// Product cache //////////////////////////
	public static ZenProduct getProduct(String id) {
		return pm.getProduct(id);
	}
	
	public static void addProduct(ZenProduct p) {
		pm.addProduct(p);
	}
	
	public static void loadProduct() {
		String jsonData = DataTools.loadProducts();
		if (jsonData != null) {
			ZenProduct[] ps = JSONParser.JSONGetProduct(jsonData);
			if (ps != null && ps.length > 0) {
				pm.clear();
				pm.addProducts(ps);
			}
		}
	}
	
	///////////////////// Customer catche //////////////////////////////
	public static ZenCustomer getCustomer(String id) {
		return cm.getCustomer(id);
	}
	
	public static void addCustomers(ZenCustomer c) {
		cm.addCustomer(c);
	}
	
	public static void loadCustomer() {
		String jsonData = DataTools.loadCustomers();
		if (jsonData != null) {
			ZenCustomer[] cs = JSONParser.JSONGetCustomer(jsonData);
			if (cs != null && cs.length > 0) {
				cm.clear();
				cm.addCustomers(cs);
			}
		}
	}
	
	public static boolean isStarted() {
		return applicationStarted;
	}
	
	public static void applicationStarted() {
		applicationStarted = true;
	}
	
	  public static boolean isActivityVisible() {
	    return activityVisible;
	  }  

	  public static void activityResumed() {
	    activityVisible = true;
	  }

	  public static void activityPaused() {
	    activityVisible = false;
	  }

	  private static boolean activityVisible;
	  private static boolean applicationStarted = false;
}