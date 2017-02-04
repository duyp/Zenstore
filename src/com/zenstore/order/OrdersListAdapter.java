package com.zenstore.order;

import java.util.ArrayList;

import com.zenstore.order.R;
import com.zenstore.order.object.OrderManager;
import com.zenstore.order.object.ZenOrder;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class OrdersListAdapter extends ArrayAdapter<String> {
	
	private final Context context;
	private final OrderManager oManager;
	private final int code;

	public OrdersListAdapter(Context context, OrderManager om, int code) {
		super(context, R.layout.row_layout, om.getQuickView(code));
		this.oManager = om;
		this.context = context;
		this.code = code;
		//Log.d("Adapter", "len: " + this.values.length + " - values[0] : "+values[0]);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		View rowView = inflater.inflate(R.layout.row_layout, parent, false);
		
		TextView textView = (TextView) rowView.findViewById(R.id.row_label);
		TextView iconView = (TextView) rowView.findViewById(R.id.row_icon);
		
		textView.setBackgroundColor(position%2 == 0?Color.WHITE : Color.LTGRAY);
		iconView.setBackgroundColor(position%2 == 1?Color.WHITE : Color.LTGRAY);
		
		textView.setText(oManager.getQuickView(code, position));
		
		int status = oManager.getOrder(code, position).status_id;
		//Log.d("list", "order size: " + orderManager.getOrderCount() + " position: " + position);
		
		switch(status) {
		case ZenOrder.STATUS_NEW: 
			iconView.setText("NEW");
			iconView.setTextColor(Color.RED);
			break;
		case ZenOrder.STATUS_WAIT: 
			iconView.setText("WAIT");
			iconView.setTextColor(Color.RED);
			break;
		case ZenOrder.STATUS_CANCEL: 
			iconView.setText("CANCELED");
			break;
		case ZenOrder.STATUS_DONE: 
			iconView.setText("DONE");
			break;
		case ZenOrder.STATUS_FAIL: 
			iconView.setText("FAIL");
			break;
		};
		return rowView;
	}
}
