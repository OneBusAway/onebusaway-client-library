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
import java.util.List;

import org.junit.Test;
import org.onebusaway.io.client.request.ObaShapeRequest;
import org.onebusaway.io.client.request.ObaShapeResponse;
import org.onebusaway.location.Location;

public class ShapeRequestTest extends ObaTestCase {

	@Test
    public void testShape() throws URISyntaxException, UnsupportedEncodingException {
        ObaShapeRequest.Builder builder =
                new ObaShapeRequest.Builder("1_40046045");
        ObaShapeRequest request = builder.build();
        ObaShapeResponse response = request.call();
        assertOK(response);
        assertTrue(response.getLength() > 0);
        final List<Location> points = response.getPoints();
        assertTrue(points.size() > 0);
    }

	@Test
    public void testNewRequest() throws UnsupportedEncodingException, URISyntaxException {
        // This is just to make sure we copy and call newRequest() at least once
        ObaShapeRequest request = ObaShapeRequest.newRequest("1_40046045");
        assertNotNull(request);
    }
}
