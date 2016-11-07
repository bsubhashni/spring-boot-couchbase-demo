package restaurantservice;

import java.util.List;
import org.springframework.data.couchbase.core.query.Dimensional;
import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.geo.Box;

/**
 * Created by subhashni on 11/5/16.
 */
public interface RestaurantRepository extends CouchbaseRepository<Restaurant, String> {

	List<Restaurant> findTop10ByZipAndInspectionScoreBetween(String zip, int low, int high, Sort sort);

	@Dimensional(designDocument = "spatial", spatialViewName = "spatial", dimensions = 3)
	List<Restaurant> findFirst10ByLocationWithinAndInspectionScoreBetween(Box boundingBox, int low, int high);
}
