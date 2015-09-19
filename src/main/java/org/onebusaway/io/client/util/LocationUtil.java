/*
 * Copyright (C) 2014 University of South Florida (sjbarbeau@gmail.com)
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
package org.onebusaway.io.client.util;

import java.util.concurrent.TimeUnit;

import org.onebusaway.io.client.ObaApi;
import org.onebusaway.io.client.ObaContext;
import org.onebusaway.io.client.elements.ObaRegion;
import org.onebusaway.location.Location;

/**
 * Utilities to help obtain and process location data
 *
 * @author barbeau
 */
public class LocationUtil {

    public static final String TAG = "LocationUtil";

    public static final int DEFAULT_SEARCH_RADIUS = 40000;

    private static final float FUZZY_EQUALS_THRESHOLD = 15.0f;

    public static final float ACC_THRESHOLD = 50f;  // 50 meters

    public static final long TIME_THRESHOLD = TimeUnit.MINUTES.toMillis(10);  // 10 minutes

    public static Location getDefaultSearchCenter() {
        ObaRegion region = ObaApi.getDefaultContext().getRegion();
        if (region != null) {
            double results[] = new double[4];
            RegionUtils.getRegionSpan(region, results);
            return LocationUtil.makeLocation(results[2], results[3]);
        } else {
            return null;
        }
    }

    /**
     * Compares Location A to Location B - prefers a non-null location that is more recent.  Does
     * NOT take estimated accuracy into account.
     * @param a first location to compare
     * @param b second location to compare
     * @return true if Location a is "better" than b, or false if b is "better" than a
     */
    public static boolean compareLocationsByTime(Location a, Location b) {
        return (a != null && (b == null || a.getTime() > b.getTime()));
    }

    /**
     * Compares Location A to Location B, considering timestamps and accuracy of locations.
     * Typically
     * this is used to compare a new location delivered by a LocationListener (Location A) to
     * a previously saved location (Location B).
     *
     * @param a location to compare
     * @param b location to compare against
     * @return true if Location a is "better" than b, or false if b is "better" than a
     */
    public static boolean compareLocations(Location a, Location b) {
        if (a == null) {
            // New location isn't valid, return false
            return false;
        }
        // If the new location is the first location, save it
        if (b == null) {
            return true;
        }

        // If the last location is older than TIME_THRESHOLD minutes, and the new location is more recent,
        // save the new location, even if the accuracy for new location is worse
        if (System.currentTimeMillis() - b.getTime() > TIME_THRESHOLD
                && compareLocationsByTime(a, b)) {
            return true;
        }

        // If the new location has an accuracy better than ACC_THRESHOLD and is newer than the last location, save it
        if (a.getAccuracy() < ACC_THRESHOLD && compareLocationsByTime(a, b)) {
            return true;
        }

        // If we get this far, A isn't better than B
        return false;
    }

    /**
     * Converts a latitude/longitude to a Location.
     *
     * @param lat The latitude.
     * @param lon The longitude.
     * @return A Location representing this latitude/longitude.
     */
    public static final Location makeLocation(double lat, double lon) {
        Location l = new Location("");
        l.setLatitude(lat);
        l.setLongitude(lon);
        return l;
    }

    /**
     * Returns true if the locations are approximately equal (i.e., within a certain distance
     * threshold)
     *
     * @param a first location
     * @param b second location
     * @return true if the locations are approximately equal, false if they are not
     */
    public static boolean fuzzyEquals(Location a, Location b) {
        return a.distanceTo(b) <= FUZZY_EQUALS_THRESHOLD;
    }
 
    /**
     * Returns the human-readable details of a Location (provider, lat/long, accuracy, timestamp)
     *
     * @return the details of a Location (provider, lat/long, accuracy, timestamp) in a string
     */
    public static String printLocationDetails(Location loc) {
        if (loc == null) {
            return "";
        }

        long timeDiff;
        double timeDiffSec;

        timeDiff = System.currentTimeMillis() - loc.getTime();
        timeDiffSec = timeDiff / 1E3;

        StringBuilder sb = new StringBuilder();
        sb.append(loc.getProvider());
        sb.append(' ');
        sb.append(loc.getLatitude());
        sb.append(',');
        sb.append(loc.getLongitude());
        if (loc.hasAccuracy()) {
            sb.append(' ');
            sb.append(loc.getAccuracy());
        }
        sb.append(", ");
        sb.append(String.format("%.0f", timeDiffSec) + " second(s) ago");

        return sb.toString();
    }
}
