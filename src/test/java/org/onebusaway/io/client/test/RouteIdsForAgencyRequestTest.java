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
import org.onebusaway.io.client.request.ObaRouteIdsForAgencyRequest;
import org.onebusaway.io.client.request.ObaRouteIdsForAgencyResponse;

public class RouteIdsForAgencyRequestTest extends ObaTestCase {

	@Test
    public void testST() {
        ObaRouteIdsForAgencyRequest.Builder builder =
                new ObaRouteIdsForAgencyRequest.Builder("40");
        ObaRouteIdsForAgencyRequest request = builder.build();
        ObaRouteIdsForAgencyResponse response = request.call();
        assertOK(response);

        final String[] routeIds = response.getRouteIds();
        assertNotNull(routeIds);
        assertTrue(routeIds.length > 0);
        assertFalse(response.getLimitExceeded());
    }

	@Test
    public void testNewRequest() {
        // This is just to make sure we copy and call newRequest() at least once
        ObaRouteIdsForAgencyRequest request =
                ObaRouteIdsForAgencyRequest.newRequest("40");
        assertNotNull(request);
    }
}
