package restaurantservice;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;

/**
 * Created by subhashni on 11/6/16.
 */

@Document
class Restaurant {
	@Id
	public String business_id;
	public String name;
	public String address;
	public String city;
	public String state;

	@JsonProperty(value = "postal_code", required = false)
	public String zip;
	public String latitude;
	public String longitude;
	public int inspectionScore;

	public int date;
}