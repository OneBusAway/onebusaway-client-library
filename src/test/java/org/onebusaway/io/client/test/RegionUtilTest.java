package org.onebusaway.io.client.test;

/**
 * Unit tests for the Regions Utilities
 */

import org.onebusaway.io.client.elements.ObaRegion;
import org.onebusaway.io.client.mock.MockRegion;
import org.onebusaway.io.client.util.LocationUtil;
import org.onebusaway.io.client.util.RegionUtils;
import org.onebusaway.location.Location;

import java.util.ArrayList;

/**
 * Tests to evaluate region utilities
 */
public class RegionUtilTest extends ObaTestCase {

    public static final float APPROXIMATE_DISTANCE_EQUALS_THRESHOLD = 2;  // meters

    // Mock regions to use in tests
    ObaRegion mPsRegion;

    ObaRegion mTampaRegion;

    ObaRegion mAtlantaRegion;

    ObaRegion mNewYorkRegion;

    // Locations to use in tests
    Location mSeattleLoc;

    Location mTampaLoc;

    Location mAtlantaLoc;

    Location mLondonLoc;

    Location mOriginLoc;

    Location mNewYorkLoc;

    @Override
    protected void setUp() {
        super.setUp();
        mPsRegion = MockRegion.getPugetSound();
        mTampaRegion = MockRegion.getTampa();
        mAtlantaRegion = MockRegion.getAtlanta();
        mNewYorkRegion = MockRegion.getNewYork();

        // Region locations
        mSeattleLoc = LocationUtil.makeLocation(47.6097, -122.3331);
        mTampaLoc = LocationUtil.makeLocation(27.9681, -82.4764);
        mAtlantaLoc = LocationUtil.makeLocation(33.7550, -84.3900);
        mNewYorkLoc = LocationUtil.makeLocation(40.712784, -74.005941);

        // Far locations
        mLondonLoc = LocationUtil.makeLocation(51.5072, -0.1275);
        mOriginLoc = LocationUtil.makeLocation(0, 0);
    }

    public void testGetDistanceAway() {
        float distance = RegionUtils.getDistanceAway(mPsRegion, mSeattleLoc);
        assertApproximateEquals(1210, distance);

        distance = RegionUtils.getDistanceAway(mTampaRegion, mTampaLoc);
        assertApproximateEquals(3160, distance);

        distance = RegionUtils.getDistanceAway(mAtlantaRegion, mAtlantaLoc);
        assertApproximateEquals(3927, distance);
    }

    public void testGetClosestRegion() {
        ArrayList<ObaRegion> list = new ArrayList<>();
        list.add(mPsRegion);
        list.add(mTampaRegion);
        list.add(mAtlantaRegion);
        list.add(mNewYorkRegion);

        boolean useLimiter = false;
        boolean enforceUsableRegions = true;

        /**
         * Without distance limiter - this should always return a region, no matter how far away
         * it is
         */
        // Close to region
        ObaRegion closestRegion = RegionUtils.getClosestRegion(list, mSeattleLoc, useLimiter, enforceUsableRegions);
        assertEquals(closestRegion.getId(), MockRegion.PUGET_SOUND_REGION_ID);

        closestRegion = RegionUtils.getClosestRegion(list, mTampaLoc, useLimiter, enforceUsableRegions);
        assertEquals(closestRegion.getId(), MockRegion.TAMPA_REGION_ID);

        closestRegion = RegionUtils.getClosestRegion(list, mAtlantaLoc, useLimiter, enforceUsableRegions);
        assertEquals(closestRegion.getId(), MockRegion.ATLANTA_REGION_ID);

        // Far from region
        closestRegion = RegionUtils.getClosestRegion(list, mLondonLoc, useLimiter, enforceUsableRegions);
        assertEquals(closestRegion.getId(), MockRegion.ATLANTA_REGION_ID);

        closestRegion = RegionUtils.getClosestRegion(list, mOriginLoc, useLimiter, enforceUsableRegions);
        assertEquals(closestRegion.getId(), MockRegion.TAMPA_REGION_ID);

        /**
         * With distance limiter - this should only return a region if its within
         * the RegionUtils.DISTANCE_LIMITER threshold, otherwise null should be returned
         */
        useLimiter = true;

        // Close to region
        closestRegion = RegionUtils.getClosestRegion(list, mSeattleLoc, useLimiter, enforceUsableRegions);
        assertEquals(closestRegion.getId(), MockRegion.PUGET_SOUND_REGION_ID);

        closestRegion = RegionUtils.getClosestRegion(list, mTampaLoc, useLimiter, enforceUsableRegions);
        assertEquals(closestRegion.getId(), MockRegion.TAMPA_REGION_ID);

        closestRegion = RegionUtils.getClosestRegion(list, mAtlantaLoc, useLimiter, enforceUsableRegions);
        assertEquals(closestRegion.getId(), MockRegion.ATLANTA_REGION_ID);

        // Far from region
        closestRegion = RegionUtils.getClosestRegion(list, mLondonLoc, useLimiter, enforceUsableRegions);
        assertNull(closestRegion);

        closestRegion = RegionUtils.getClosestRegion(list, mOriginLoc, useLimiter, enforceUsableRegions);
        assertNull(closestRegion);

        // Enforce usable regions, which by default should not use NYC due to not supporting the OBA real-time APIs
        enforceUsableRegions = true;
        closestRegion = RegionUtils.getClosestRegion(list, mNewYorkLoc, useLimiter, enforceUsableRegions);
        assertNull(closestRegion);

        // Turn off usable regions filter, which should give us NYC
        enforceUsableRegions = false;
        closestRegion = RegionUtils.getClosestRegion(list, mNewYorkLoc, useLimiter, enforceUsableRegions);
        assertEquals(closestRegion.getId(), MockRegion.NEW_YORK_REGION_ID);
    }

