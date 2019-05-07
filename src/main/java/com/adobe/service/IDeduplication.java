package com.adobe.service;

/**
 * Dededuplication interface
 * 
 * @author ningm
 *
 */
public interface IDeduplication {
	
	/**
	 * remove the duplicate records
	 * 
	 * @param filePath
	 * 				json file path
	 */
	public void deDuplicate(String filePath);
	
}
