package com.yrl;

import java.util.List;

public class Product extends Item{
	private double basePrice;
	
	
	public Product(String itemCode, String itemName, double basePrice) {
		super(itemCode, itemName);
		this.basePrice = basePrice;
	}

	public String toString() {
		return null;
		
	}
	
	
	@Override
	public double getTotalPrice() {
		return getSubTotal() + getTaxAmount();
	}

	@Override
	public double getTaxAmount() {
		double salesTaxRate = 0.065;
		double salesTax = getSubTotal() * salesTaxRate;
		return Math.round(salesTax * 100.0) / 100.0;
	}

	@Override
	public double getSubTotal() {
		return basePrice;
	}

	
}
