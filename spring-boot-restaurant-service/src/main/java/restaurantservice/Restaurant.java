package restaurantservice;

import com.couchbase.client.core.annotations.InterfaceAudience;
import com.couchbase.client.deps.com.fasterxml.jackson.databind.annotation.JsonAppend;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.geo.Point;

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
	public float latitude;
	public float longitude;
	public int inspectionScore;
	public String location;
	public int date;

}