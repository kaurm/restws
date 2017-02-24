package com.intelliment.restws.test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.matchers.InstanceOf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.intelliment.restws.configuration.RestWsConfiguration;
import com.intelliment.restws.controller.RestWsController;
import com.intelliment.restws.model.TextToSearch;
import com.intelliment.restws.service.RestWsService;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RestWsConfiguration.class)
@WebAppConfiguration
public class RestWsTest {

	@Autowired
	private WebApplicationContext ctx;
	private MockMvc mockMvc;

	@InjectMocks
	private RestWsController controller;

	@Mock
	private RestWsService restService;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx).build();
	}

	@Test
	public void testSearchTextInParagraph() throws Exception {
		String input = "{\"searchText\":[\"Duis\",\"Sed\",\"Donec\",\"Augue\",\"Pellentesque\",\"123\"]}";
		String output = "{\"counts\":[{\"Duis\":61},{\"Sed\":49},{\"Donec\":38},{\"Augue\":0},{\"Pellentesque\":157},{\"123\":0}]}";

//		when(restService.searchText(input)).thenReturn(output);
		this.mockMvc.perform(post("/search")
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.content(input)).andExpect(status().isOk());
		//.andExpect(jsonPath("$.counts", arrayWithSize(6)));
	}
	
	@Test
	public void testSearchTextInoutJsonIsCorrupted() throws Exception {
		String input = "{\"searchText\",},{\"test\"}";
		String output = "{\"counts\":[{\"Duis\":61},{\"Sed\":49},{\"Donec\":38},{\"Augue\":0},{\"Pellentesque\":157},{\"123\":0}]}";

		this.mockMvc.perform(post("/search")
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.content(input));
	}
}