    public void testGetRegionSpan() {
        double[] results = new double[4];
        RegionUtils.getRegionSpan(mTampaRegion, results);

        assertApproximateEquals(0.542461f, (float) results[0]);
        assertApproximateEquals(0.576357f, (float) results[1]);
        assertApproximateEquals(27.9769105f, (float) results[2]);
        assertApproximateEquals(-82.445851f, (float) results[3]);
    }

    public void testIsLocationWithinRegion() {
        assertTrue(RegionUtils.isLocationWithinRegion(mSeattleLoc, mPsRegion));
        assertFalse(RegionUtils.isLocationWithinRegion(mTampaLoc, mPsRegion));
        assertFalse(RegionUtils.isLocationWithinRegion(mAtlantaLoc, mPsRegion));

        assertTrue(RegionUtils.isLocationWithinRegion(mTampaLoc, mTampaRegion));
        assertFalse(RegionUtils.isLocationWithinRegion(mSeattleLoc, mTampaRegion));
        assertFalse(RegionUtils.isLocationWithinRegion(mAtlantaLoc, mTampaRegion));

        assertTrue(RegionUtils.isLocationWithinRegion(mAtlantaLoc, mAtlantaRegion));
        assertFalse(RegionUtils.isLocationWithinRegion(mSeattleLoc, mAtlantaRegion));
        assertFalse(RegionUtils.isLocationWithinRegion(mTampaLoc, mAtlantaRegion));
    }

    public void testIsRegionUsable() {
        assertTrue(RegionUtils.isRegionUsable(mPsRegion));
        assertTrue(RegionUtils.isRegionUsable(mTampaRegion));
        assertTrue(RegionUtils.isRegionUsable(mAtlantaRegion));

        assertFalse(RegionUtils.isRegionUsable(MockRegion.getRegionWithoutObaApis()));
        assertFalse(RegionUtils.isRegionUsable(MockRegion.getInactiveRegion()));
        assertFalse(RegionUtils.isRegionUsable(MockRegion.getRegionNoObaBaseUrl()));
    }

    /**
     * Asserts that the expectedDistance is approximately equal to the actual distance, within
     * APPROXIMATE_DISTANCE_EQUALS_THRESHOLD
     */
    private void assertApproximateEquals(float expectedDistance, float actualDistance) {
        assertTrue(expectedDistance - APPROXIMATE_DISTANCE_EQUALS_THRESHOLD <= actualDistance &&
                actualDistance <= expectedDistance + APPROXIMATE_DISTANCE_EQUALS_THRESHOLD);
    }
}
