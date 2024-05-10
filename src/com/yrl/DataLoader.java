package com.yrl;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;


public class DataLoader {

	public static List<Person> loadPersonData(String fileName) {
		List<Person> persons = new ArrayList<>();
		File personFile = new File("data/Persons.csv");
		try (Scanner scanner = new Scanner(personFile)) {
			scanner.nextLine(); // Skip header line
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String[] tokens = line.split(",");
				String personCode = tokens[0];
				String firstName = tokens[1];
				String lastName = tokens[2];
				Address address = new Address(tokens[3], tokens[4], tokens[5], tokens[6]);
				List<String> emailAddresses = new ArrayList<String>();

				for (int i = 7; i < tokens.length; i++) {
					emailAddresses.add(tokens[i]);

				}
				persons.add(new Person(personCode, firstName, lastName, address, emailAddresses));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return persons;
	}

	public static List<Store> loadStoreData(String fileName, List<Person> persons) {
		List<Store> stores = new ArrayList<>();
		File storeFile = new File("data/Stores.csv");
		try (Scanner scanner = new Scanner(storeFile)) {
			scanner.nextLine(); // Skip header line
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String[] tokens = line.split(",");
				String storeCode = tokens[0];
				String managerUUID = tokens[1];
				Person manager = findPersonById(persons, managerUUID);
				Address address = new Address(tokens[2], tokens[3], tokens[4], tokens[5]);
				stores.add(new Store(storeCode, manager, address));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return stores;
	}

	public static List<Item> loadItemData(String fileName) {
		List<Item> items = new ArrayList<>();
		File itemFile = new File("data/Items.csv");
		try (Scanner scanner = new Scanner(itemFile)) {
			scanner.nextLine(); // Skip header line
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String[] tokens = line.split(",");
				String itemCode = tokens[0];
				String itemType = tokens[1];
				String itemName = tokens[2];
				double baseCost = Double.parseDouble(tokens[3]);
				switch (itemType) {
				case "D":
					items.add(new DataPlan(itemCode, itemName, baseCost));
					break;
				case "P":
					items.add(new Product(itemCode, itemName, baseCost));
					break;
				case "V":
					items.add(new VoicePlan(itemCode, itemName, baseCost));
					break;
				case "S":
					items.add(new Service(itemCode, itemName, baseCost));
					break;
				default:
					break;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return items;
	}

	public static List<Sale> loadSaleData(String fileName, List<Person> persons, List<Store> stores) {
		List<Sale> sales = new ArrayList<>();
		File saleFile = new File("data/Sales.csv"); // Use the provided fileName parameter
		try (Scanner scanner = new Scanner(saleFile)) {
			scanner.nextLine(); // Skip header line
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String[] tokens = line.split(",");
				String saleCode = tokens[0];
				String storeCode = tokens[1];
				String customerUUID = tokens[2];
				String salesPersonUUID = tokens[3];
				LocalDate date = LocalDate.parse(tokens[4]);
				Person customer = findPersonById(persons, customerUUID);
				Person salesPerson = findPersonById(persons, salesPersonUUID);
				Store store = findStoreByCode(stores, storeCode);
				if (store == null) {
					System.err.println("Store not found for sale with code: " + saleCode);
					// You may choose to handle this case differently based on your requirements
					continue; // Skip this sale if store is not found
				}
				sales.add(new Sale(saleCode, store, customer, salesPerson, date));
			}
		} catch (FileNotFoundException e) {
			System.err.println("File not found: " + fileName);
			e.printStackTrace();
		} catch (DateTimeParseException e) {
			System.err.println("Error parsing date in file: " + fileName);
			e.printStackTrace();
		}
		return sales;
	}

	public static List<SaleItem> loadSaleItemData(String fileName, List<Sale> sales, List<Item> items,
			List<Person> persons) {
		List<SaleItem> saleItems = new ArrayList<>();
		Map<String, Sale> saleMap = sales.stream().collect(Collectors.toMap(Sale::getSaleCode, Function.identity()));
		Map<String, Item> itemMap = items.stream().collect(Collectors.toMap(Item::getCode, Function.identity()));
		Map<String, Person> personMap = persons.stream()
				.collect(Collectors.toMap(Person::getPersonCode, Function.identity()));

		File saleItemFile = new File("data/SaleItems.csv");
		try (Scanner scanner = new Scanner(saleItemFile)) {
			scanner.nextLine(); // Skip header line
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String[] tokens = line.split(",");
				if (tokens.length < 2) {
                    continue;
                }
				String saleCode = tokens[0];
				String itemCode = tokens[1];
				Sale sale = saleMap.get(saleCode);
				Item item = itemMap.get(itemCode);
				if (item instanceof Product) {
					if (tokens.length == 2) {
						Purchase purchase = new Purchase(item.getCode(), item.getItemName(), item.getBaseCost());
						sale.addItem(purchase);
					} else {
						String startDate = tokens[2];
						String endDate = tokens[3];
						Lease lease = new Lease(item.getCode(), item.getItemName(), endDate, startDate,
								item.getBaseCost());
						sale.addItem(lease);
					}
				} else if (item instanceof Service) {
					double hoursBilled = Double.parseDouble(tokens[2]);
					String serviceProviderUUID = tokens[3];
					Person serviceProvider = personMap.get(serviceProviderUUID);
					Service service = new Service(item.getCode(), item.getItemName(), item.getBaseCost(), hoursBilled,
							serviceProvider);
					sale.addItem(service);
				} else if (item instanceof DataPlan) {
					double purchasedGBs = Double.parseDouble(tokens[2]);
					DataPlan dataPlan = new DataPlan(item.getCode(), item.getItemName(), item.getBaseCost(),
							purchasedGBs);
					sale.addItem(dataPlan);
				} else if (item instanceof VoicePlan) {
					String phoneNumber = tokens[2];
					int purchasedDays = Integer.parseInt(tokens[3]);
					VoicePlan voicePlan = new VoicePlan(item.getCode(), item.getItemName(), item.getBaseCost(),
							phoneNumber, purchasedDays);
					sale.addItem(voicePlan);
				}

				saleItems.add(new SaleItem(sale, item));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return saleItems;
	}

	private static Person findPersonById(List<Person> persons, String personCode) {
		for (Person person : persons) {
			if (person.getPersonCode().equals(personCode)) {
				return person;
			}
		}
		return null;
	}

	private static Store findStoreByCode(List<Store> stores, String storeCode) {
		for (Store store : stores) {
			if (store.getStoreCode().equals(storeCode)) {
				return store;
			}
		}
		return null;
	}

	private static Sale findSaleByCode(List<Sale> sales, String saleCode) {
		for (Sale sale : sales) {
			if (sale.getSaleCode().equals(saleCode)) {
				return sale;
			}
		}
		return null;
	}

	private static Item findItemByCode(List<Item> items, String itemCode) {
		for (Item item : items) {
			if (item.getCode().equals(itemCode)) {
				return item;
			}
		}
		return null;
	}

	public static Map<String, Item> mapItems(List<Item> items) {
		Map<String, Item> itemMap = new HashMap<>();
		for (Item item : items) {
			itemMap.put(item.getCode(), item);
		}
		return itemMap;
	}
}
