package com.yrl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class Store {
	private String storeCode;
	private Person manager;
	private Address address;
	private String managerr;
	
	public Store(String storeCode, Person manager, Address address) {
		this.storeCode = storeCode;
		this.manager = manager;
		this.address = address;
	}
	public Store(String storeCode, String managerr, Address address) {
		this.storeCode = storeCode;
		this.managerr = managerr;
		this.address = address;
	}

	public String getStoreCode() {
		return storeCode;
	}


	public Person getManager() {
		return manager;
	}


	public Address getAddress() {
		return address;
	}
	
	@Override
	public String toString() {
		return storeCode;
	}

	

}
