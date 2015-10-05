/*
 * Copyright (C) 2010 Paul Watts (paulcwatts@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.onebusaway.io.client.test;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;
import org.onebusaway.io.client.ObaApi;
import org.onebusaway.io.client.elements.ObaAgency;
import org.onebusaway.io.client.elements.ObaArrivalInfo;
import org.onebusaway.io.client.elements.ObaRegion;
import org.onebusaway.io.client.elements.ObaRoute;
import org.onebusaway.io.client.elements.ObaSituation;
import org.onebusaway.io.client.elements.ObaStop;
import org.onebusaway.io.client.mock.MockRegion;
import org.onebusaway.io.client.request.ObaArrivalInfoRequest;
import org.onebusaway.io.client.request.ObaArrivalInfoResponse;

@SuppressWarnings("serial")
public class ArrivalInfoRequestTest extends ObaTestCase {

	@Test
    public void testKCMStopRequestUsingRegion() throws URISyntaxException, UnsupportedEncodingException {
        // Test by setting region
        ObaRegion ps = MockRegion.getPugetSound();
        assertNotNull(ps);
        ObaApi.getDefaultContext().setRegion(ps);
        _assertKCMStopRequest();
    }

    private void _assertKCMStopRequest() throws URISyntaxException, UnsupportedEncodingException {
        ObaArrivalInfoRequest.Builder builder =
                new ObaArrivalInfoRequest.Builder("1_29261");
        ObaArrivalInfoRequest request = builder.build();
        UriAssert.assertUriMatch(
        		DEFAULT_BASE_URL + "api/where/arrivals-and-departures-for-stop/1_29261.json",
                new HashMap<String, String>() {{
                    put("key", "*");
                    put("version", "2");
                }},
                request
        );
    }

    @Test
    public void testHARTStopRequestUsingRegion() throws UnsupportedEncodingException, URISyntaxException {
        // Test by setting region
        ObaRegion tampa = MockRegion.getTampa();
        assertNotNull(tampa);
        ObaApi.getDefaultContext().setRegion(tampa);
        _assertHARTStopRequest();
    }

    private void _assertHARTStopRequest() throws UnsupportedEncodingException, URISyntaxException {
        ObaArrivalInfoRequest.Builder builder =
                new ObaArrivalInfoRequest.Builder("Hillsborough Area Regional Transit_3105");
        ObaArrivalInfoRequest request = builder.build();
        UriAssert.assertUriMatch(
        		DEFAULT_BASE_URL + "api/api/where/arrivals-and-departures-for-stop/Hillsborough+Area+Regional+Transit_3105.json",
                new HashMap<String, String>() {{
                    put("key", "*");
                    put("version", "2");
                }},
                request
        );
    }

    public void testKCMStopResponseUsingRegion() throws Exception {
        // Test by setting region
        ObaRegion ps = MockRegion.getPugetSound();
        assertNotNull(ps);
        ObaApi.getDefaultContext().setRegion(ps);
        _assertKCMStopResponse();
    }

    private void _assertKCMStopResponse() throws UnsupportedEncodingException, URISyntaxException {
        ObaArrivalInfoResponse response =
                new ObaArrivalInfoRequest.Builder("1_29261").build().call();
        assertOK(response);
        ObaStop stop = response.getStop();
        assertNotNull(stop);
        assertEquals("1_29261", stop.getId());
        final List<ObaRoute> routes = response.getRoutes(stop.getRouteIds());
        assertTrue(routes.size() > 0);
        ObaAgency agency = response.getAgency(routes.get(0).getAgencyId());
        assertEquals("1", agency.getId());

        final ObaArrivalInfo[] arrivals = response.getArrivalInfo();
        assertNotNull(arrivals);

        final List<ObaStop> nearbyStops = response.getNearbyStops();
        assertTrue(nearbyStops.size() > 0);
    }

    public void testHARTStopResponseUsingRegion() throws Exception {
        // Test by setting region
        ObaRegion tampa = MockRegion.getTampa();
        assertNotNull(tampa);
        ObaApi.getDefaultContext().setRegion(tampa);
        _assertHARTStopResponse();
    }

    private void _assertHARTStopResponse() throws UnsupportedEncodingException, URISyntaxException {
        ObaArrivalInfoResponse response =
                new ObaArrivalInfoRequest.Builder("Hillsborough%20Area%20Regional%20Transit_3105").build().call();
        assertOK(response);
        ObaStop stop = response.getStop();
        assertNotNull(stop);
        assertEquals("Hillsborough Area Regional Transit_3105", stop.getId());
        final List<ObaRoute> routes = response.getRoutes(stop.getRouteIds());
        assertTrue(routes.size() > 0);
        ObaAgency agency = response.getAgency(routes.get(0).getAgencyId());
        assertEquals("Hillsborough Area Regional Transit", agency.getId());

        final ObaArrivalInfo[] arrivals = response.getArrivalInfo();
        // Uhh, this will vary considerably depending on when this is run.
        assertNotNull(arrivals);

        final List<ObaStop> nearbyStops = response.getNearbyStops();
        assertTrue(nearbyStops.size() > 0);
    }

    public void testNewRequestUsingRegion() throws Exception {
        // Test by setting region
        ObaRegion ps = MockRegion.getPugetSound();
        assertNotNull(ps);
        ObaApi.getDefaultContext().setRegion(ps);
        _assertNewRequest();
    }

    private void _assertNewRequest() throws URISyntaxException, UnsupportedEncodingException {
        // This is just to make sure we copy and call newRequest() at least once
        ObaArrivalInfoRequest request = ObaArrivalInfoRequest.newRequest("1_10");
        assertNotNull(request);
        UriAssert.assertUriMatch(
        		DEFAULT_BASE_URL + "api/where/arrivals-and-departures-for-stop/1_10.json",
                new HashMap<String, String>() {{
                    put("key", "*");
                    put("version", "2");
                }},
                request
        );
    }

    public void testStopSituation() throws Exception {
        // Test by setting API directly
    	ObaRegion tampa = MockRegion.getTampa();
        assertNotNull(tampa);
        ObaApi.getDefaultContext().setRegion(tampa);
        ObaArrivalInfoResponse response =
                new ObaArrivalInfoRequest.Builder("PSTA_4077").build().call();
        assertOK(response);
        List<ObaSituation> situations = response.getSituations();
        assertNotNull(situations);

        ObaSituation situation = situations.get(0);
        assertEquals("PSTA_1", situation.getId());
        assertEquals("new 29: 29", situation.getSummary());
        assertEquals("[Low] : 29 updated", situation.getDescription());
        assertEquals("", situation.getReason());
        assertEquals(1424788240588l, situation.getCreationTime());
        assertEquals("", situation.getSeverity());
        assertEquals("PSTA", situation.getAllAffects()[0].getAgencyId());
        assertEquals("", situation.getAllAffects()[0].getApplicationId());
        assertEquals("", situation.getAllAffects()[0].getDirectionId());
        assertEquals("", situation.getAllAffects()[0].getRouteId());
        assertEquals("", situation.getAllAffects()[0].getStopId());
        assertEquals("", situation.getAllAffects()[0].getTripId());

        // TODO - we need valid test responses that include the below situation data
        //ObaSituation.Affects affects = situation.getAffects();
        //assertNotNull(affects);
        //List<String> affectedStops = affects.getStopIds();
        //assertNotNull(affectedStops);
        //assertEquals(1, affectedStops.size());
        //assertEquals("1_75403", affectedStops.get(0));
        //ObaSituation.VehicleJourney[] journeys = affects.getVehicleJourneys();
        //assertNotNull(journeys);
        //ObaSituation.VehicleJourney journey = journeys[0];
        //assertNotNull("1", journey.getDirection());
        //assertNotNull("1_30", journey.getRouteId());
        //List<String> journeyStops = journey.getStopIds();
        //assertNotNull(journeyStops);
        //assertTrue(journeyStops.size() > 0);
        //assertEquals("1_9980", journeyStops.get(0));

        //ObaSituation.Consequence[] consequences = situation.getConsequences();
        //assertNotNull(consequences);
        //assertEquals(1, consequences.length);
        //assertEquals("diversion", consequences[0].getCondition());
    }

    // TODO: get/create situation response
    /*
    public void testTripSituation() throws Exception {
        ObaArrivalInfoResponse response =
                readResourceAs(Resources.getTestUri("arrivals_and_departures_for_stop_1_10020"),
                        ObaArrivalInfoResponse.class);
        assertOK(response);

        ObaArrivalInfo[] infoList = response.getArrivalInfo();
        assertNotNull(infoList);
        assertEquals(2, infoList.length);
        ObaArrivalInfo info = infoList[0];
        String[] situationIds = info.getSituationIds();
        assertNotNull(situationIds);
        List<ObaSituation> situations = response.getSituations(situationIds);
        assertNotNull(situations);

        assertEquals("Expected failure / TODO: add situation", 1, situations.size());
        ObaSituation situation = situations.get(0);
        assertEquals("Snow Reroute", situation.getSummary());
        assertEquals("heavySnowFall", situation.getReason());
        //assertEquals(ObaSituation.REASON_TYPE_ENVIRONMENT,
        //        situation.getReasonType());

        //ObaSituation.Affects affects = situation.getAffects();
        //assertNotNull(affects);

        ObaSituation.Consequence[] consequences = situation.getConsequences();
        assertNotNull(consequences);
        assertEquals(1, consequences.length);
        ObaSituation.Consequence c = consequences[0];
        assertEquals(ObaSituation.Consequence.CONDITION_DIVERSION,
                c.getCondition());
        ObaSituation.ConditionDetails details = c.getDetails();
        assertNotNull(details);
        List<String> stopIds = details.getDiversionStopIds();
        assertNotNull(stopIds);
        assertTrue(stopIds.size() > 0);
        assertEquals("1_9972", stopIds.get(0));
        ObaShape diversion = details.getDiversionPath();
        assertNotNull(diversion);
    }
    */
}
