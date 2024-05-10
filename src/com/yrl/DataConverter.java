package com.yrl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;


public class DataConverter {
	
	public static void loadStoresXML(List<Store> stores) {
		XStream xstream = new XStream(new DomDriver());
		File f = new File("data/Stores.xml");
		PrintWriter pw;
		String s = xstream.toXML(stores);

		try {
			pw = new PrintWriter(f);
			pw.print(s);
			pw.close();
		}catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void loadPersonsXML(List<Person> person) {
		XStream xstream = new XStream(new DomDriver());
		File f = new File("data/Persons.xml");
		PrintWriter pw;
		String s = xstream.toXML(person);

		try {
			pw = new PrintWriter(f);
			pw.print(s);
			pw.close();
		}catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void loadItemsXML(List<Item> item) {
		XStream xstream = new XStream(new DomDriver());
		File f = new File("data/Items.xml");
		PrintWriter pw;
		String s = xstream.toXML(item);

		try {
			pw = new PrintWriter(f);
			pw.print(s);
			pw.close();
		}catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	
}
