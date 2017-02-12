/*
 * Copyright (C) 2010-2013 Paul Watts (paulcwatts@gmail.com)
 * and individual contributors.
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

import org.onebusaway.io.client.ObaApi;
import org.onebusaway.io.client.elements.*;
import org.onebusaway.io.client.mock.MockRegion;
import org.onebusaway.io.client.request.ObaArrivalInfoRequest;
import org.onebusaway.io.client.request.ObaArrivalInfoResponse;
import org.onebusaway.io.client.util.ArrivalInfo;
import org.onebusaway.io.client.util.UIUtils;

import java.util.List;

/**
 * Tests to evaluate utility methods related to the presentation of information to the user in the
 * UI
 */
public class UIUtilTest extends ObaTestCase {

    /**
     * Tests the status and time labels for arrival info
     */
    public void testArrivalInfoLabels() {
        // Initial setup to get an ObaArrivalInfo object from a test response
        ObaRegion tampa = MockRegion.getTampa();
        assertNotNull(tampa);
        ObaApi.getDefaultContext().setRegion(tampa);

        ObaArrivalInfoResponse response =
                new ObaArrivalInfoRequest.Builder("Hillsborough%20Area%20Regional%20Transit_6497").build().call();
        assertOK(response);
        ObaStop stop = response.getStop();
        assertNotNull(stop);
        assertEquals("Hillsborough Area Regional Transit_6497", stop.getId());
        List<ObaRoute> routes = response.getRoutes(stop.getRouteIds());
        assertTrue(routes.size() > 0);
        ObaAgency agency = response.getAgency(routes.get(0).getAgencyId());
        assertEquals("Hillsborough Area Regional Transit", agency.getId());

        // Get the response
        ObaArrivalInfo[] arrivals = response.getArrivalInfo();
        assertNotNull(arrivals);

        /**
         * Labels *with* arrive/depart included, and time labels
         */
        boolean includeArriveDepartLabels = true;
        List<ArrivalInfo> arrivalInfo = ArrivalInfo.convertObaArrivalInfo(arrivals, null,
                response.getCurrentTime(), includeArriveDepartLabels, false);

        // Now confirm that we have the correct number of elements, and values for ETAs for the test
        validateUatcArrivalInfo(arrivalInfo);

        // Arrivals/departures that have already happened
        assertEquals("Arrived on time", arrivalInfo.get(0).getStatusText());
        assertEquals("Route 9 Downtown to UATC via 15th St arrived 4 minutes ago", arrivalInfo.get(0).getLongDescription());

        assertEquals("Departed 2 minutes late", arrivalInfo.get(1).getStatusText());
        assertEquals("Route 6 South to Downtown/MTC departed 3 minutes ago", arrivalInfo.get(1).getLongDescription());
        assertEquals("Departed 1 minute early", arrivalInfo.get(2).getStatusText());
        assertEquals("Route 1 UATC to Downtown via Florida Ave departed 1 minute ago", arrivalInfo.get(2).getLongDescription());
        assertEquals("Arrived on time", arrivalInfo.get(3).getStatusText());
        assertEquals("Route 18 North to UATC/Livingston arrived 1 minute ago", arrivalInfo.get(3).getLongDescription());
        assertEquals("Departed 6 minutes early", arrivalInfo.get(4).getStatusText());
        assertEquals("Route 5 South to Downtown/MTC departed 1 minute ago", arrivalInfo.get(4).getLongDescription());
        // Arrivals and departures that will happen in the future
        assertEquals("On time", arrivalInfo.get(5).getStatusText());
        assertEquals("Route 2 UATC to Downtown via Nebraska Ave is departing now", arrivalInfo.get(5).getLongDescription());
        assertEquals("On time", arrivalInfo.get(6).getStatusText());
        assertEquals("Route 18 South to UATC/Downtown/MTC is arriving now", arrivalInfo.get(6).getLongDescription());
        assertEquals("5 minute delay", arrivalInfo.get(7).getStatusText());
        assertEquals("Route 12 North to University Area TC is arriving in 3 minutes", arrivalInfo.get(7).getLongDescription());
        assertEquals("On time", arrivalInfo.get(8).getStatusText());
        assertEquals("Route 9 UATC to Downtown via 15th St is departing in 5 minutes", arrivalInfo.get(8).getLongDescription());
        assertEquals("On time", arrivalInfo.get(9).getStatusText());
        assertEquals("Route 12 South to Downtown/MTC is departing in 5 minutes", arrivalInfo.get(9).getLongDescription());
        assertEquals("10 minute delay", arrivalInfo.get(10).getStatusText());
        assertEquals("Route 5 North to University Area TC is arriving in 6 minutes", arrivalInfo.get(10).getLongDescription());
        assertEquals("1 minute early", arrivalInfo.get(11).getStatusText());
        assertEquals("Route 6 North to University Area TC is arriving in 7 minutes", arrivalInfo.get(11).getLongDescription());
        assertEquals("On time", arrivalInfo.get(12).getStatusText());
        assertEquals("Route 18 South to UATC/Downtown/MTC is arriving in 10 minutes", arrivalInfo.get(12).getLongDescription());
        assertEquals("1 minute early", arrivalInfo.get(13).getStatusText());
        assertEquals("Route 6 South to Downtown/MTC is departing in 14 minutes", arrivalInfo.get(13).getLongDescription());
        assertEquals("1 minute delay", arrivalInfo.get(14).getStatusText());
        assertEquals("Route 1 Downtown to UATC via Florida Ave is arriving in 17 minutes", arrivalInfo.get(14).getLongDescription());
        assertEquals("On time", arrivalInfo.get(15).getStatusText());
        assertEquals("Route 45 North to University Area TC is arriving in 20 minutes", arrivalInfo.get(15).getLongDescription());
        assertEquals("On time", arrivalInfo.get(16).getStatusText());
        assertEquals("Route 45 South to Westshore TC is departing in 20 minutes", arrivalInfo.get(16).getLongDescription());
        assertEquals("4 minute delay", arrivalInfo.get(17).getStatusText());
        assertEquals("Route 2 Downtown to UATC via Nebraska Ave is arriving in 23 minutes", arrivalInfo.get(17).getLongDescription());
        assertEquals("On time", arrivalInfo.get(18).getStatusText());
        assertEquals("Route 1 UATC to Downtown via Florida Ave is departing in 25 minutes", arrivalInfo.get(18).getLongDescription());
        assertEquals("8 minute delay", arrivalInfo.get(19).getStatusText());
        assertEquals("Route 12 North to University Area TC is arriving in 26 minutes", arrivalInfo.get(19).getLongDescription());
        assertEquals("2 minute delay", arrivalInfo.get(20).getStatusText());
        assertEquals("Route 12 South to Downtown/MTC is departing in 27 minutes", arrivalInfo.get(20).getLongDescription());
        assertEquals("3 minute delay", arrivalInfo.get(21).getStatusText());
        assertEquals("Route 18 North to UATC/Livingston is arriving in 28 minutes", arrivalInfo.get(21).getLongDescription());
        assertEquals("On time", arrivalInfo.get(22).getStatusText());
        assertEquals("Route 18 South to UATC/Downtown/MTC is arriving in 30 minutes", arrivalInfo.get(22).getLongDescription());
        assertEquals("On time", arrivalInfo.get(23).getStatusText());
        assertEquals("Route 2 UATC to Downtown via Nebraska Ave is departing in 30 minutes", arrivalInfo.get(23).getLongDescription());
        assertEquals("6 minute delay", arrivalInfo.get(24).getStatusText());
        assertEquals("Route 5 North to University Area TC is arriving in 32 minutes", arrivalInfo.get(24).getLongDescription());
        assertEquals("3 minute delay", arrivalInfo.get(25).getStatusText());
        assertEquals("Route 18 North to UATC/Livingston is arriving in 32 minutes", arrivalInfo.get(25).getLongDescription());
        assertEquals("6 minute delay", arrivalInfo.get(26).getStatusText());
        assertEquals("Route 6 North to University Area TC is arriving in 34 minutes", arrivalInfo.get(26).getLongDescription());
        assertEquals("On time", arrivalInfo.get(27).getStatusText());
        assertEquals("Route 1 Downtown to UATC via Florida Ave is arriving in 34 minutes", arrivalInfo.get(27).getLongDescription());
        assertEquals("On time", arrivalInfo.get(28).getStatusText());
        assertEquals("Route 5 South to Downtown/MTC is departing in 35 minutes", arrivalInfo.get(28).getLongDescription());
        assertEquals("On time", arrivalInfo.get(29).getStatusText());
        assertEquals("Route 6 South to Downtown/MTC is departing in 35 minutes", arrivalInfo.get(29).getLongDescription());
        assertEquals("9 minute delay", arrivalInfo.get(30).getStatusText());
        assertEquals("Route 9 Downtown to UATC via 15th St is arriving in 35 minutes", arrivalInfo.get(30).getLongDescription());
        assertEquals("On time", arrivalInfo.get(31).getStatusText());
        assertEquals("Route 9 UATC to Downtown via 15th St is departing in 35 minutes", arrivalInfo.get(31).getLongDescription());

        /**
         * Status labels *without* arrive/depart included
         */
        includeArriveDepartLabels = false;
        arrivalInfo = ArrivalInfo.convertObaArrivalInfo(arrivals, null,
                response.getCurrentTime(), includeArriveDepartLabels, false);

        // Now confirm that we have the correct number of elements, and values for ETAs for the test
        validateUatcArrivalInfo(arrivalInfo);

        assertEquals("On time", arrivalInfo.get(0).getStatusText());
        assertEquals("2 minute delay", arrivalInfo.get(1).getStatusText());
        assertEquals("1 minute early", arrivalInfo.get(2).getStatusText());
        assertEquals("On time", arrivalInfo.get(3).getStatusText());
        assertEquals("6 minutes early", arrivalInfo.get(4).getStatusText());
        // Arrivals and departures that will happen in the future
        assertEquals("On time", arrivalInfo.get(5).getStatusText());
        assertEquals("On time", arrivalInfo.get(6).getStatusText());
        assertEquals("5 minute delay", arrivalInfo.get(7).getStatusText());
        assertEquals("On time", arrivalInfo.get(8).getStatusText());
        assertEquals("On time", arrivalInfo.get(9).getStatusText());
        assertEquals("10 minute delay", arrivalInfo.get(10).getStatusText());
        assertEquals("1 minute early", arrivalInfo.get(11).getStatusText());
        assertEquals("On time", arrivalInfo.get(12).getStatusText());
        assertEquals("1 minute early", arrivalInfo.get(13).getStatusText());
        assertEquals("1 minute delay", arrivalInfo.get(14).getStatusText());
        assertEquals("On time", arrivalInfo.get(15).getStatusText());
        assertEquals("On time", arrivalInfo.get(16).getStatusText());
        assertEquals("4 minute delay", arrivalInfo.get(17).getStatusText());
        assertEquals("On time", arrivalInfo.get(18).getStatusText());
        assertEquals("8 minute delay", arrivalInfo.get(19).getStatusText());
        assertEquals("2 minute delay", arrivalInfo.get(20).getStatusText());
        assertEquals("3 minute delay", arrivalInfo.get(21).getStatusText());
        assertEquals("On time", arrivalInfo.get(22).getStatusText());
        assertEquals("On time", arrivalInfo.get(23).getStatusText());
        assertEquals("6 minute delay", arrivalInfo.get(24).getStatusText());
        assertEquals("3 minute delay", arrivalInfo.get(25).getStatusText());
        assertEquals("6 minute delay", arrivalInfo.get(26).getStatusText());
        assertEquals("On time", arrivalInfo.get(27).getStatusText());
        assertEquals("On time", arrivalInfo.get(28).getStatusText());
        assertEquals("On time", arrivalInfo.get(29).getStatusText());
        assertEquals("9 minute delay", arrivalInfo.get(30).getStatusText());
        assertEquals("On time", arrivalInfo.get(31).getStatusText());
    }

