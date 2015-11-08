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

import org.junit.Test;
import org.onebusaway.io.client.ObaApi;
import org.onebusaway.io.client.elements.ObaRegion;
import org.onebusaway.io.client.elements.ObaRouteSchedule;
import org.onebusaway.io.client.elements.ObaStop;
import org.onebusaway.io.client.mock.MockRegion;
import org.onebusaway.io.client.request.ObaScheduleForStopRequest;
import org.onebusaway.io.client.request.ObaScheduleForStopResponse;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.HashMap;

@SuppressWarnings("serial")
public class ScheduleForStopTest extends ObaTestCase {

    // TODO - fix this test in context of regions and loading multiple URLs
    // Currently mixes Tampa URL with KCM data
	@Test
    public void testKCMStopRequest() throws UnsupportedEncodingException, URISyntaxException {
        // Test by setting region
        ObaRegion ps = MockRegion.getPugetSound();
        assertNotNull(ps);
        ObaApi.getDefaultContext().setRegion(ps);
        callKCMStopRequest();
    }

    private void callKCMStopRequest() throws UnsupportedEncodingException, URISyntaxException {
        ObaScheduleForStopRequest request =
                new ObaScheduleForStopRequest.Builder("1_75403")
                        .build();
        UriAssert.assertUriMatch(
        		DEFAULT_BASE_URL + "api/where/schedule-for-stop/1_75403.json",
                new HashMap<String, String>() {{
                    put("key", "*");
                    put("version", "2");
                }},
                request
        );
    }

    @Test
    public void testKCMStop() {
        ObaScheduleForStopResponse response =
                new ObaScheduleForStopRequest.Builder("1_75403")
                        .build()
                        .call();
        // This is just to ensure we can call it, but since we don't
        // know the day we can't really assume very much.
        assertOK(response);
        final ObaStop stop = response.getStop();
        assertEquals("1_75403", stop.getId());
        // TODO: This is no longer included?
        //final ObaStopSchedule.CalendarDay[] days = response.getCalendarDays();
        //assertTrue(days.length > 0);
        final ObaRouteSchedule[] schedules = response.getRouteSchedules();
        assertTrue(schedules.length > 0);
        final ObaRouteSchedule.Direction[] dirs = schedules[0].getDirectionSchedules();
        assertTrue(dirs.length > 0);
    }

    // TODO - fix this test in context of regions and loading multiple URLs
    // Currently mixes Tampa URL with KCM data
//    @Test
//    public void testKCMStopRequestWithDate() throws UnsupportedEncodingException, URISyntaxException {
//        // Test by setting region
//        ObaRegion ps = MockRegion.getPugetSound();
//        assertNotNull(ps);
//        ObaApi.getDefaultContext().setRegion(ps);
//        callKCMStopRequestWithDate();
//    }

//    private void callKCMStopRequestWithDate() throws UnsupportedEncodingException, URISyntaxException {
//    	Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
//    	c.set(Calendar.YEAR, 2012);
//    	c.set(Calendar.MONTH, 6);
//    	c.set(Calendar.DAY_OF_MONTH, 30);
//    	
//    	// TODO - conver to Java 8 date/time
//    	//ZonedDateTime z = ZonedDateTime.of(2012, 6, 30, 0,0,0,0, ZoneId.of("UTC")); // from values
//    	
//        ObaScheduleForStopRequest request =
//                new ObaScheduleForStopRequest.Builder("1_75403")
//                        .setDate(c.getTime())
//                        .build();
//        UriAssert.assertUriMatch(
//                DEFAULT_BASE_URL + "api/where/schedule-for-stop/1_75403.json",
//                new HashMap<String, String>() {{
//                    put("date", "2012-07-30");
//                    put("key", "*");
//                    put("version", "2");
//                }},
//                request
//        );
//    }
}
