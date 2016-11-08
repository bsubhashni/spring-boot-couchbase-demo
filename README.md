#Bootiful Couchbase

##Restaurant suggestion application based on inspection score

Requirements:
* Couchbase Server 4.5
* Maven

To run:
* Create bucket called restaurants.
* Do ```mvn spring-boot:run``` on eureka service, restaurant service and front-end demo. That should spin up services listening on 
127.0.0.1:8671, :9000, :8080 respectively. The order of starting up the services are important for discovery.
* Create secondary or primary index using N1QL and 2i.
* Create spatial view index with map function

~~~~ 
function(doc, meta) {
   if (doc.latitude != 0 & doc.longitude != 0) {
          emit([{
     "type": "Point",
     "coordinates":[doc.longitude, doc.latitude],
     },  doc.inspectionScore], doc.name);
   }
}
~~~~ 
Search application at http://host:8080/
