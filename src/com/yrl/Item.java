package com.yrl;

public abstract class Item{
	private String itemCode;
	private String itemName;
	private String itemType;
	private double baseCost;
	
	public Item(String itemCode, String itemName) {
		super();
		this.itemCode = itemCode;
		this.itemName = itemName;
	}

	public Item(String itemCode, String itemType, String itemName, double baseCost) {
        this.itemCode = itemCode;
        this.itemType = itemType;
        this.itemName = itemName;
        this.baseCost = baseCost;
    }
	public String getCode() {
		return itemCode;
	}

	public String getItemName() {
		return itemName;
	}




	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%10s", this.itemCode));
		sb.append(String.format("%sn", this.itemName));
		return sb.toString();
	}
	
	public double getBaseCost() {
		return baseCost;
	}
	
	public abstract double getTotalPrice();
	
	public abstract double getSubTotal();
	
	public abstract double getTaxAmount();

	public String getItemType() {
		return itemType;
	}
	
	
}
