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
import org.onebusaway.io.client.ObaApi;
import org.onebusaway.io.client.request.ObaCurrentTimeRequest;
import org.onebusaway.io.client.request.ObaCurrentTimeResponse;

public class CurrentTimeRequestTest extends ObaTestCase {

	@Test
    public void testCurrentTime() throws URISyntaxException {
        ObaCurrentTimeRequest.Builder builder = new ObaCurrentTimeRequest.Builder();
        ObaCurrentTimeRequest request = builder.build();
        ObaCurrentTimeResponse response = request.call();
        assertOK(response);
        final long time = response.getTime();
        assertTrue(time > 0);

        final String readableTime = response.getReadableTime();
        assertNotNull(readableTime);
    }

	@Test
    public void testNewRequest() throws URISyntaxException {
        // This is just to make sure we copy and call newRequest() at least once
        ObaCurrentTimeRequest request = ObaCurrentTimeRequest.newRequest();
        assertNotNull(request);
    }
}
