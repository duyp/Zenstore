package com.zenstore.order.ui;

import com.zenstore.order.R;
import com.zenstore.order.DataTools;
import com.zenstore.order.JSONParser;
import com.zenstore.order.MyApplication;
import com.zenstore.order.WSListener;
import com.zenstore.order.WSManager;
import com.zenstore.order.object.ZenCustomer;
import com.zenstore.order.object.ZenOrder;
import com.zenstore.order.object.ZenProduct;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class OrderActivity extends Activity implements WSListener{

	ZenOrder order;
	TextView t_id;
	TextView t_status;
	TextView t_name;
	TextView t_phone;
	TextView t_address;
	TextView t_product;
	TextView t_price;
	TextView t_time;
	TextView t_extra;
	
	WSManager wsManager;
	
	ZenCustomer customer;
	ZenProduct product;
	
	int status_updated = -1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order);

		try {
			Intent i = getIntent();
			order = (ZenOrder)i.getParcelableExtra("order");
			
			t_id = (TextView)findViewById(R.id.order_code);
			t_status = (TextView)findViewById(R.id.order_status);
			t_name = (TextView)findViewById(R.id.order_name);
			t_phone = (TextView)findViewById(R.id.order_phone);
			t_address = (TextView)findViewById(R.id.order_address);
			t_product = (TextView)findViewById(R.id.order_product);
			t_price = (TextView)findViewById(R.id.order_price);
			t_time = (TextView)findViewById(R.id.order_time);
			t_extra = (TextView)findViewById(R.id.order_extra);
			
			customer = null;
			product = null;
			
			wsManager = new WSManager(this);
			
			displayOrder();
			getProduct();
			getCustomer();
			
			t_phone.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					if (customer != null) {
						makeCall(customer.phone);
					}
				}
			});
			
			t_product.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					viewProduct();
				}
			});
			
			t_status.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					showStatusDialog();
				}
			});
		} catch(Exception e) {
			e.printStackTrace();
			Toast.makeText(this, "OrderActivity.onCreate(): " + e.getMessage(), Toast.LENGTH_SHORT).show();
		}
		
	}
	
	void displayOrder() {
		t_id.setText("Mã ĐH: " + order.id);
		t_status.setText("Tình trạng ĐH: " + ZenOrder.STATUS_NAME[order.status_id]);
		t_time.setText("Thời gian ĐH: " + order.time);
		t_price.setText(String.valueOf(order.price));
		t_extra.setText("Ghi Chu: " + order.extra);
	}
	
	void displayProduct() {
		t_product.setText(product.name);
	}
	
	void displayCustomer() {
		t_name.setText("KH: " + customer.name);
		t_phone.setText(customer.phone);
		t_address.setText("Địa chỉ: " + customer.address);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.order, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_order_update) {
			showStatusDialog();
			return true;
		} else if (id == R.id.action_order_delete){
			
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	void showStatusDialog() {
		new UpdateStatusDialog().show(getFragmentManager(), "status");
	}
	
	void updateStatus(int status) {
		wsManager.executeOrderUpdate(order.id, status);
	}
	
	void makeCall(String number) {
		Intent i = new Intent(Intent.ACTION_DIAL);
		i.setData(Uri.parse("tel:"+number));
		startActivity(i);
	}

	void getProduct() {
		//Toast.makeText(this, "Getting product...", Toast.LENGTH_SHORT).show();
		product = MyApplication.getProduct(order.product_id);
		if (product == null) {
			wsManager.executeGetProduct(order.product_id);
		} else {
			displayProduct();
		}
	}
	
	void getCustomer() {
		//Toast.makeText(this, "Getting customer...", Toast.LENGTH_SHORT).show();
		customer = MyApplication.getCustomer(order.customer_id);
		if (customer == null) {
			wsManager.executeGetCustomer(order.customer_id);
		} else {
			displayCustomer();
		}
	}
	
	void viewProduct() {
		if (product != null) {
			Intent productIntent = new Intent(OrderActivity.this, ProductActivity.class);
			productIntent.putExtra("product", product.toArray());
			OrderActivity.this.startActivity(productIntent);
		}
	}

	// writing....
	@Override
	public void onRespond(int code, String jsonData) {
		if (jsonData != null) {
			switch (code) {
			case WSManager.ACTION_GET_PRODUCT:
				product = JSONParser.JSONGetProduct(jsonData)[0];
				if(product!=null) {
					MyApplication.addProduct(product);
					DataTools.saveProduct(jsonData);
					displayProduct();
				}
				break;
			case WSManager.ACTION_GET_CUSTOMER:
				customer = JSONParser.JSONGetCustomer(jsonData)[0];
				if(customer!=null) {
					MyApplication.addCustomers(customer);
					DataTools.saveCustomer(jsonData);
					displayCustomer();
				}
				break;
			case WSManager.ACTION_ORDER_UPDATE_STATUS:
				Toast.makeText(this, jsonData, Toast.LENGTH_SHORT).show();
				if (status_updated != -1 && jsonData.equals(WSManager.MESSAGE_DONE)) {
					order.status_id = status_updated;
					t_status.setText("Tình trạng ĐH: " + ZenOrder.STATUS_NAME[order.status_id]);
				}
				break;
				default: break;
			}
		}
	}
	
	public class UpdateStatusDialog extends DialogFragment {
		
	    @Override
	    public Dialog onCreateDialog(Bundle savedInstanceState) {
	        // Use the Builder class for convenient dialog construction
	    	final String[] list = ZenOrder.getStatusList(order.status_id);
	        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	        builder.setTitle(R.string.menu_order_update)
	        		.setItems(list, new DialogInterface.OnClickListener() {
	                    public void onClick(DialogInterface dialog, int pos) {
	                    	int id = ZenOrder.getStatusId(list[pos]);
	                    	updateStatus(id);
	                    	status_updated = id;
	                    }
	               });
	        // Create the AlertDialog object and return it
	        return builder.create();
	    }
	}
}
