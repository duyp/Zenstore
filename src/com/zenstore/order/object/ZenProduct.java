package com.zenstore.order.object;

public class ZenProduct {
	
	public String id;
	public String name;
	public int price;
	public String link;
	
	public ZenProduct() {
		id = "";
		price = 0;
		link = "";
		name = "";
	}
	
	public ZenProduct(String[] product) {
		id = product[0];
		name = product[1];
		price = Integer.valueOf(product[2]);
		link = product[3];
	}
	
	public String[] toArray() {
		return new String[] {id, name, String.valueOf(price), link};
	}
}
