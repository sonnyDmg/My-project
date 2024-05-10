package com.yrl;

import java.util.List;

public class VoicePlan extends Item{
	private double basePrice;
	private String phoneNumber;
	private int purchasedDays;

	public VoicePlan(String itemCode, String itemName, double basePrice, String phoneNumber, int purchasedDays) {
		super(itemCode, itemName);
		this.phoneNumber = phoneNumber;
		this.purchasedDays = purchasedDays;
	}

	public VoicePlan(String itemCode, String itemName, double basePrice) {
		super(itemCode, itemName);
		this.basePrice = basePrice;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public int getPurchasedDays() {
		return purchasedDays;
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
		double periods = Math.ceil(purchasedDays / 30.0);
		return basePrice * periods;
	}
	
	
}
