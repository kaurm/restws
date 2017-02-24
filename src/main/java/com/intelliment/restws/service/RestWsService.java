package com.intelliment.restws.service;

import java.util.List;

public interface RestWsService {
	/**
	 * This function will search for word in input json
	 * and create a json with corresponding counts for each of that word.
	 * @param jsonString
	 * @return Json string consisting of counts
	 */
	public String searchText(String jsonString);
	
	/**
	 * This function will retrieve the top words from list 
	 * which have highest occurrences in the whole paragraph.
	 * @param maxElementToRetrieve
	 * @return
	 */
	public List<String> retrieveTopInList(Long maxElementToRetrieve);
}
