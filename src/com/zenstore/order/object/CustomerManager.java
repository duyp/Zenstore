package com.zenstore.order.object;

import java.util.ArrayList;

public class CustomerManager {
	
	ArrayList<ZenCustomer> list;
	
	public CustomerManager() {
		list = new ArrayList<>();
	}
	
	public void addCustomer(ZenCustomer c) {
		list.add(c);
	}
	
	public void addCustomers(ZenCustomer[] cs) {
		for(ZenCustomer c: cs)
			list.add(c);
	}
	
	public ZenCustomer getCustomer(String id) {
		for(ZenCustomer c : list) {
			if (c.id.equals(id))
				return c;
		}
		return null;
	}
	
	public void clear() {
		list.clear();
	}
}
