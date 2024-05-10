package com.yrl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

/**
 * This is a collection of utility methods that define a general API for
 * interacting with the database supporting this application.
 *
 */
public class SalesData {
	// Utility method to get a database connection
	private static Connection getConnection() throws SQLException {
		return DatabaseLoader.getConnection();
	}

	/**
	 * Removes all records from all tables in the database.
	 */
	public static void clearDatabase() {
		String[] tables = { "Email", "SaleItem", "Item", "Sale", "Store", "Person", "Address", "Zip", "State" };
		try (Connection conn = getConnection()) {
			for (String table : tables) {
				String query = "DELETE FROM " + table;
				try (PreparedStatement ps = conn.prepareStatement(query)) {
					ps.executeUpdate();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Error clearing database");
		}
	}

	/**
	 * Method to add a person record to the database with the provided data.
	 *
	 * @param personUuid
	 * @param firstName
	 * @param lastName
	 * @param street
	 * @param city
	 * @param state
	 * @param zip
	 */
	public static void addPerson(String personUuid, String firstName, String lastName, String street, String city,
			String state, String zip) {
		String query = "INSERT INTO Person (personCode, firstName, lastName, addressId) VALUES (?, ?, ?, (SELECT addressId FROM Address WHERE street = ? AND city = ? AND stateId = (SELECT stateId FROM State WHERE stateName = ?) AND zipId = (SELECT zipId FROM Zip WHERE zipCode = ?)))";
		try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setString(1, personUuid);
			ps.setString(2, firstName);
			ps.setString(3, lastName);
			ps.setString(4, street);
			ps.setString(5, city);
			ps.setString(6, state);
			ps.setString(7, zip);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Error adding person to the database");
		}
	}

	/**
	 * Adds an email record corresponding person record corresponding to the
	 * provided <code>personUuid</code>
	 *
	 * @param personUuid
	 * @param email
	 */
	public static void addEmail(String personUuid, String email) {
		String query = "INSERT INTO Email (personId, emailAddress) VALUES ((SELECT personId FROM Person WHERE personCode = ?), ?)";
		try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setString(1, personUuid);
			ps.setString(2, email);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Error adding email to the database");
		}
	}

