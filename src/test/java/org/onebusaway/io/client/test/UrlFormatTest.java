package org.onebusaway.io.client.test;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Test;
import org.onebusaway.io.client.ObaApi;
import org.onebusaway.io.client.elements.ObaRegion;
import org.onebusaway.io.client.mock.MockRegion;
import org.onebusaway.io.client.request.ObaArrivalInfoRequest;
import org.onebusaway.io.client.util.RegionUtils;

/**
 * Tests various URL formats to ensure URI building/encoding is correct
 */
public class UrlFormatTest extends ObaTestCase {

	@Test
    public void testBasicUrlUsingRegion() throws UnsupportedEncodingException, URISyntaxException {
        /*
        Puget Sound fits this profile (i.e., http://api.pugetsound.onebusaway.org),
        so use Puget Sound base URL
        */
        ObaRegion region = MockRegion.getPugetSound();

        // Test by setting region
        ObaApi.getDefaultContext().setRegion(region);
        _assertBasicUrl();
    }

    private void _assertBasicUrl() throws UnsupportedEncodingException, URISyntaxException {
        ObaArrivalInfoRequest.Builder builder =
                new ObaArrivalInfoRequest.Builder("1_29261");
        ObaArrivalInfoRequest request = builder.build();
        UriAssert.assertUriMatch(
                "http://api.pugetsound.onebusaway.org/api/where/arrivals-and-departures-for-stop/1_29261.json",
                new HashMap<String, String>() {{
                    put("key", "*");
                    put("version", "2");
                }},
                request
        );
    }

    @Test
    public void testUrlWithSpacesUsingRegion() throws UnsupportedEncodingException, URISyntaxException {
        // Tampa fits this profile, so use Tampa URL
        ObaRegion region = MockRegion.getTampa();

        // Test by setting region
        ObaApi.getDefaultContext().setRegion(region);
        _assertUrlWithSpaces();
    }

    private void _assertUrlWithSpaces() throws URISyntaxException, UnsupportedEncodingException {
        // Spaces are included in agency name
        ObaArrivalInfoRequest.Builder builder =
                new ObaArrivalInfoRequest.Builder("Hillsborough Area Regional Transit_3105");
        ObaArrivalInfoRequest request = builder.build();
        UriAssert.assertUriMatch(
                "http://api.tampa.onebusaway.org/api/api/where/arrivals-and-departures-for-stop/Hillsborough%20Area%20Regional%20Transit_3105.json",
                new HashMap<String, String>() {{
                    put("key", "*");
                    put("version", "2");
                }},
                request
        );
    }

    @Test
    public void testUrlWithPathAndSeparatorUsingRegion() throws UnsupportedEncodingException, URISyntaxException {
        // Tampa fits this profile, so use Tampa URL
        ObaRegion region = MockRegion.getTampa();

        // Test by setting region
        ObaApi.getDefaultContext().setRegion(region);
        _assertUrlWithPathAndSeparator();
    }

    private void _assertUrlWithPathAndSeparator() throws URISyntaxException, UnsupportedEncodingException {
        ObaArrivalInfoRequest.Builder builder =
                new ObaArrivalInfoRequest.Builder("Hillsborough Area Regional Transit_3105");
        ObaArrivalInfoRequest request = builder.build();
        UriAssert.assertUriMatch(
                "http://api.tampa.onebusaway.org/api/api/where/arrivals-and-departures-for-stop/Hillsborough%20Area%20Regional%20Transit_3105.json",
                new HashMap<String, String>() {{
                    put("key", "*");
                    put("version", "2");
                }},
                request
        );
    }

    @Test
    public void testUrlWithPathNoSeparatorUsingRegion() throws UnsupportedEncodingException, URISyntaxException {
        ObaRegion region = MockRegion.getRegionWithPathNoSeparator();

        // Test by setting region
        ObaApi.getDefaultContext().setRegion(region);
        _assertUrlWithPathNoSeparator();
    }

    private void _assertUrlWithPathNoSeparator() throws URISyntaxException, UnsupportedEncodingException {
        ObaArrivalInfoRequest.Builder builder =
                new ObaArrivalInfoRequest.Builder("Hillsborough Area Regional Transit_3105");
        ObaArrivalInfoRequest request = builder.build();
        UriAssert.assertUriMatch(
                "http://api.tampa.onebusaway.org/api/api/where/arrivals-and-departures-for-stop/Hillsborough%20Area%20Regional%20Transit_3105.json",
                new HashMap<String, String>() {{
                    put("key", "*");
                    put("version", "2");
                }},
                request
        );
    }

    @Test
    public void testUrlNoSeparatorUsingRegion() throws UnsupportedEncodingException, URISyntaxException {
        ObaRegion region = MockRegion.getRegionNoSeparator();

        // Test by setting region
        ObaApi.getDefaultContext().setRegion(region);
        _assertUrlNoSeparator();
    }

    private void _assertUrlNoSeparator() throws URISyntaxException, UnsupportedEncodingException {
        ObaArrivalInfoRequest.Builder builder =
                new ObaArrivalInfoRequest.Builder("1_29261");
        ObaArrivalInfoRequest request = builder.build();
        UriAssert.assertUriMatch(
                "http://api.pugetsound.onebusaway.org/api/where/arrivals-and-departures-for-stop/1_29261.json",
                new HashMap<String, String>() {{
                    put("key", "*");
                    put("version", "2");
                }},
                request
        );
    }

    @Test
    public void testUrlWithPortUsingRegion() throws UnsupportedEncodingException, URISyntaxException {
        ObaRegion region = MockRegion.getRegionWithPort();

        // Test by setting region
        ObaApi.getDefaultContext().setRegion(region);
        _assertUrlWithPort();
    }

    private void _assertUrlWithPort() throws URISyntaxException, UnsupportedEncodingException {
        ObaArrivalInfoRequest.Builder builder =
                new ObaArrivalInfoRequest.Builder("Hillsborough Area Regional Transit_3105");
        ObaArrivalInfoRequest request = builder.build();
        UriAssert.assertUriMatch(
                "http://api.tampa.onebusaway.org:8088/api/api/where/arrivals-and-departures-for-stop/Hillsborough%20Area%20Regional%20Transit_3105.json",
                new HashMap<String, String>() {{
                    put("key", "*");
                    put("version", "2");
                }},
                request
        );
    }

    @Test
    public void testRegionBaseUrls() {
        // Checks all bundled region base URLs to make sure they are real URLs
        ArrayList<ObaRegion> regions = RegionUtils.getRegionsFromResources();
        for (ObaRegion r : regions) {
            try {
                URL url = new URL(r.getObaBaseUrl());
            } catch (MalformedURLException e) {
                fail("Region '" + r.getName() + "' has an invalid base URL: " + e.getMessage());
            }
        }
    }

    @Test
    public void testRegionTwitterUrls() {
        // Checks all bundled region Twitter URLs to make sure they are real URLs
        ArrayList<ObaRegion> regions = RegionUtils.getRegionsFromResources();
        for (ObaRegion r : regions) {
            try {
                if (r.getTwitterUrl() != null && !r.getTwitterUrl().isEmpty()) {
                    URL url = new URL(r.getTwitterUrl());
                }
            } catch (MalformedURLException e) {
                fail("Region '" + r.getName() + "' has an invalid Twitter URL: " + e.getMessage());
            }
        }
    }
}
