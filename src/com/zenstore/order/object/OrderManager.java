package com.zenstore.order.object;

import java.util.ArrayList;

import com.zenstore.order.MyApplication;
import com.zenstore.order.WSManager;


import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

public class OrderManager {

	ArrayList<ZenOrder> newOrders;
	ArrayList<ZenOrder> waitOrders;
	ArrayList<ZenOrder> failOrders;
	ArrayList<ZenOrder> doneOrders;
	
	public OrderManager() {
		newOrders = new ArrayList<>();
		waitOrders = new ArrayList<>();
		failOrders = new ArrayList<>();
		doneOrders = new ArrayList<>();
	}
	
	public OrderManager(int status, ArrayList<ZenOrder> list) {
		set(status, list);
	}
	
//	public OrderManager(Parcel in) {
//		in.readList(orders, getClass().getClassLoader());
//	}
	
	public ArrayList<ZenOrder> getOrderList(int status) {
		if (status == WSManager.ACTION_UPDATE_NEW) {
			return newOrders;
		}
		else if (status == WSManager.ACTION_UPDATE_WAIT) {
			return waitOrders;
		}
		else if (status == WSManager.ACTION_UPDATE_DONE) {
			return doneOrders;
		}
		else if (status == WSManager.ACTION_UPDATE_FAIL) {
			return failOrders;
		}
		return null;
	}
	
	//@SuppressWarnings("unchecked")
	public void set(int status, ArrayList<ZenOrder> list) {
		getOrderList(status).clear();
		getOrderList(status).addAll(list);
	}
	
	public void addOrder(int status, ZenOrder obj) {
		getOrderList(status).add(obj);
	}
	
	public int getOrderCount(int status) {
		return getOrderList(status).size();
	}
	
	public void clear() {
		newOrders.clear();
		waitOrders.clear();
		doneOrders.clear();
		failOrders.clear();
	}
	
	public ZenOrder getOrder(int status, int index) {
		return getOrderList(status).get(index);
	}
	
//	public int getNewCount() {
//		int newCount = 0;
//		for(ZenOrder o:orders) {
//			if(o.status_id == ZenOrder.STATUS_WAIT) {
//				newCount++;
//			}
//		}
//		return newCount;
//	}
	
	public String[] getQuickView(int status) {
		int n = getOrderCount(status);
		String result[] = new String[n];
		ArrayList<ZenOrder> list = getOrderList(status);
		for(int i = 0; i < n; i++) {
			ZenOrder o = list.get(i); // http://zencase.tk/product/bao-da-zenfone-5/
			result[i] = "Don Hang: " + o.id + "\nSP: " + o.product_id + "\nKH: " + o.customer_id;
		}
		return result;
	}
	
	public String getQuickView(int status, int index) {
		ZenOrder o = getOrder(status, index);
		String line1 = "";
		String line2 = "";
		ZenProduct p;
		ZenCustomer c;
		if ((p=MyApplication.getProduct(o.product_id)) != null) {
			line1 = "SP: " + p.name;
		} else {
			line1 = "SP: " + o.product_id;
		}
		if ((c=MyApplication.getCustomer(o.customer_id)) != null) {
			line2 = "DC: " + c.address;
		} else {
			line2 = "Gia: " + o.price;
		}
		return line1 + "\n\n" + line2;
	}
	
//	@Override
//	public int describeContents() {
//		return 0;
//	}
//
//	@Override
//	public void writeToParcel(Parcel dest, int flags) {
//		dest.writeList(orders);
//	}
//	
//	public static final Parcelable.Creator<OrderManager> CREATOR = new Parcelable.Creator<OrderManager>() {
//	    public OrderManager createFromParcel(Parcel in) {
//	      return new OrderManager(in);
//	    }
//	    public OrderManager[] newArray(int size) {
//	      return new OrderManager[size];
//	    }
//	  };
}
