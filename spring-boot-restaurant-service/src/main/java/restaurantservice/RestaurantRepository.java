package restaurantservice;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.data.domain.Sort;

/**
 * Created by subhashni on 11/5/16.
 */
public interface RestaurantRepository extends CouchbaseRepository<Restaurant, String> {

	List<Restaurant> findTop10ByZip(String zip, Sort sort);
}
