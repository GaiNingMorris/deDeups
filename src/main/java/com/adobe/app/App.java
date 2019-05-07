package com.adobe.app;

import com.adobe.service.Deduplication;

/**
 * Main Method of App
 * 
 * @author ningm
 *
 */
public class App {

	public static void main(String[] args) {
		Deduplication deDups = new Deduplication();
		deDups.deDuplicate("leads.json");
	}
}