	// Method to add a store record to the database
	public static void addStore(String storeCode, String managerCode, String street, String city, String state,
			String zip) {
		String query = "INSERT INTO Store (storeCode, personId, addressId) VALUES (?, (SELECT personId FROM Person WHERE personCode = ?), (SELECT addressId FROM Address WHERE street = ? AND city = ? AND stateId = (SELECT stateId FROM State WHERE stateName = ?) AND zipId = (SELECT zipId FROM Zip WHERE zipCode = ?)))";
		try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setString(1, storeCode);
			ps.setString(2, managerCode);
			ps.setString(3, street);
			ps.setString(4, city);
			ps.setString(5, state);
			ps.setString(6, zip);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Error adding store to the database");
		}
	}

	// Method to add an item record to the database
	public static void addItem(String code, String name, String type, double basePrice) {
		String query = "INSERT INTO Item (code, name, type, baseCost) VALUES (?, ?, ?, ?)";
		try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setString(1, code);
			ps.setString(2, name);
			ps.setString(3, type);
			ps.setDouble(4, basePrice);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Error adding item to the database");
		}
	}

	// Method to add a sale record to the database
	public static void addSale(String saleCode, String storeCode, String customerPersonUuid, String salesPersonUuid,
			String saleDate) {
		String query = "INSERT INTO Sale (saleCode, storeId, customerId, salePersonId, saleDate) VALUES (?, (SELECT storeId FROM Store WHERE storeCode = ?), (SELECT personId FROM Person WHERE personCode = ?), (SELECT personId FROM Person WHERE personCode = ?), ?)";
		try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setString(1, saleCode);
			ps.setString(2, storeCode);
			ps.setString(3, customerPersonUuid);
			ps.setString(4, salesPersonUuid);
			ps.setString(5, saleDate);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Error adding sale to the database");
		}
	}

	// Method to associate an item with a sale
	public static void addProductToSale(String saleCode, String itemCode) {
		String query = "INSERT INTO SaleItem (saleId, itemId) VALUES ((SELECT saleId FROM Sale WHERE saleCode = ?), (SELECT itemId FROM Item WHERE code = ?))";
		try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setString(1, saleCode);
			ps.setString(2, itemCode);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Error associating item with sale in the database");
		}
	}

	/**
	 * Adds a particular leased (identified by <code>itemCode</code>) to a
	 * particular sale (identified by <code>saleCode</code>) with the start/end date
	 * specified.
	 *
	 * @param saleCode
	 * @param startDate
	 * @param endDate
	 */
	public static void addLeaseToSale(String saleCode, String itemCode, String startDate, String endDate) {
		String query = "INSERT INTO SaleItem (saleId, itemId, startDate, endDate) VALUES ((SELECT saleId FROM Sale WHERE saleCode = ?), (SELECT itemId FROM Item WHERE code = ?), ?, ?)";
		try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setString(1, saleCode);
			ps.setString(2, itemCode);
			ps.setString(3, startDate);
			ps.setString(4, endDate);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Error adding lease to sale in the database");
		}
	}

	/**
	 * Adds a particular service (identified by <code>itemCode</code>) to a
	 * particular sale (identified by <code>saleCode</code>) with the specified
	 * number of hours. The service is done by the employee with the specified
	 * <code>servicePersonUuid</code>
	 *
	 * @param saleCode
	 * @param itemCode
	 * @param billedHours
	 * @param servicePersonUuid
	 */
	public static void addServiceToSale(String saleCode, String itemCode, double billedHours,
			String servicePersonUuid) {
		String query = "INSERT INTO SaleItem (saleId, itemId, hoursBilled, providerId) VALUES ((SELECT saleId FROM Sale WHERE saleCode = ?), (SELECT itemId FROM Item WHERE code = ?), ?, (SELECT personId FROM Person WHERE personCode = ?))";
		try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setString(1, saleCode);
			ps.setString(2, itemCode);
			ps.setDouble(3, billedHours);
			ps.setString(4, servicePersonUuid);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Error adding service to sale in the database");
		}
	}

	/**
	 * Adds a particular data plan (identified by <code>itemCode</code>) to a
	 * particular sale (identified by <code>saleCode</code>) with the specified
	 * number of gigabytes.
	 *
	 * @param saleCode
	 * @param itemCode
	 * @param gbs
	 */
	public static void addDataPlanToSale(String saleCode, String itemCode, double gbs) {
		String query = "INSERT INTO SaleItem (saleId, itemId, gigsOfData) VALUES ((SELECT saleId FROM Sale WHERE saleCode = ?), (SELECT itemId FROM Item WHERE code = ?), ?)";
		try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setString(1, saleCode);
			ps.setString(2, itemCode);
			ps.setDouble(3, gbs);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Error adding data plan to sale in the database");
		}
	}

	/**
	 * Adds a particular voice plan (identified by <code>itemCode</code>) to a
	 * particular sale (identified by <code>saleCode</code>) with the specified
	 * <code>phoneNumber</code> for the given number of <code>days</code>.
	 *
	 * @param saleCode
	 * @param itemCode
	 * @param phoneNumber
	 * @param days
	 */
	public static void addVoicePlanToSale(String saleCode, String itemCode, String phoneNumber, int days) {
		String query = "INSERT INTO SaleItem (saleId, itemId, phoneNumber, vpDaysBought) VALUES ((SELECT saleId FROM Sale WHERE saleCode = ?), (SELECT itemId FROM Item WHERE code = ?), ?, ?)";
		try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setString(1, saleCode);
			ps.setString(2, itemCode);
			ps.setString(3, phoneNumber);
			ps.setInt(4, days);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Error adding voice plan to sale in the database");
		}
	}
}
