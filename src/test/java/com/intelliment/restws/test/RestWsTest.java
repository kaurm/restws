package com.intelliment.restws.test;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.intelliment.restws.configuration.RestWsConfiguration;
import com.intelliment.restws.security.RestSecurityConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { RestWsConfiguration.class,
		RestSecurityConfiguration.class }, loader = AnnotationConfigWebContextLoader.class)

public class RestWsTest {

	@Autowired
	private WebApplicationContext ctx;
	private MockMvc mockMvc;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx).build();
	}

	@Test
	public void testSearchTextInParagraph() throws Exception {
		String input = "{\"searchText\":[\"Duis\",\"Sed\",\"Donec\",\"Augue\",\"Pellentesque\",\"123\"]}";

		this.mockMvc
				.perform(post("/search").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
						.content(input))
				.andExpect(status().isOk()).andExpect((jsonPath("$.counts", Matchers.hasSize(6))))
				.andExpect((jsonPath("$.counts[1].Sed", is(49))));
	}

	@Test
	public void testSearchTextInputJsonIsCorrupted() throws Exception {
		String input = "{\"searchText\"},{\"test\"}";
		this.mockMvc.perform(post("/search").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.content(input)).andExpect(status().isUnprocessableEntity());
	}

	@Test
	public void testCsvIsGenerated() {
		String contentType = "text/csv";
		String expectedResult="eleifend|184\ncondimentum|183\nblandit|181\nMauris|179\nmagna|178";

		try {
			ResultActions result = this.mockMvc.perform(get("/top/5"));
			result.andExpect(status().isOk()).andExpect(content().contentType(contentType))
			.andExpect(content().string(containsString(expectedResult)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}