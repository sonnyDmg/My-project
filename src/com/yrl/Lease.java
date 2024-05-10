package com.yrl;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

public class Lease extends Product{
	private String startDate;
	private String endDate;
	private double basePrice;

	
	
//	
//	public Lease(String itemCode, String itemName, String startDate, String endDate) {
//		super(itemCode, itemName);
//		this.startDate = startDate;
//		this.endDate = endDate;
//	}



	public Lease(String itemCode, String itemName, String startDate, String endDate, double basePrice) {
		super(itemCode, itemName, basePrice);
		this.startDate = startDate;
		this.endDate = endDate;
		this.basePrice = basePrice;
	}



	public String getStartDate() {
		return startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	


	public double getBasePrice() {
		return basePrice;
	}


	@Override
	public double getTotalPrice() {
		return getSubTotal() + getTaxAmount();
	}

	public int getMonths() {
		return Period.between(LocalDate.parse(this.startDate), LocalDate.parse(this.endDate)).getMonths();
	}

	@Override
	public double getSubTotal() {
		double originalPrice = basePrice * 1.5;
		return originalPrice / getMonths();
	}


	@Override
	public double getTaxAmount() {
		return 0.0;
	}
	
	
}
