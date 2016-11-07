package restaurantservice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * Created by subhashni on 11/5/16.
 */
@SpringBootApplication
@EnableDiscoveryClient
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}

@Configuration
class Config {
	public RestaurantService restaurantService() { return new RestaurantService(); }
}

@RestController
@CrossOrigin
class Controller {
	@Autowired
	RestaurantService restaurantService;

	@RequestMapping("/load")
	public void load() {
		restaurantService.loadRestaurantInfo();
		restaurantService.loadInpectionInfo();
	}

	@RequestMapping(value = "/searchByZip", method = RequestMethod.GET, produces= MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<Restaurant> searchByZip(@RequestParam String zip){
		return restaurantService.getRestaurantsByZip(zip);
	}

}
