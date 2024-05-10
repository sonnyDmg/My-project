
package com.yrl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class SalesReport {
	public static void main(String[] args) throws IOException {
		// Load persons, items, stores, sales, and saleItems
		List<Person> persons = DataLoader.loadPersonData("Persons.csv");
		List<Store> stores = DataLoader.loadStoreData("Stores.csv", persons);
		List<Item> items = DataLoader.loadItemData("Items.csv");
		List<Sale> sales = DataLoader.loadSaleData("Sales.csv", persons, stores);
		List<SaleItem> saleItems = DataLoader.loadSaleItemData("SaleItems.csv", sales, items, persons);

		DataConverter.loadPersonsXML(persons);
		DataConverter.loadItemsXML(items);
		DataConverter.loadStoresXML(stores);

		// Generate reports
		generateSummaryReport(sales, saleItems);
		generateStoreSummaryReport(stores, sales, saleItems);
		generateIndividualSaleReport(sales, saleItems);
	}

	public static void generateSummaryReport(List<Sale> sales, List<SaleItem> saleItems) {
		List<Item> items = DataLoader.loadItemData("Items.csv");
		Map<String, Item> itemMap = DataLoader.mapItems(items);

		System.out.println("+----------------------------------------------------------------------------------------+");
		System.out.println("| Summary Report - By Total                                                              |");
		System.out.println("+----------------------------------------------------------------------------------------+");
		System.out.println("Invoice #  Store      Customer                       Num Items          Tax       Total");

		// Calculate totals
		double totalSales = 0.0;
		double totalTax = 0.0;

		// Map to store total sales per sale
		Map<String, Double> saleTotalMap = new HashMap<>();

		// Output individual sales
		for (Sale sale : sales) {
			// Get the list of sale items for the current sale
			List<SaleItem> numItems = saleItems.stream().filter(si -> si.getSale().equals(sale)).toList();

			// Calculate the quantity of items in the sale
			int quantity = numItems.size();

			// Update totals
			double saleTotal = numItems.stream().mapToDouble(si -> si.getItem().getTotalPrice()).sum();
			double saleTax = numItems.stream().mapToDouble(si -> si.getItem().getTaxAmount()).sum();
			totalSales += saleTotal + saleTax;
			totalTax += saleTax;

			// Output each sale item
			String customerName = sale.getCustomer() != null
					? sale.getCustomer().getLastName() + ", " + sale.getCustomer().getFirstName()
					: "";
			System.out.printf("%-11s%-11s%-30s%-20d$%-12.2f$%-12.2f%n", sale.getSaleCode(), sale.getStoreCode(), 
					customerName, quantity, saleTax, saleTotal + saleTax);

		}

		// Output totals
		System.out.println("+----------------------------------------------------------------------------------------+");
		System.out.printf("%-71s$%-12.2f$%-12.2f%n", " ", totalTax, totalSales + totalTax);
	}

	public static void generateStoreSummaryReport(List<Store> stores, List<Sale> sales, List<SaleItem> saleItems) {
	    System.out.println("\n+----------------------------------------------------------------+");
	    System.out.println("| Store Sales Summary Report                                     |");
	    System.out.println("+----------------------------------------------------------------+");
	    System.out.println("Store      Manager                        # Sales    Grand Total    ");
	    Map<String, Double> storeTotals = new HashMap<>();
	    Map<String, Integer> storeSalesCount = new HashMap<>(); // Map to store sales count per store

	    // Calculate total sales and count for each store
	    for (Sale sale : sales) {
	        Store store = sale.getStoreCode();
	        double totalSales = 0.0;

	        for (SaleItem saleItem : saleItems) {
	            if (saleItem.getSale().equals(sale)) {
	                totalSales += saleItem.getItem().getTotalPrice();
	            }
	        }

	        if (!storeTotals.containsKey(store.getStoreCode())) {
	            storeTotals.put(store.getStoreCode(), totalSales);
	            storeSalesCount.put(store.getStoreCode(), 1); // Initialize count to 1
	        } else {
	            storeTotals.put(store.getStoreCode(), storeTotals.get(store.getStoreCode()) + totalSales);
	            storeSalesCount.put(store.getStoreCode(), storeSalesCount.get(store.getStoreCode()) + 1); // Increment count
	        }
	    }

	    // Output store summary
	    for (Store store : stores) {
	        System.out.printf("%-11s%-31s%-11s$%10.2f%n", store.getStoreCode(),
	                store.getManager().getLastName() + ", " + store.getManager().getFirstName(),
	                storeSalesCount.getOrDefault(store.getStoreCode(), 0), // Use count per store
	                storeTotals.getOrDefault(store.getStoreCode(), 0.0));
	    }

	    // Output total sales across all stores
	    System.out.println("+----------------------------------------------------------------+");
	    System.out.printf("%-11s%-31s%-11s$%10.2f%n", "", "", sales.size(), // Total sales count across all stores
	            storeTotals.values().stream().mapToDouble(Double::doubleValue).sum());
	}


	public static void generateIndividualSaleReport(List<Sale> sales, List<SaleItem> saleItems) {
		System.out.println("\nDetails for each individual sale:");

		for (Sale sale : sales) {
			System.out.println("Sale: " + sale.getSaleCode());
			System.out.println("Store: " + sale.getStoreCode());
			System.out.println("Date: " + sale.getSaleDate());
			System.out.println("Customer: " + sale.getCustomer().getFirstName() + " " + sale.getCustomer().getLastName()
					+ " (" + sale.getCustomer().getPersonCode() + ")");
			System.out.println("Sales Person: " + sale.getSalesPerson().getFirstName() + " "
					+ sale.getSalesPerson().getLastName() + " (" + sale.getSalesPerson().getPersonCode() + ")");

			List<SaleItem> items = saleItems.stream().filter(si -> si.getSale().equals(sale)).toList();
			double totalTax = items.stream().mapToDouble(si -> si.getItem().getTaxAmount()).sum();
			double totalPrice = items.stream().mapToDouble(si -> si.getItem().getTotalPrice()).sum();

			System.out.println("Items (" + items.size()
					+ ")                                                            Tax       Total");
			System.out.println("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-                          -=-=-=-=-=- -=-=-=-=-=-");

			for (SaleItem item : items) {
				Item i = item.getItem();
				System.out.printf("%-65s$%10.2f $%10.2f%n", i.getItemName() + " (" + i.getCode() + ")",
						i.getTaxAmount(), i.getSubTotal());
			}

			System.out.println("                                                             -=-=-=-=-=- -=-=-=-=-=-");
			System.out.printf("                                                   Subtotals $%10.2f $%10.2f%n",
					totalTax, totalPrice);
			System.out.printf("                                                 Grand Total             $%10.2f%n%n",
					totalTax + totalPrice);
		}
	}
}