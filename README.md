# onebusaway-client-library [![Build Status](https://travis-ci.org/OneBusAway/onebusaway-client-library.svg?branch=master)](https://travis-ci.org/OneBusAway/onebusaway-client-library)
A Java library that makes it easy to call the OneBusAway REST APIs (http://developer.onebusaway.org/modules/onebusaway-application-modules/current/api/where/index.html)

### Requirements

You'll need [JDK 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html).

### Usage

The below example shows how to get the stops near a location, first by using the results of the OBA Regions API, and then secondly by setting a custom OBA Server API.

~~~
private static void getStops() throws IOException {
  // Set the API key to be used - should be changed to your API key
  ObaApi.getDefaultContext().setApiKey("TEST");
  ObaRegionsResponse response = null;

  try {
    // Call the OBA Regions API (http://regions.onebusaway.org/regions-v3.json)
	  response = ObaRegionsRequest.newRequest().call();
  } catch (URISyntaxException e) {
  	e.printStackTrace();
  }
  ArrayList<ObaRegion> regions = new ArrayList<ObaRegion>(Arrays.asList(response.getRegions()));
  for (ObaRegion r : regions) {
    if (r.getName().equalsIgnoreCase("Tampa")) {
  	  ObaApi.getDefaultContext().setRegion(r);
  	  // Get the stops for the region named "Tampa"
		  getStops();
	  }
  }

  // An example of setting a custom API server
  UriBuilder builder = UriBuilder.fromUri("http://api.tampa.onebusaway.org/api/");

  try {
  	ObaApi.getDefaultContext().setBaseUrl(builder);
  	// Get the stops from a custom API
	  getStops();
  } catch (URISyntaxException e) {
  	e.printStackTrace();
  }
}

private static void getStops() throws IOException {
	Location l = new Location("Test");
	l.setLatitude(28.0664191);
	l.setLongitude(-82.4298721);
	ObaStopsForLocationResponse response2 = null;
	// Call the OBA stops-for-location API (http://developer.onebusaway.org/modules/onebusaway-application-modules/current/api/where/methods/stops-for-location.html)
	try {
		response2 = new ObaStopsForLocationRequest.Builder(l)
				.setQuery("3105")
		        .build()
		        .call();
	} catch (URISyntaxException e) {
		e.printStackTrace();
	}
  final ObaStop[] list = response2.getStops();
  for (ObaStop s : list) {
    System.out.println(s.getName() + "\n");
  }
}
~~~
