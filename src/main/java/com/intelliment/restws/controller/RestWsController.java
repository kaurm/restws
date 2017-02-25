package com.intelliment.restws.controller;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.intelliment.restws.exception.InvalidDataException;
import com.intelliment.restws.service.RestWsService;

@RestController
public class RestWsController {

	private static final Logger logger = Logger.getLogger(RestWsController.class);

	@Autowired
	RestWsService restWsService;

	/**
	 * This function return json with count corresponding to each word provided
	 * in the input json during endpoint call.
	 * 
	 * @throws InvalidDataException
	 */
	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public String getSearchTextCount(@RequestBody String searchText, HttpStatus status) throws InvalidDataException {
		logger.info("Calling function to search text := " + searchText);
		String countJson = "";
		try {
			countJson = restWsService.searchText(searchText);
		} catch (InvalidDataException e) {
			logger.info("Invalid Data, sending 422 status...");
			throw e;
		}
		return countJson;
	}

	/**
	 * This Function will call the service to get top word counts from paragraph
	 * and display them in csv format.
	 * 
	 * @param maxElementToRetrieve
	 * @param response
	 */
	@RequestMapping(value = "/top/{maxElementToRetrieve}", method = RequestMethod.GET)
	public void getTopText(@PathVariable("maxElementToRetrieve") long maxElementToRetrieve,
			HttpServletResponse response) {

		logger.info("Fetching top " + maxElementToRetrieve + " words from paragraph");

		List<String> topWordCountList = restWsService.retrieveTopInList(maxElementToRetrieve);

		if (topWordCountList != null && topWordCountList.size() > 0) {
			try {
				response.setHeader("Content-Disposition", "attachment; filename=Temp.csv");
				response.setContentType("text/csv");

				Iterator<String> iter = topWordCountList.iterator();
				while (iter.hasNext()) {
					String outputString = (String) iter.next();
					response.getOutputStream().print(outputString);
				}
				response.getOutputStream().flush();
			} catch (IOException e) {
				logger.error("Error while generating CSV output." + e.getMessage());
			}
		} else {
			try {
				response.getOutputStream().print("Sorry...No Data Found!!");
				response.getOutputStream().flush();
			} catch (IOException e) {
				logger.error("Error generating blank response to browser.");
			}
		}

	}

}
