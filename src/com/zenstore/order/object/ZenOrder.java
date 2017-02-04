package com.zenstore.order.object;

import java.text.ParseException;
import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

public class ZenOrder implements Parcelable {
	
	public static final int STATUS_NEW = 0; // don hang moi dat
	public static final int STATUS_WAIT = 1; // cho giao hang
	public static final int STATUS_DONE = 2; // da giao
	public static final int STATUS_CANCEL = 3; // don hang bi huy
	public static final int STATUS_FAIL = 4; // da giao, khong mua
	
	public static final String[] STATUS_NAME = {"Moi", "Cho giao", "Da giao", "Huy", "Hong"};

	public String id;
	public int status_id;
	public String customer_id;
	public String product_id;
	public int price;
	public String time;
	public String extra;

	public ZenOrder() {
		id = "";
		status_id = -1;
		customer_id = "";
		product_id = "";
		price = 0;
		time = "";
		extra = "";
	}

	public ZenOrder(String id, int status, String customer_id, String product_id, 
			int product_price, String time, String extra) {
		this.id = id;
		this.status_id = status;
		this.customer_id = customer_id;
		this.product_id = product_id;
		this.price = product_price;
		this.time = time;
		this.extra = extra;
	}

	public ZenOrder(Parcel in) {
		String[] dataIn = new String[7];
		in.readStringArray(dataIn);
		
		id = dataIn[0];
		status_id = Integer.valueOf(dataIn[1]);
		customer_id = dataIn[2];
		product_id = dataIn[3];
		price = Integer.valueOf(dataIn[4]);
		time = dataIn[5];
		extra = dataIn[6];
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeStringArray(new String[] { 
				this.id,
				String.valueOf(this.status_id), 
				String.valueOf(this.customer_id), 
				this.product_id,
				String.valueOf(this.price), 
				this.time, 
				this.extra,
		});
	}

	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public ZenOrder createFromParcel(Parcel in) {
            return new ZenOrder(in); 
        }

        public ZenOrder[] newArray(int size) {
            return new ZenOrder[size];
        }
    };
    
    public static int getStatusId(String name) {
    	for(int i = 0; i < STATUS_NAME.length; i++) {
    		if (name.equals(STATUS_NAME[i])) {
    			return i;
    		}
    	}
    	return -1;
    }
    
    public static String[] getStatusList(int excludeId) {
    	String[] result = new String[STATUS_NAME.length - 1];
    	int j = 0;
    	for(int i = 0; i < STATUS_NAME.length; i++) {
    		if (i != excludeId) {
    			result[j] = STATUS_NAME[i];
    			j++;
    		}
    	}
    	return result;
    }
}