    /**
     * Validates the ETAs and number of arrivals for Tampa's University Area Transit Center.  This
     * data is used for a few tests and we want to make sure it's valid.
     */
    private void validateUatcArrivalInfo(List<ArrivalInfo> arrivalInfo) {
        assertEquals(32, arrivalInfo.size());

        assertEquals(-4, arrivalInfo.get(0).getEta());
        assertEquals(-3, arrivalInfo.get(1).getEta());
        assertEquals(-1, arrivalInfo.get(2).getEta());
        assertEquals(-1, arrivalInfo.get(3).getEta());
        assertEquals(-1, arrivalInfo.get(4).getEta());
        assertEquals(0, arrivalInfo.get(5).getEta()); // First non-negative ETA
        assertEquals(0, arrivalInfo.get(6).getEta());
        assertEquals(3, arrivalInfo.get(7).getEta());
        assertEquals(5, arrivalInfo.get(8).getEta());
        assertEquals(5, arrivalInfo.get(9).getEta());
        assertEquals(6, arrivalInfo.get(10).getEta());
        assertEquals(7, arrivalInfo.get(11).getEta());
        assertEquals(10, arrivalInfo.get(12).getEta());
        assertEquals(14, arrivalInfo.get(13).getEta());
        assertEquals(17, arrivalInfo.get(14).getEta());
        assertEquals(20, arrivalInfo.get(15).getEta());
        assertEquals(20, arrivalInfo.get(16).getEta());
        assertEquals(23, arrivalInfo.get(17).getEta());
        assertEquals(25, arrivalInfo.get(18).getEta());
        assertEquals(26, arrivalInfo.get(19).getEta());
        assertEquals(27, arrivalInfo.get(20).getEta());
        assertEquals(28, arrivalInfo.get(21).getEta());
        assertEquals(30, arrivalInfo.get(22).getEta());
        assertEquals(30, arrivalInfo.get(23).getEta());
        assertEquals(32, arrivalInfo.get(24).getEta());
        assertEquals(32, arrivalInfo.get(25).getEta());
        assertEquals(34, arrivalInfo.get(26).getEta());
        assertEquals(34, arrivalInfo.get(27).getEta());
        assertEquals(35, arrivalInfo.get(28).getEta());
        assertEquals(35, arrivalInfo.get(29).getEta());
        assertEquals(35, arrivalInfo.get(30).getEta());
        assertEquals(35, arrivalInfo.get(31).getEta());
    }

