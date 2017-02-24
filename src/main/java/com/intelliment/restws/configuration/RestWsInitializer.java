package com.intelliment.restws.configuration;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.apache.log4j.Logger;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import com.intelliment.restws.model.ParagraphText;
import com.intelliment.restws.security.RestSecurityConfiguration;

public class RestWsInitializer implements WebApplicationInitializer {
	
	private static final Logger logger = Logger.getLogger(RestWsInitializer.class); 

	@Override
	public void onStartup(ServletContext container) throws ServletException {
		
		logger.info("Initializing context..");
		
		AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
		ctx.register(RestWsConfiguration.class, RestSecurityConfiguration.class);
		ctx.setServletContext(container);

		/***
		 * This block will read the file content and create a map with count of
		 * each word and sort it in descending order
		 */
		logger.debug("Reading file to initialize word map.");
		try (InputStream is = container.getResourceAsStream("/WEB-INF/content/paragraph.txt")) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			String line = null;
			long ctr = 1;
			while ((line = reader.readLine()) != null) {
				ctr = 1;

				String[] textArray = line.split(" |\\.|\\,");
				for (String txt : textArray) {
					if (!txt.trim().equals("")) {
						if (ParagraphText.textCountMap.get(txt) != null) {
							ParagraphText.textCountMap.put(txt, ctr++);
						} else {
							ParagraphText.textCountMap.put(txt, ctr);
						}
					}
				}
			}
			// this will sort the map in descending order
			this.sortHashMap();
		} catch (Exception e) {
			logger.error("Error while reading input file and creating map "+ e.getMessage());
		}

		/** Block End **/
		ServletRegistration.Dynamic servlet = container.addServlet("dispatcher", new DispatcherServlet(ctx));

		servlet.setLoadOnStartup(1);
		servlet.addMapping("/");
	}

	/**
	 * This function will sort the map by value.
	 */
	public void sortHashMap() {
		logger.debug("Sorting TextCountMap(size= "+ ParagraphText.textCountMap.size()+") by value.");
		Set<Entry<String, Long>> entries = ParagraphText.textCountMap.entrySet();

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

		ParagraphText.sortedTextCountMap = new LinkedHashMap<String, Long>(listOfEntries.size());
		// copying entries from List to Map
		for (Entry<String, Long> entry : listOfEntries) {
			ParagraphText.sortedTextCountMap.put(entry.getKey(), entry.getValue());
		}
		logger.debug("Map Sorted...");
	}

}
