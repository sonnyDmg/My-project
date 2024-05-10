package com.yrl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseLoader {
	// Connection details
	public static final String USERNAME = "sgonzalez22";
	public static final String PASSWORD = "nierooC3gaeb";
	public static final String PARAMETERS = "";
	public static final String SERVER = "cse-linux-01.unl.edu";
	public static final String URL = String.format("jdbc:mysql://%s/%s?%s", SERVER, USERNAME, PARAMETERS);

	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(URL, USERNAME, PASSWORD);
	}

	public static Person getPersonById(int personId) {
		Person person = null;
		String query = "SELECT * FROM Person WHERE personId = ?";
		try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setInt(1, personId);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					String personCode = rs.getString("personCode");
					String firstName = rs.getString("firstName");
					String lastName = rs.getString("lastName");
					// Load address details
					Address address = getDetailedAddress(rs.getInt("addressId"));
					person = new Person(personCode, firstName, lastName, address);
					// Load email details
					getDetailedEmail(personId, person);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Error loading person details from database");
		}
		return person;
	}

	public static Address getDetailedAddress(int addressId) {
		Address address = null;
		String query = "SELECT * FROM Address WHERE addressId = ?";
		try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setInt(1, addressId);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					String street = rs.getString("street");
					String zip = rs.getString("zip");
					String city = rs.getString("city");
					String state = rs.getString("state");
					address = new Address(street, zip, city, state);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Error loading address details from database");
		}
		return address;
	}

	public static void getDetailedEmail(int personId, Person person) {
		List<String> emails = new ArrayList<>();
		String query = "SELECT * FROM Email WHERE personId = ?";
		try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setInt(1, personId);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					String email = rs.getString("email");
					emails.add(email);
				}
				person.setEmail(emails);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Error loading email details from database");
		}
	}

	// Similar methods for loading store, sale, and sale item details from SQL
	// tables...

	public static Store getStoreByCode(String storeCode) {
		Store store = null;
		String query = "SELECT * FROM Store WHERE storeCode = ?";
		try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setString(1, storeCode);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					Person manager = getPersonById(rs.getInt("managerCode"));
					Address address = getDetailedAddress(rs.getInt("addressId"));
					store = new Store(storeCode, manager, address);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Error loading store details from database");
		}
		return store;
	}

	public static Sale getSaleByCode(String saleCode) {
		Sale sale = null;
		String query = "SELECT * FROM Sale WHERE saleCode = ?";
		try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setString(1, saleCode);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					Store store = getStoreByCode(rs.getString("storeCode")); // This line causes the error
					Person customer = getPersonById(rs.getInt("customerCode"));
					Person salesperson = getPersonById(rs.getInt("salesPersonCode"));
					LocalDate saleDate = rs.getDate("saleDate").toLocalDate();
					sale = new Sale(saleCode, store, customer, salesperson, saleDate);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Error loading sale details from database");
		}
		return sale;
	}

	public static SaleItem getSaleItemByCode(String saleCode) {
		SaleItem saleItem = null;
		String query = "SELECT * FROM SaleItem WHERE saleCode = ?";
		try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setString(1, saleCode);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					// Assuming the SaleItem class has appropriate constructors or setters
					String itemCode = rs.getString("itemCode");
					saleItem = new SaleItem(saleCode, itemCode);
					// Set other attributes of SaleItem as per the ResultSet
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Error loading sale item details from database");
		}
		return saleItem;
	}

	public static List<Sale> getAllSales() {
		List<Sale> sales = new ArrayList<>();
		String query = "SELECT * FROM Sale";
		try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Sale sale = getSaleByCode(rs.getString("saleCode"));
					sales.add(sale);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Error loading all sales from database");
		}
		return sales;
	}

	public static double getTotalCostForSale(String saleCode) {
		double totalCost = 0;
		String query = "SELECT SUM(itemCost) AS totalCost FROM SaleItem WHERE saleCode = ?";
		try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setString(1, saleCode);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					totalCost = rs.getDouble("totalCost");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Error calculating total cost for sale from database");
		}
		return totalCost;
	}

	public static int getItemsSoldForSale(String saleCode) {
		int itemsSold = 0;
		String query = "SELECT COUNT(*) AS itemsSold FROM SaleItem WHERE saleCode = ?";
		try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setString(1, saleCode);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					itemsSold = rs.getInt("itemsSold");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Error calculating number of items sold for sale from database");
		}
		return itemsSold;
	}

	public static List<Store> getAllStores() {
		List<Store> stores = new ArrayList<>();
		String query = "SELECT * FROM Store";
		try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Store store = getStoreByCode(rs.getString("storeCode"));
					stores.add(store);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Error loading all stores from database");
		}
		return stores;
	}

	public static int getTotalSalesForStore(String storeCode) {
		int totalSales = 0;
		String query = "SELECT COUNT(*) AS totalSales FROM Sale WHERE storeCode = ?";
		try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setString(1, storeCode);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					totalSales = rs.getInt("totalSales");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Error calculating total sales for store from database");
		}
		return totalSales;
	}

	public static List<SaleItem> getItemsForSale(String saleCode) {
		List<SaleItem> items = new ArrayList<>();
		String query = "SELECT * FROM SaleItem WHERE saleCode = ?";
		try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setString(1, saleCode);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					SaleItem item = getSaleItemByCode(saleCode);
					items.add(item);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Error loading items for sale from database");
		}
		return items;
	}

}
