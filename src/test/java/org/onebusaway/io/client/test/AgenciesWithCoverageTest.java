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

import java.net.URISyntaxException;

import org.junit.Test;
import org.onebusaway.io.client.elements.ObaAgency;
import org.onebusaway.io.client.elements.ObaAgencyWithCoverage;
import org.onebusaway.io.client.request.ObaAgenciesWithCoverageRequest;
import org.onebusaway.io.client.request.ObaAgenciesWithCoverageResponse;

public class AgenciesWithCoverageTest extends ObaTestCase {

	@Test
    public void testRequest() {
        ObaAgenciesWithCoverageRequest request = null;
		try {
			request = ObaAgenciesWithCoverageRequest.newRequest();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        ObaAgenciesWithCoverageResponse response = request.call();
        assertOK(response);
        final ObaAgencyWithCoverage[] list = response.getAgencies();
        assertTrue(list.length > 0);
        for (ObaAgencyWithCoverage agency : list) {
            final ObaAgency a = response.getAgency(agency.getId());
            assertNotNull(a);
        }
    }

	@Test
    public void testBuilder() throws URISyntaxException {
        ObaAgenciesWithCoverageRequest.Builder builder =
                new ObaAgenciesWithCoverageRequest.Builder();
        ObaAgenciesWithCoverageRequest request = builder.build();
        assertNotNull(request);
    }
}
