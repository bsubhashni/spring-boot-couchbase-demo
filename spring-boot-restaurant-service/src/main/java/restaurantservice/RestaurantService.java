package restaurantservice;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.couchbase.core.CouchbaseTemplate;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.domain.Sort;
import org.springframework.data.geo.Box;
import org.springframework.stereotype.Service;
import restaurantservice.RestaurantRepository;

/**
 * Created by subhashni on 11/5/16.
 */
@Service
public class RestaurantService {
	@Autowired
	RestaurantRepository restaurantRepository;

	@Autowired
	CouchbaseTemplate couchbaseTemplate;


	public List<Restaurant> getRestaurantsByZip(String value, int lowScore, int highScore) {
		Sort sort = new Sort(Sort.Direction.DESC, "inspectionScore");
		return restaurantRepository.findTop10ByZipAndInspectionScoreBetween(value, lowScore, highScore, sort);
	}

	public List<Restaurant> getRestaurantsByArea(Box boundingBox, int lowScore, int highScore) {
		return restaurantRepository.findFirst10ByLocationWithinAndInspectionScoreBetween(boundingBox, lowScore, highScore);
	}

	public void loadRestaurantInspectionInfo() {
		loadRestaurantInfo();
		loadInpectionInfo();
	}

	/** Load restaurant data */
	public void loadRestaurantInfo() {
		restaurantRepository.save(loadObjectList(Restaurant.class, "businesses.csv"));

	}

	/** Add inspection */
	public void loadInpectionInfo() {
		List<Inspection> inspections = loadObjectList(Inspection.class, "inspections.csv");
		inspections.stream()
					.map(inspection -> addInspectionData(inspection))
					.map(restaurant -> restaurantRepository.save(restaurant))
					.toArray();
	}

	private Restaurant addInspectionData(Inspection inspection) {
		Restaurant restaurant = restaurantRepository.findOne(inspection.business_id);
		if (restaurant.date < inspection.date) {
			restaurant.date = inspection.date;
			restaurant.inspectionScore = inspection.score;
		}
		return restaurant;
	}

	/** Reads data from csv **/
	private <T> List<T> loadObjectList(Class<T> type, String fileName) {
		try {
			CsvSchema bootstrapSchema = CsvSchema.emptySchema().withHeader();
			CsvMapper mapper = new CsvMapper();
			File file = new ClassPathResource(fileName).getFile();
			MappingIterator<T> readValues =
					mapper.reader(type).with(bootstrapSchema).readValues(file);
			return readValues.readAll();
		} catch (Exception e) {
			return Collections.emptyList();
		}
	}

	public static class Inspection {
		public String business_id;
		public int score;
		public int date;
		public String type;
	}
}
