package com.intelliment.restws.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intelliment.restws.exception.InvalidDataException;
import com.intelliment.restws.model.TextCount;
import com.intelliment.restws.model.TextToSearch;
import com.intelliment.restws.service.RestWsService;

@Service("restWsService")
public class RestWsServiceImpl implements RestWsService {

	private static final Logger logger = Logger.getLogger(RestWsServiceImpl.class);

	Map<String, Long> textCountMap = new HashMap<>();
	LinkedHashMap<String, Long> sortedTextCountMap = new LinkedHashMap<>();

	/*
	 * This Function will Initiate the maps after reading the input file.
	 */
	@PostConstruct
	public void init() {

		logger.debug("Reading file to initialize word map.");

		Resource resource = new ClassPathResource("paragraph.txt");

		try (InputStream is = resource.getInputStream();) {

			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			String line = null;
			long ctr = 1;

			while ((line = reader.readLine()) != null) {
				ctr = 1;
				String[] textArray = line.split(" |\\.|\\,");
				for (String txt : textArray) {
					if (!txt.trim().equals("")) {
						if (textCountMap.get(txt) != null) {
							textCountMap.put(txt, ctr++);
						} else {
							textCountMap.put(txt, ctr);
						}
					}
				}
			}
			// this will sort the map in descending order
			this.sortHashMap();
		} catch (Exception e) {
			logger.error("Error while reading input file and creating map " + e.getMessage());
		}

	}

	/**
	 * This function will sort the map by value
	 * in descending order.
	 */
	public void sortHashMap() {
		logger.debug("Sorting TextCountMap(size= " + textCountMap.size() + ") by value.");
		Set<Entry<String, Long>> entries = textCountMap.entrySet();

		Comparator<Entry<String, Long>> valueComparator = new Comparator<Entry<String, Long>>() {

			@Override
			public int compare(Entry<String, Long> e1, Entry<String, Long> e2) {
				Long v1 = e1.getValue();
				Long v2 = e2.getValue();
				return v2.compareTo(v1);
			}
		};

		List<Entry<String, Long>> listOfEntries = new ArrayList<Entry<String, Long>>(entries);

		// sorting HashMap by values using comparator
		Collections.sort(listOfEntries, valueComparator);

		sortedTextCountMap = new LinkedHashMap<String, Long>(listOfEntries.size());
		// copying entries from List to Map
		for (Entry<String, Long> entry : listOfEntries) {
			sortedTextCountMap.put(entry.getKey(), entry.getValue());
		}
		logger.debug("Map Sorted...");
	}

	public String searchText(String jsonString) throws InvalidDataException {
		ObjectMapper mapper = new ObjectMapper();
		List<Map<String, Long>> countList = new ArrayList<>();
		Map<String, Long> searchTextMap = null;
		try {
			TextToSearch searchText = mapper.readValue(jsonString, TextToSearch.class);
			if (searchText != null) {
				List<String> searchList = searchText.getSearchText();
				for (String textToSearch : searchList) {
					searchTextMap = new HashMap<>();
					if (textCountMap.get(textToSearch) != null) {
						searchTextMap.put(textToSearch, textCountMap.get(textToSearch));
					} else {
						searchTextMap.put(textToSearch, 0L);
					}
					countList.add(searchTextMap);
				}
				TextCount count = new TextCount();
				count.setCounts(countList);
				String countJson = new ObjectMapper().writeValueAsString(count);
				logger.info("Result for search Text:= " + countJson);
				return countJson;
			}
		} catch (Exception e) {
			// logger.error("JsonGenerationException in searchText " +
			// e.getMessage());
			throw new InvalidDataException(e.getMessage());
		}
		return "";
	}

	public List<String> retrieveTopInList(Long maxElementToRetrieve) {
		List<String> topWordList = new ArrayList<>();
		long counter = 0;
		for (Entry<String, Long> entry : sortedTextCountMap.entrySet()) {
			if (counter < maxElementToRetrieve) {
				topWordList.add(entry.getKey() + "|" + entry.getValue() + "\n");
			} else {
				break;
			}
			counter++;
		}
		logger.debug("Returning top list of size:= " + topWordList.size());
		return topWordList;
	}
}