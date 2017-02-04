package com.zenstore.order.object;

import java.util.ArrayList;

public class OrderStatusList {

	ArrayList<Status> list;
	
	public OrderStatusList() {
		list = new ArrayList<OrderStatusList.Status>();
	}
	
	public void addStatus(int id, String name) {
		list.add(new Status(id,name));
	}
	
	public ArrayList<Status> getList() {
		return list;
	}
	
	public String getName(int id) {
		for(Status t:list) {
			if(t.id == id) {
				return t.name;
			}
		}
		return null;
	}
	
	private class Status {
		public int id;
		public String name;
		
		public Status(int id, String name) {
			this.id = id;
			this.name = name;
		}
	}
	
}
