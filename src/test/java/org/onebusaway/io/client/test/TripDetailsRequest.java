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

import org.junit.Test;
import org.onebusaway.io.client.ObaApi;
import org.onebusaway.io.client.elements.ObaRegion;
import org.onebusaway.io.client.elements.ObaTrip;
import org.onebusaway.io.client.elements.ObaTripSchedule;
import org.onebusaway.io.client.elements.ObaTripStatus;
import org.onebusaway.io.client.mock.MockRegion;
import org.onebusaway.io.client.request.ObaTripDetailsRequest;
import org.onebusaway.io.client.request.ObaTripDetailsResponse;

@SuppressWarnings("serial")
public class TripDetailsRequest extends ObaTestCase {

    protected final String TEST_TRIP_ID = "1_18196913";

    @Test
    public void testKCMTripRequestUsingRegion() throws UnsupportedEncodingException, URISyntaxException {
        // Test by setting region
        ObaRegion ps = MockRegion.getPugetSound();
        assertNotNull(ps);
        ObaApi.getDefaultContext().setRegion(ps);
        _assertKCMTripRequest();
    }

    private void _assertKCMTripRequest() throws URISyntaxException, UnsupportedEncodingException {
        ObaTripDetailsRequest.Builder builder =
                new ObaTripDetailsRequest.Builder(TEST_TRIP_ID);
        ObaTripDetailsRequest request = builder.build();
        UriAssert.assertUriMatch(
                "http://api.pugetsound.onebusaway.org/api/where/trip-details/" + TEST_TRIP_ID
                        + ".json",
                new HashMap<String, String>() {{
                    put("key", "*");
                    put("version", "2");
                }},
                request
        );
    }

    @Test
    public void testKCMTripResponseUsingRegion() throws Exception {
        // Test by setting region
        ObaRegion ps = MockRegion.getPugetSound();
        assertNotNull(ps);
        ObaApi.getDefaultContext().setRegion(ps);
        _assertKCMTripResponse();
    }

    @Test
    private void _assertKCMTripResponse() throws UnsupportedEncodingException, URISyntaxException {
        ObaTripDetailsResponse response =
                new ObaTripDetailsRequest.Builder(TEST_TRIP_ID)
                        .build()
                        .call();
        assertOK(response);
        assertEquals(TEST_TRIP_ID, response.getId());

        ObaTripSchedule schedule = response.getSchedule();
        assertNotNull(schedule);

        //ObaTripStatus status = response.getStatus();
        //assertNotNull(status);

        // Make sure the trip exists
        ObaTrip trip = response.getTrip(response.getId());
        assertNotNull(trip);
    }

    @Test
    public void testNoTripsRequestUsingRegion() throws UnsupportedEncodingException, URISyntaxException {
        // Test by setting region
        ObaRegion ps = MockRegion.getPugetSound();
        assertNotNull(ps);
        ObaApi.getDefaultContext().setRegion(ps);
        _assertNoTripsRequest();
    }

    private void _assertNoTripsRequest() throws UnsupportedEncodingException, URISyntaxException {
        ObaTripDetailsRequest request = new ObaTripDetailsRequest.Builder(TEST_TRIP_ID)
                .setIncludeTrip(false)
                .build();
        UriAssert.assertUriMatch(
                "http://api.pugetsound.onebusaway.org/api/where/trip-details/" + TEST_TRIP_ID
                        + ".json",
                new HashMap<String, String>() {{
                    put("includeTrip", "false");
                    put("key", "*");
                    put("version", "2");
                }},
                request
        );
    }

    // TODO: API response includes the trip anyway
    @Test
    public void testNoTripsResponse() throws Exception {
        ObaTripDetailsResponse response =
                new ObaTripDetailsRequest.Builder(TEST_TRIP_ID)
                        .setIncludeTrip(false)
                        .build()
                        .call();
        assertOK(response);
        assertEquals(TEST_TRIP_ID, response.getId());
        // Make sure the trip exists
        ObaTrip trip = response.getTrip(response.getId());
        assertNull("Expected failure / TODO: report as API bug?", trip);
    }

    @Test
    public void testNoScheduleRequestUsingRegion() throws UnsupportedEncodingException, URISyntaxException {
        // Test by setting region
        ObaRegion ps = MockRegion.getPugetSound();
        assertNotNull(ps);
        ObaApi.getDefaultContext().setRegion(ps);
        _assertNoScheduleRequest();
    }

    private void _assertNoScheduleRequest() throws UnsupportedEncodingException, URISyntaxException {
        ObaTripDetailsRequest request = new ObaTripDetailsRequest.Builder(TEST_TRIP_ID)
                .setIncludeSchedule(false)
                .build();
        UriAssert.assertUriMatch(
                "http://api.pugetsound.onebusaway.org/api/where/trip-details/" + TEST_TRIP_ID
                        + ".json",
                new HashMap<String, String>() {{
                    put("includeSchedule", "false");
                    put("key", "*");
                    put("version", "2");
                }},
                request
        );
    }

    @Test
    public void testNoScheduleResponse() throws Exception {
        ObaTripDetailsResponse response =
                new ObaTripDetailsRequest.Builder(TEST_TRIP_ID)
                        .setIncludeSchedule(false)
                        .build()
                        .call();
        assertOK(response);
        assertEquals(TEST_TRIP_ID, response.getId());

        ObaTripSchedule schedule = response.getSchedule();
        assertNull(schedule);
    }

    @Test
    public void testNoStatusRequestUsingRegion() throws UnsupportedEncodingException, URISyntaxException {
        // Test by setting region
        ObaRegion ps = MockRegion.getPugetSound();
        assertNotNull(ps);
        ObaApi.getDefaultContext().setRegion(ps);
        _assertNoStatusRequest();
    }

    private void _assertNoStatusRequest() throws URISyntaxException, UnsupportedEncodingException {
        ObaTripDetailsRequest request =
                new ObaTripDetailsRequest.Builder(TEST_TRIP_ID)
                        .setIncludeStatus(false)
                        .build();
        UriAssert.assertUriMatch(
                "http://api.pugetsound.onebusaway.org/api/where/trip-details/" + TEST_TRIP_ID
                        + ".json",
                new HashMap<String, String>() {{
                    put("includeStatus", "false");
                    put("key", "*");
                    put("version", "2");
                }},
                request
        );
    }

    @Test
    public void testNoStatus() throws Exception {
        ObaTripDetailsResponse response =
                new ObaTripDetailsRequest.Builder(TEST_TRIP_ID)
                        .setIncludeStatus(false)
                        .build()
                        .call();
        assertOK(response);
        assertEquals(TEST_TRIP_ID, response.getId());

        ObaTripStatus status = response.getStatus();
        assertNull(status);
    }

    @Test
    public void testNewRequestUsingRegion() throws UnsupportedEncodingException, URISyntaxException {
        // Test by setting region
        ObaRegion ps = MockRegion.getPugetSound();
        assertNotNull(ps);
        ObaApi.getDefaultContext().setRegion(ps);
        _assertNewRequest();
    }

    private void _assertNewRequest() throws UnsupportedEncodingException, URISyntaxException {
        // This is just to make sure we copy and call newRequest() at least once
        ObaTripDetailsRequest request =
                ObaTripDetailsRequest.newRequest(TEST_TRIP_ID);
        UriAssert.assertUriMatch(
                "http://api.pugetsound.onebusaway.org/api/where/trip-details/" + TEST_TRIP_ID
                        + ".json",
                new HashMap<String, String>() {{
                    put("key", "*");
                    put("version", "2");
                }},
                request
        );
    }
}
