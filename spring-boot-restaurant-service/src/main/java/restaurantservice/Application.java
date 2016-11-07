package restaurantservice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.geo.Box;
import org.springframework.data.geo.Point;
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

	@Value("${lowScore}")
	int lowScore;

	@Value("${highScore}")
	int highScore;


	@RequestMapping("/load")
	public void load() {
		restaurantService.loadRestaurantInfo();
		restaurantService.loadInpectionInfo();
	}

	@RequestMapping(value = "/searchByZip", method = RequestMethod.GET, produces= MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<Restaurant> searchByZip(@RequestParam String zip){
		return restaurantService.getRestaurantsByZip(zip, lowScore, highScore);
	}

	@RequestMapping(value = "/searchByArea", method = RequestMethod.GET, produces= MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<Restaurant> searchByArea(@RequestParam Double x1, @RequestParam Double y1,
										 @RequestParam Double x2, @RequestParam Double y2){
		Point point1 = new Point(x1, y1);
		Point point2 = new Point(x2, y2);
		Box boundingBox = new Box(point1, point2);
		return restaurantService.getRestaurantsByArea(boundingBox, lowScore, highScore);
	}


}
