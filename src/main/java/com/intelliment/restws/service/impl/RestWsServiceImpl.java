package com.intelliment.restws.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intelliment.restws.model.ParagraphText;
import com.intelliment.restws.model.TextCount;
import com.intelliment.restws.model.TextToSearch;
import com.intelliment.restws.service.RestWsService;

@Service("restWsService")
public class RestWsServiceImpl implements RestWsService {

	private static final Logger logger = Logger.getLogger(RestWsServiceImpl.class); 

	public String searchText(String jsonString) {
		ObjectMapper mapper = new ObjectMapper();
		List<Map<String, Long>> countList = new ArrayList<>();
		Map<String, Long> searchTextMap = null;
		try {
			TextToSearch searchText = mapper.readValue(jsonString, TextToSearch.class);
			if (searchText != null) {
				List<String> searchList = searchText.getSearchText();
				for (String textToSearch : searchList) {
					searchTextMap = new HashMap<>();
					if (ParagraphText.textCountMap.get(textToSearch) != null) {
						searchTextMap.put(textToSearch, ParagraphText.textCountMap.get(textToSearch));
					} else {
						searchTextMap.put(textToSearch, 0L);
					}
					countList.add(searchTextMap);
				}
				TextCount count = new TextCount();
				count.setCounts(countList);
				String countJson = new ObjectMapper().writeValueAsString(count);
				logger.debug("Result for search Text:= "+countJson);
				return countJson;
			}
		} catch (JsonGenerationException e) {
			logger.error("Exception in searchText "+e.getMessage());
		} catch (JsonMappingException e) {
			logger.error("Exception in searchText "+e.getMessage());
		} catch (IOException e) {
			logger.error("Exception in searchText "+e.getMessage());
		}
		return "";
	}

	public List<String> retrieveTopInList(Long maxElementToRetrieve) {
		List<String> topWordList = new ArrayList<>();
		long counter = 0;
		for (Entry<String, Long> entry : ParagraphText.sortedTextCountMap.entrySet()) {
			if (counter < maxElementToRetrieve) {
				topWordList.add(entry.getKey() + "|" + entry.getValue()+"\n");
			}else{
				break;
			}
			counter++;
		}
		logger.debug("Returning top list of size:= "+ topWordList.size());
		return topWordList;
	}
}