    /**
     * Tests the summary of arrival info with an ETA value like "in 9 minutes"
     */
    public void testArrivalSummaryEta() {
        // Initial setup to get an ObaArrivalInfo object from a test response
        ObaRegion tampa = MockRegion.getTampa();
        assertNotNull(tampa);
        ObaApi.getDefaultContext().setRegion(tampa);

        ObaArrivalInfoResponse response =
                new ObaArrivalInfoRequest.Builder("Hillsborough%20Area%20Regional%20Transit_10001").build().call();
        assertOK(response);
        ObaStop stop = response.getStop();
        assertNotNull(stop);
        assertEquals("Hillsborough Area Regional Transit_6497", stop.getId());
        List<ObaRoute> routes = response.getRoutes(stop.getRouteIds());
        assertTrue(routes.size() > 0);
        ObaAgency agency = response.getAgency(routes.get(0).getAgencyId());
        assertEquals("Hillsborough Area Regional Transit", agency.getId());

        // Get the response
        ObaArrivalInfo[] arrivals = response.getArrivalInfo();
        assertNotNull(arrivals);

        /**
         * Labels with arrive/depart included, and time labels
         */
        boolean includeArriveDepartLabels = true;
        final String SEPARATOR = "\n";

        // ETA (instead of clock time)
        boolean clocktime = false;
        List<ArrivalInfo> arrivalInfo = ArrivalInfo.convertObaArrivalInfo(arrivals, null,
                response.getCurrentTime(), includeArriveDepartLabels, clocktime);
        String summary = UIUtils.getArrivalInfoSummary(arrivalInfo, SEPARATOR, clocktime);
        assertEquals("Route 9 Downtown to UATC via 15th St arrived 4 minutes ago and is arriving again in 35 minutes" + SEPARATOR +
                        "Route 6 South to Downtown/MTC departed 3 minutes ago and is departing again in 14 minutes and 35 minutes" + SEPARATOR +
                        "Route 1 UATC to Downtown via Florida Ave departed 1 minute ago and is departing again in 25 minutes" + SEPARATOR +
                        "Route 18 North to UATC/Livingston arrived 1 minute ago and is arriving again in 28 minutes and 32 minutes" + SEPARATOR +
                        "Route 5 South to Downtown/MTC departed 1 minute ago and is departing again in 35 minutes" + SEPARATOR +
                        "Route 2 UATC to Downtown via Nebraska Ave is departing now and again in 30 minutes" + SEPARATOR +
                        "Route 18 South to UATC/Downtown/MTC is arriving now and again in 10 minutes and 30 minutes" + SEPARATOR +
                        "Route 12 North to University Area TC is arriving in 3 minutes and 26 minutes" + SEPARATOR +
                        "Route 9 UATC to Downtown via 15th St is departing in 5 minutes, 35 minutes, 85 minutes, and 90 minutes" + SEPARATOR +
                        "Route 12 South to Downtown/MTC is departing in 5 minutes and 27 minutes" + SEPARATOR +
                        "Route 5 North to University Area TC is arriving in 6 minutes and 32 minutes" + SEPARATOR +
                        "Route 6 North to University Area TC is arriving in 7 minutes and 34 minutes" + SEPARATOR +
                        "Route 1 Downtown to UATC via Florida Ave is arriving in 17 minutes and 34 minutes" + SEPARATOR +
                "Route 45 North to University Area TC is arriving in 20 minutes" + SEPARATOR +
                "Route 45 South to Westshore TC is departing in 20 minutes" + SEPARATOR +
                        "Route 2 Downtown to UATC via Nebraska Ave is arriving in 23 minutes" + SEPARATOR +
                        "Route 9 UATC to Downtown via 15th St is departing in 108 minutes based on the schedule" + SEPARATOR
                , summary);
    }

