package com.yrl;

import java.io.File;
import com.thoughtworks.xstream.io.xml.DomDriver;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.thoughtworks.xstream.XStream;

public class Person {
	private String personCode;
	private String firstName;
	private String lastName;
	private Address address;
	private List<String> email = new ArrayList();

	public Person(String personCode, String firstName, String lastName, Address address) {
		this.personCode = personCode;
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
	}
	


	public Person(String personCode, String firstName, String lastName, Address address, List<String> email) {
		super();
		this.personCode = personCode;
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.email = email;
	}



	public void addEmail (List<String> emails) {
		this.email.addAll(emails);
	}
	
	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public Address getAddress() {
		return address;
	}

	public List<String> getEmailAddresses() {
		return email;
	}
	
	public String getPersonCode() {
		return this.personCode;
	}
	
	public String toString() {
	      StringBuilder sb = new StringBuilder();
	      sb.append(String.format("%s, %s ( %s: %s)\n", this.lastName, this.firstName, this.personCode, this.email.toString()));
	      sb.append(this.address);
	      return sb.toString();
	   }



	public void setEmail(List<String> email) {
		this.email = email;
	}
}
