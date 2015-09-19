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

import org.junit.Test;
import org.onebusaway.io.client.ObaApi;
import org.onebusaway.io.client.elements.ObaAgency;
import org.onebusaway.io.client.elements.ObaRegion;
import org.onebusaway.io.client.elements.ObaRoute;
import org.onebusaway.io.client.mock.MockRegion;
import org.onebusaway.io.client.request.ObaRouteRequest;
import org.onebusaway.io.client.request.ObaRouteResponse;

public class RouteRequestTest extends ObaTestCase {

	@Test
    public void testKCMRoute() throws UnsupportedEncodingException, URISyntaxException {
        String defaultRouteLineColor = "00F";
        String defaultRouteTextColor = "ff000000";
        ObaRouteRequest.Builder builder = new ObaRouteRequest.Builder("1_10");
        ObaRouteRequest request = builder.build();
        ObaRouteResponse response = request.call();
        assertOK(response);
        assertEquals("1_10", response.getId());
        assertEquals("10", response.getShortName());
        assertEquals("1", response.getAgencyId());
        assertEquals(ObaRoute.TYPE_BUS, response.getType());
        String routeColor = defaultRouteLineColor;
        if (response.getColor() != null) {
            routeColor = response.getColor();
        }
        String routeTextColor = defaultRouteTextColor;
        if (response.getTextColor() != null) {
            routeTextColor = response.getTextColor();
        }
        // KCM doesn't define route line or text color in their GTFS, so we should be using the defaults
        assertEquals(defaultRouteLineColor, routeColor);
        assertEquals(defaultRouteTextColor, routeTextColor);

        ObaAgency agency = response.getAgency();
        assertNotNull(agency);
        assertEquals("1", agency.getId());
        assertEquals("Metro Transit", agency.getName());
    }

	@Test
    public void testNewRequest() throws UnsupportedEncodingException, URISyntaxException {
        // This is just to make sure we copy and call newRequest() at least once
        ObaRouteRequest request = ObaRouteRequest.newRequest("1_10");
        assertNotNull(request);
    }

	@Test
    public void testHARTRoute() throws URISyntaxException, UnsupportedEncodingException {
        // Test by setting region
        ObaRegion tampa = MockRegion.getTampa();
        assertNotNull(tampa);
        ObaApi.getDefaultContext().setRegion(tampa);

        String defaultRouteLineColor = "00F";
        String defaultRouteTextColor = "ff000000";

        ObaRouteRequest.Builder builder = new ObaRouteRequest.Builder("Hillsborough Area Regional Transit_5");
        ObaRouteRequest request = builder.build();
        ObaRouteResponse response = request.call();
        assertOK(response);
        assertEquals("Hillsborough Area Regional Transit_5", response.getId());
        assertEquals("5", response.getShortName());
        assertEquals("Hillsborough Area Regional Transit", response.getAgencyId());
        assertEquals(ObaRoute.TYPE_BUS, response.getType());

        String route5LineColor = "09346D";
        String route5TextColor = "FFFFFF";

        String routeColor = defaultRouteLineColor;
        if (response.getColor() != null) {
            routeColor = response.getColor();
        }
        String routeTextColor = defaultRouteTextColor;
        if (response.getTextColor() != null) {
            routeTextColor = response.getTextColor();
        }

        assertEquals(route5LineColor, routeColor);
        assertEquals(route5TextColor, routeTextColor);

        ObaAgency agency = response.getAgency();
        assertNotNull(agency);
        assertEquals("Hillsborough Area Regional Transit", agency.getId());
        assertEquals("Hillsborough Area Regional Transit", agency.getName());
    }
}
