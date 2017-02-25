import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class Test {
	public List<String> list = new ArrayList<>();
	public List<Map<String, Long>> counts = new ArrayList<>();

	public List<Map<String, Long>> getCounts() {
		return counts;
	}

	public void setCounts(List<Map<String, Long>> counts) {
		this.counts = counts;
	}

	public List<String> getList() {
		return list;
	}

	public void setList(List<String> list) {
		this.list = list;
	}

	public static void main(String[] args) {

		final String uri = "http://localhost:8080/counter-api/search/";
		RestTemplate restTemplate = new RestTemplate();
		// Add the Jackson message converter
		restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
		String input = "{\"searchText\":[\"Duis\",\"Sed\",\"Donec\",\"Augue\",\"Pellentesque\",\"123\"]}";
		
		// set headers
		String plainClientCredentials="test:abc123";
		String base64ClientCredentials = new String(Base64.encodeBase64(plainClientCredentials.getBytes()));
		 
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", "Basic " +base64ClientCredentials);
		HttpEntity<String> entity = new HttpEntity<String>(input, headers);

		// send request and parse result
		ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);
		//
		System.out.println(response);

		// try {
		//
		// ObjectMapper mapper = new ObjectMapper();
		// String json = "{\"list\":[\"name\", \"age\"]}";
		// Map<String, Long> map = new HashMap<>();
		//// List<String> list = new ArrayList<>();
		// // convert JSON string to Map
		// Test list = mapper.readValue(json, Test.class);
		// List<Map<String, Long>> ll = new ArrayList<>();
		// System.out.println(list.getList().get(0));
		// System.out.println(list.getList().get(1));
		// for (String textToSearch : list.getList()) {
		// map=new HashMap<>();
		// map.put(textToSearch, 1L);
		// ll.add(map);
		// }
		//
		// Test tt = new Test();
		// tt.setCounts(ll);
		// String jsonStr = new ObjectMapper().writeValueAsString(tt);
		// System.out.println(jsonStr);
		//
		// } catch (JsonGenerationException e) {
		// e.printStackTrace();
		// } catch (JsonMappingException e) {
		// e.printStackTrace();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }

//		String str = "sit amet, consectetur adipiscing elit. ";
//		String[] tokens = str.split(" |\\.|\\,");
//		for (int i = 0; i < tokens.length; i++) {
//			if (!tokens[i].trim().equals("")) {
//				System.out.println(tokens[i]);
//			}
//		}
	}
}
