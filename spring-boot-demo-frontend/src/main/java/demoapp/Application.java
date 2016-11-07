package demoapp;
import java.net.URI;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;


/**
 * Created by subhashni on 11/5/16.
 */
@EnableDiscoveryClient
@SpringBootApplication
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}

@Configuration
class Config {
	@Bean
	RestTemplate restTemplate() {
		return new RestTemplate();
	}
}

@RestController
@CrossOrigin
@RequestMapping("/api")
class Controller {

	@Autowired
	DiscoveryClient discoveryClient;

	@Autowired
	RestTemplate restTemplate;

	@Value("${restaurant-inspection-service-lookup-name}")
	private String restaurantService;

	@RequestMapping(value = "/searchByZip", method = RequestMethod.GET)
	public List<Restaurant> searchByZip(@RequestParam String zip) {
		URI uri = discoveryClient.getInstances(restaurantService).get(0).getUri();
		ResponseEntity<List<Restaurant>> responseEntity =
				restTemplate.exchange(uri + "/searchByZip?zip={zip}",
						HttpMethod.GET, null, new ParameterizedTypeReference<List<Restaurant>>() {
						}, zip);
		return responseEntity.getBody();
	}

	@RequestMapping(value = "/searchByArea", method = RequestMethod.GET)
	public List<Restaurant> searchByArea(@RequestParam Double x1, @RequestParam Double y1,
										 @RequestParam Double x2, @RequestParam Double y2) {
		URI uri = discoveryClient.getInstances(restaurantService).get(0).getUri();
		ResponseEntity<List<Restaurant>> responseEntity =
				restTemplate.exchange(uri + "/searchByArea?x1={x1}&y1={y1}&x2={x2}&y2={y2}",
						HttpMethod.GET, null, new ParameterizedTypeReference<List<Restaurant>>() {
						}, x1, y1, x2, y2);
		return responseEntity.getBody();
	}
}