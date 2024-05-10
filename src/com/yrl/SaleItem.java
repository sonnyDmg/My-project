package com.yrl;

import java.util.List;

public class SaleItem {
	private Sale sale;
	private Item item;
	private int quantity;
	public SaleItem(Sale sale, Item item) {
		super();
		this.sale = sale;
		this.item = item;
	}



	public Sale getSale() {
		return sale;
	}

	public void setSale(Sale sale) {
		this.sale = sale;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	private String saleCode;
	private String itemCode;
	private String startDate;
	private String endDate;
	private double hoursBilled;
	private int providerId;
	private double gigsOfData;
	private String phoneNumber;
	private int vpDaysBought;

	public SaleItem(String saleCode, String itemCode) {
		super();
		this.saleCode = saleCode;
		this.itemCode = itemCode;
	}

	public SaleItem(Item item, String saleCode, String itemCode, double hoursBilled, int providerId, double gigsOfData,
			String phoneNumber, int vpDaysBought) {
		super();
		this.saleCode = saleCode;
		this.itemCode = itemCode;
		this.startDate = startDate;
		this.endDate = endDate;
		this.hoursBilled = hoursBilled;
		this.providerId = providerId;
		this.gigsOfData = gigsOfData;
		this.phoneNumber = phoneNumber;
		this.vpDaysBought = vpDaysBought;
	}

	public String getStartDate() {
		return startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public double getHoursBilled() {
		return hoursBilled;
	}

	public int getProviderId() {
		return providerId;
	}

	public double getGigsOfData() {
		return gigsOfData;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public int getVpDaysBought() {
		return vpDaysBought;
	}

	public String getSaleCode() {
		return saleCode;
	}

	public String getItemCode() {
		return itemCode;
	}

}
