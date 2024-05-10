package com.yrl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Sale {

	private String saleCode;
	private Store storeCode;
	private Person customer;
	private Person salesPerson;
	private LocalDate saleDate;
	private List<Item> saleItems = new ArrayList();
	private List<SaleItem> items;

	public Sale(String saleCode, Store storeCode, Person customer, Person salesPerson, LocalDate dateStr) {
		super();
		this.saleCode = saleCode;
		this.storeCode = storeCode;
		this.customer = customer;
		this.salesPerson = salesPerson;
		this.saleDate = dateStr;
	}

	public Sale(LocalDate saleDate, double totalAmount, Person customer, Store store) {
		super();
		this.storeCode = storeCode;
		this.customer = customer;
		this.saleDate = saleDate;
	}

	public Store getStoreCode() {
		return storeCode;
	}

	public Person getCustomer() {
		return customer;
	}

	public Person getSalesPerson() {
		return salesPerson;
	}

	public LocalDate getSaleDate() {
		return saleDate;
	}

	public String getSaleCode() {
		return saleCode;
	}

	public List<SaleItem> getItems() {
		if (items == null) {
			items = new ArrayList<>();
		}
		return items;
	}

	public void addItem(Item item) {
		this.saleItems.add(item);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Sale code: ").append(saleCode).append("\n");
		if (storeCode != null) {
			sb.append("Store: ").append(storeCode.getStoreCode()).append("\n ");
		} else {
			sb.append("Store: null\n");
		}
		sb.append("Customer: ").append(customer != null ? customer.toString() : "null").append("\n ");
		sb.append("Sales Person: ").append(salesPerson != null ? salesPerson.toString() : "null").append("\n ");
		sb.append("Sale Date: ").append(saleDate).append("\n ");
		sb.append("Items: \n");
		for (Item item : saleItems) {
			sb.append("- ").append(item).append("\n");
		}
		return sb.toString();
	}
}
