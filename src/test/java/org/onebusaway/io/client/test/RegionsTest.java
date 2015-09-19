package org.onebusaway.io.client.test;

import java.net.URISyntaxException;

import org.junit.Test;
import org.onebusaway.io.client.elements.ObaRegion;
import org.onebusaway.io.client.request.ObaRegionsRequest;
import org.onebusaway.io.client.request.ObaRegionsResponse;

/**
 * Tests Regions API requests and responses
 */
public class RegionsTest extends ObaTestCase {

	@Test
    public void testRequest() throws URISyntaxException {
        ObaRegionsRequest request =
                ObaRegionsRequest.newRequest();
        ObaRegionsResponse response = request.call();
        assertOK(response);
        final ObaRegion[] list = response.getRegions();
        assertTrue(list.length > 0);
        for (ObaRegion region : list) {
            assertNotNull(region.getName());
        }
    }

	@Test
    public void testBuilder() throws URISyntaxException {
        ObaRegionsRequest.Builder builder =
                new ObaRegionsRequest.Builder();
        ObaRegionsRequest request = builder.build();
        assertNotNull(request);
    }
}