    /**
     * Tests the summary of arrival info with a clock time value like "at 10:05 PM"
     */
    public void testArrivalSummaryClockTime() {
        // Initial setup to get an ObaArrivalInfo object from a test response
        ObaRegion tampa = MockRegion.getTampa();
        assertNotNull(tampa);
        ObaApi.getDefaultContext().setRegion(tampa);

        ObaArrivalInfoResponse response =
                new ObaArrivalInfoRequest.Builder("Hillsborough%20Area%20Regional%20Transit_10001").build().call();
        assertOK(response);
        ObaStop stop = response.getStop();
        assertNotNull(stop);
        assertEquals("Hillsborough Area Regional Transit_6497", stop.getId());
        List<ObaRoute> routes = response.getRoutes(stop.getRouteIds());
        assertTrue(routes.size() > 0);
        ObaAgency agency = response.getAgency(routes.get(0).getAgencyId());
        assertEquals("Hillsborough Area Regional Transit", agency.getId());

        // Get the response
        ObaArrivalInfo[] arrivals = response.getArrivalInfo();
        assertNotNull(arrivals);

        /**
         * Labels with arrive/depart included, and time labels
         */
        boolean includeArriveDepartLabels = true;
        List<ArrivalInfo> arrivalInfo;
        final String SEPARATOR = "\n";

        // TODO - finish changing the below tests pass for clock times, fix trailing space issue

        // Clock time (instead of ETA)
        boolean clocktime = true;
        arrivalInfo = ArrivalInfo.convertObaArrivalInfo(arrivals, null,
                response.getCurrentTime(), includeArriveDepartLabels, clocktime);
        String summary = UIUtils.getArrivalInfoSummary(arrivalInfo, SEPARATOR, clocktime);
        assertEquals("Route 9 Downtown to UATC via 15th St arrived at 3:51 PM and is arriving again at 4:30 PM" + SEPARATOR +
                        "Route 6 South to Downtown/MTC departed 3:52 PM and is departing again at 4:09 PM and 4:30 PM" + SEPARATOR +
                        "Route 1 UATC to Downtown via Florida Ave departed at 3:54 PM and is departing again at 4:20 PM" + SEPARATOR +
                        "Route 18 North to UATC/Livingston arrived at 3:54 PM and is arriving again at 4:23 PM and 4:27 PM" + SEPARATOR +
                        "Route 5 South to Downtown/MTC departed at 3:54 PM and is departing again at 4:30 PM" + SEPARATOR +
                        "Route 2 UATC to Downtown via Nebraska Ave is departing now and again in 30 minutes" + SEPARATOR +
                        "Route 18 South to UATC/Downtown/MTC is arriving now and again in 10 minutes and 30 minutes" + SEPARATOR +
                        "Route 12 North to University Area TC is arriving in 3 minutes and 26 minutes" + SEPARATOR +
                        "Route 9 UATC to Downtown via 15th St is departing in 5 minutes, 35 minutes, 85 minutes, and 90 minutes" + SEPARATOR +
                        "Route 12 South to Downtown/MTC is departing in 5 minutes and 27 minutes" + SEPARATOR +
                        "Route 5 North to University Area TC is arriving in 6 minutes and 32 minutes" + SEPARATOR +
                        "Route 6 North to University Area TC is arriving in 7 minutes and 34 minutes" + SEPARATOR +
                        "Route 1 Downtown to UATC via Florida Ave is arriving in 17 minutes and 34 minutes" + SEPARATOR +
                        "Route 45 North to University Area TC is arriving in 20 minutes" + SEPARATOR +
                        "Route 45 South to Westshore TC is departing in 20 minutes" + SEPARATOR +
                        "Route 2 Downtown to UATC via Nebraska Ave is arriving in 23 minutes" + SEPARATOR +
                        "Route 9 UATC to Downtown via 15th St is departing in 108 minutes based on the schedule" + SEPARATOR
                , summary);
    }
}
