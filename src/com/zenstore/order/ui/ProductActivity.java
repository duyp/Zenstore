package com.zenstore.order.ui;

import com.zenstore.order.R;
import com.zenstore.order.DataTools;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

public class ProductActivity extends Activity {

	String[] product;
	
	TextView t_code;
	TextView t_link;
	TextView t_name;
	TextView t_price;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product);
		
		Intent i = getIntent();
		product = i.getStringArrayExtra("product");

		t_code = (TextView)findViewById(R.id.product_code);
		t_name = (TextView)findViewById(R.id.product_title);
		t_price = (TextView)findViewById(R.id.product_price);
		t_link = (TextView)findViewById(R.id.product_link);
		
		if (product != null) {
			t_code.setText("Mã SP " + product[0]);
			t_name.setText(product[1]);
			t_link.setText(product[3]);
			t_price.setText("Giá " + DataTools.getPriceFormat(product[2]));
//			t_count.setText(product[4] + " sản phẩm trong kho");
//			t_promotion.setText("Khuyến mãi: " + product[5]);
		}
		t_link.setMovementMethod(LinkMovementMethod.getInstance());
		t_link.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(product[2]));
				startActivity(browserIntent);
			}
		});
	}
	
}
