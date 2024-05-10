package com.yrl;

import java.util.List;

public class DataPlan extends Item{
	private double basePrice;
	private double purchasedGBs;

	
	public DataPlan(String itemCode, String itemName, double basePrice, double purchasedGBs) {
		super(itemCode, itemName);
		this.basePrice = basePrice;
		this.purchasedGBs = purchasedGBs;
	}
	


	public DataPlan(String itemCode, String itemName, double basePrice) {
		super(itemCode, itemName);
		this.basePrice = basePrice;
	}



	public double getPurchasedGBs() {
		return purchasedGBs;
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
		return basePrice * purchasedGBs;
	}
	
	
}
