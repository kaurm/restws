/**
 * Converting the Input Json to a Java Object
 * to help in processing the input.
 */
package com.intelliment.restws.model;

import java.util.List;

public class TextToSearch {

	private List<String> searchText;
	
	public List<String> getSearchText() {
		return searchText;
	}
	public void setSearchText(List<String> searchText) {
		this.searchText = searchText;
	}
}
