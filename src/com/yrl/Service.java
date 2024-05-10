package com.yrl;

import java.util.List;

public class Service extends Item {
	private double basePrice;
	private double hoursBilled;
	private Person serviceProvider;

	public Service(String code, String itemName, double basePrice, double hoursBilled, Person serviceProvider) {
		super(code, itemName);
		this.hoursBilled = hoursBilled;
		this.serviceProvider = serviceProvider;
	}

	public Service(String itemCode, String itemName, double basePrice) {
		super(itemCode, itemName);
		this.basePrice = basePrice;
	}

	public double getHoursBilled() {
		return hoursBilled;
	}

	public Person getServiceProvider() {
		return serviceProvider;
	}

	@Override
	public double getTotalPrice() {
		return getSubTotal() + getTaxAmount();
	}

	@Override
	public double getTaxAmount() {
		double salesTaxRate = 0.035;
		double salesTax = getSubTotal() * salesTaxRate;
		return Math.round(salesTax * 100.0) / 100.0;
	}

	@Override
	public double getSubTotal() {
		return basePrice * hoursBilled;
	}
}
