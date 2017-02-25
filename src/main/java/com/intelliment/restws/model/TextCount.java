/**
 * Bean to return json (via Object Mapper) 
 * in case of search endpoint is being called.
 */
package com.intelliment.restws.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TextCount {

public List<Map<String, Long>> counts = new ArrayList<>();
	
	
	public List<Map<String, Long>> getCounts() {
		return counts;
	}

	public void setCounts(List<Map<String, Long>> counts) {
		this.counts = counts;
	}
}
