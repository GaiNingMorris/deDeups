package com.adobe.service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;

import com.adobe.model.Employee;
import com.adobe.model.LeadsInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Dededuplication implementation
 * 
 * @author ningm
 *
 */
public class Deduplication implements IDeduplication {
	
	private static final Logger LOGGER = LogManager.getLogger(Deduplication.class);
	
	// a hashMap to save id and employee (key is _id, value is employee)
	private final Map<String, Employee> idMap = new HashMap<String, Employee>(); 
	
	// a hashMap to save email and employee (key is email, value is employee)
	private final Map<String, Employee> emailMap = new HashMap<String, Employee>();
	
	// Create a new Gson object
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
	
	public void deDuplicate(String filePath) {
		FileReader fr = null;
		BufferedReader br = null;
		
		try {
			fr = new FileReader(filePath);
			br = new BufferedReader(fr); // Read the leads.json file
			LeadsInfo leadsInfo = gson.fromJson(br, LeadsInfo.class); // convert the json to LeadsInfo object
			
			for (Employee employee : leadsInfo.getLeads()) {
				if (!idMap.containsKey(employee.get_id()) // when _id and email both don`t exist in map idMap and emailMap
						&& !emailMap.containsKey(employee.getEmail())) {
					insertEmployee(employee);
				} else if (idMap.containsKey(employee.get_id())) { // when id exists in map idMap
					if (new DateTime(idMap.get(employee.get_id()).getEntryDate()).getMillis()
							<= new DateTime(employee.getEntryDate()).getMillis()) {
						LOGGER.info("_id is duplicated, " + idMap.get(employee.get_id()));
						
						emailMap.remove(idMap.get(employee.get_id()).getEmail());
						idMap.remove(employee.get_id());
						
						insertEmployee(employee);
					}
				} else { // when email exists in map emailMap
					if (new DateTime(emailMap.get(employee.getEmail()).getEntryDate()).getMillis() 
							<= new DateTime(employee.getEntryDate()).getMillis()) {
						LOGGER.info("email is duplicated, " + emailMap.get(employee.getEmail()));
						
						idMap.remove(emailMap.get(employee.getEmail()).get_id());
						emailMap.remove(employee.getEmail());
						
						insertEmployee(employee);
					}
				}
			}
			
			// write the new records to json file
			writeToJson(leadsInfo); 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (fr != null) {
				try {
					fr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * write new records to json file
	 * 
	 * @param leadsInfo
	 * 			leads information
	 */
	private void writeToJson(LeadsInfo leadsInfo) {
		FileWriter fw = null;

		try {
			leadsInfo.setLeads(new ArrayList<Employee>(idMap.values()));
			String jsonString = gson.toJson(leadsInfo); // convert the Java object to json
			fw = new FileWriter("newLeads.json");
			fw.write(jsonString);
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * insert (id, employee) to map idMap, and (email, employee) to map emailMap
	 * 
	 * @param employee
	 */
	private void insertEmployee(Employee employee) {
		idMap.put(employee.get_id(), employee);
		emailMap.put(employee.getEmail(), employee);
	}
}