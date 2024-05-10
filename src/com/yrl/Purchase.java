package com.yrl;

public class Purchase extends Product{
	public Purchase(String itemCode, String itemName, double basePrice) {
		super(itemCode, itemName, basePrice);
	}

	private double basePrice;
	
	public double getSubTotal() {
		return basePrice;

	}
	
	public double getTax() {
		return 0.0;
	}

	

}
