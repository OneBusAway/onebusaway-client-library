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
import org.onebusaway.io.client.elements.ObaRoute;
import org.onebusaway.io.client.mock.MockRegion;
import org.onebusaway.io.client.request.ObaTripRequest;
import org.onebusaway.io.client.request.ObaTripResponse;

@SuppressWarnings("serial")
public class TripRequestTest extends ObaTestCase {

    protected final String TEST_TRIP_ID = "1_18196913";

    @Test
    public void testKCMTripRequest() throws UnsupportedEncodingException, URISyntaxException {
        // Test by setting region
        ObaRegion ps = MockRegion.getPugetSound();
        assertNotNull(ps);
        ObaApi.getDefaultContext().setRegion(ps);
        callKCMTripRequest();
        ObaApi.getDefaultContext().setRegion(null);
    }

    private void callKCMTripRequest() throws URISyntaxException, UnsupportedEncodingException {
        ObaTripRequest.Builder builder = new ObaTripRequest.Builder(TEST_TRIP_ID);
        ObaTripRequest request = builder.build();
        UriAssert.assertUriMatch(
                "http://api.pugetsound.onebusaway.org/api/where/trip/" + TEST_TRIP_ID + ".json",
                new HashMap<String, String>() {{
                    put("key", "*");
                    put("version", "2");
                }},
                request
        );
    }

    @Test
    public void testKCMTripResponse() throws Exception {
        ObaTripResponse response =
                new ObaTripRequest.Builder(TEST_TRIP_ID)
                        .build()
                        .call();
        assertOK(response);
        assertEquals(TEST_TRIP_ID, response.getId());
        assertEquals("1_65", response.getRouteId());

        final ObaRoute route = response.getRoute();
        assertNotNull(route);
        assertEquals("1_65", route.getId());
        assertEquals("1", route.getAgencyId());
    }

    @Test
    public void testNewRequest() throws UnsupportedEncodingException, URISyntaxException {
        // Test by setting region
        ObaRegion ps = MockRegion.getPugetSound();
        assertNotNull(ps);
        ObaApi.getDefaultContext().setRegion(ps);
        callNewRequest();
        ObaApi.getDefaultContext().setRegion(null);
    }

    private void callNewRequest() throws UnsupportedEncodingException, URISyntaxException {
        // This is just to make sure we copy and call newRequest() at least once
        ObaTripRequest request = ObaTripRequest.newRequest(TEST_TRIP_ID);
        UriAssert.assertUriMatch(
                "http://api.pugetsound.onebusaway.org/api/where/trip/" + TEST_TRIP_ID + ".json",
                new HashMap<String, String>() {{
                    put("key", "*");
                    put("version", "2");
                }},
                request
        );
    }
}
