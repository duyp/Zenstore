package com.zenstore.order.object;

import java.util.ArrayList;

public class ProductManager {
	
	ArrayList<ZenProduct> list;
	
	public ProductManager() {
		list = new ArrayList<>();
	}
	
	public void addProduct(ZenProduct p) {
		list.add(p);
	}
	
	public void addProducts(ZenProduct[] ps) {
		for(ZenProduct p : ps)
			list.add(p);
	}
	
	public ZenProduct getProduct(String id) {
		for(ZenProduct p : list) {
			if (p.id.equals(id))
				return p;
		}
		return null;
	}
	
	public void clear() {
		list.clear();
	}
}
