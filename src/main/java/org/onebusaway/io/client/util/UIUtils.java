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

package org.onebusaway.io.client.util;

import org.onebusaway.io.client.elements.ObaArrivalInfo;
import org.onebusaway.io.client.elements.ObaRoute;
import org.onebusaway.io.client.elements.ObaStop;
import org.onebusaway.util.comparators.AlphanumComparator;

import java.util.*;

/**
 * A class containing utility methods related to the user interface
 */
public class UIUtils {

    /**
     * Returns a comma-delimited list of route display names that serve a stop
     * <p/>
     * For example, if a stop was served by "14" and "54", this method will return "14,54"
     *
     * @param stop   the stop for which the route display names should be serialized
     * @param routes a HashMap containing all routes that serve this stop, with the routeId as the
     *               key.
     *               Note that for efficiency this routes HashMap may contain routes that don't
     *               serve this stop as well -
     *               the routes for the stop are referenced via stop.getRouteDisplayNames()
     * @return comma-delimited list of route display names that serve a stop
     */
    public static String serializeRouteDisplayNames(ObaStop stop,
                                                    HashMap<String, ObaRoute> routes) {
        StringBuffer sb = new StringBuffer();
        String[] routeIds = stop.getRouteIds();
        for (int i = 0; i < routeIds.length; i++) {
            if (routes != null) {
                ObaRoute route = routes.get(routeIds[i]);
                sb.append(getRouteDisplayName(route));
            } else {
                // We don't have route mappings - use routeIds
                sb.append(routeIds[i]);
            }

            if (i != routeIds.length - 1) {
                sb.append(",");
            }
        }

        return sb.toString();
    }

    /**
     * Returns a list of route display names from a serialized list of route display names
     * <p/>
     * See {@link #serializeRouteDisplayNames(ObaStop, java.util.HashMap)}
     *
     * @param serializedRouteDisplayNames comma-separate list of routeIds from serializeRouteDisplayNames()
     * @return list of route display names
     */
    public static List<String> deserializeRouteDisplayNames(String serializedRouteDisplayNames) {
        String routes[] = serializedRouteDisplayNames.split(",");
        return Arrays.asList(routes);
    }

    /**
     * Returns a formatted and sorted list of route display names for presentation in a single line
     * <p/>
     * For example, the following list:
     * <p/>
     * 11,1,15, 8b
     * <p/>
     * ...would be formatted as:
     * <p/>
     * 4, 8b, 11, 15
     *
     * @param routeDisplayNames          list of route display names
     * @param nextArrivalRouteShortNames the short route names of the next X arrivals at the stop
     *                                   that are the same.  These will be highlighted in the
     *                                   results.
     * @return a formatted and sorted list of route display names for presentation in a single line
     */
    public static String formatRouteDisplayNames(List<String> routeDisplayNames,
                                                 List<String> nextArrivalRouteShortNames) {
        Collections.sort(routeDisplayNames, new AlphanumComparator());
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < routeDisplayNames.size(); i++) {
            boolean match = false;

            for (String nextArrivalRouteShortName : nextArrivalRouteShortNames) {
                if (routeDisplayNames.get(i).equalsIgnoreCase(nextArrivalRouteShortName)) {
                    match = true;
                    break;
                }
            }

            if (match) {
                // If this route name matches a route name for the next X arrivals that are the same, highlight this route in the text
                sb.append(routeDisplayNames.get(i) + "*");
            } else {
                // Just append the normally-formatted route name
                sb.append(routeDisplayNames.get(i));
            }

            if (i != routeDisplayNames.size() - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    public static final String getRouteDisplayName(String routeShortName, String routeLongName) {
        if (routeShortName != null && routeShortName != "") {
            return routeShortName;
        }
        if (routeLongName != null && routeLongName != "") {
            return routeLongName;
        }
        // Just so we never return null.
        return "";
    }

    public static final String getRouteDisplayName(ObaRoute route) {
        return getRouteDisplayName(route.getShortName(), route.getLongName());
    }

    public static final String getRouteDisplayName(ObaArrivalInfo arrivalInfo) {
        return getRouteDisplayName(arrivalInfo.getShortName(), arrivalInfo.getRouteLongName());
    }

    public static final String getRouteDescription(ObaRoute route) {
        String shortName = route.getShortName();
        String longName = route.getLongName();

        if (shortName == null || shortName == "") {
            shortName = longName;
        }
        if (longName == null || longName == "" || shortName.equals(longName)) {
            longName = route.getDescription();
        }
        return MyTextUtils.toTitleCase(longName);
    }

    /**
     * Generates a summary of arrival/departure information in the following format:
     *
     * "The next bus on Route 45 university district east green lake is arriving in 2 minutes, and will arrive
     * again in 10 minutes, 18 minutes, and in 32 minutes based on the schedule.  Route 5 to UATC will depart in 5
     * minutes, and will depart again in 12 minutes based on the schedule, 20 minutes, and 35 minutes.
     *
     * @param arrivalInfo the arrival information to generate the summary text for
     * @param separator a string used as a separator between each arrival
     * @return a summary of arrival/departure information, with each arrival info summary separated by the provided
     * separator
     */
    public static final String getArrivalInfoSummary(List<ArrivalInfo> arrivalInfo, String separator) {
        StringBuilder keyBuilder = new StringBuilder();
        StringBuilder output = new StringBuilder();
        String key;

        /**
         * Create an ordered map to hold a grouping of route types, based on same route, headsign, arrival/departure,
         * and real-time/scheduled.  Key is generated so arrivals of the same grouping in the text can be matched, and
         * the value is a list of all the arrival info indexes that are in the same gruop.  After this mapping from key
         * to list of arrival info indexes is generated, we can loop through the map and generate similar text for each
         * group.
         */
        Map<String, List<Integer>> routeGroups = new LinkedHashMap<>(arrivalInfo.size());
        int index = 0;
        List<Integer> routeGroup;

        for (ArrivalInfo ai : arrivalInfo) {
            // Clear the key builder from the previous iteration
            keyBuilder.setLength(0);

            // Create a key for this arrival info using route, headsign, arrival/departure, and real-time/scheduled
            String routeAndHeadsign = ArrivalInfo.computeRouteAndHeadsign(ai.getInfo());
            keyBuilder.append(routeAndHeadsign);
            keyBuilder.append(ai.isArrival());
            keyBuilder.append(ai.getPredicted());
            key = keyBuilder.toString();

            routeGroup = routeGroups.get(key);

            if (routeGroup == null) {
                // Nothing yet for this key - create new list and add index for this arrival type to map
                routeGroup = new ArrayList<>();
                routeGroup.add(index);
                routeGroups.put(key, routeGroup);
            } else {
                // Add this index to the list of arrivals of same route, headsign, arrival/departure, and real-time/sch
                routeGroup.add(index);
            }
            index++;
        }

        // Now we have a map with routes grouped by similar traits - generate the output text by looping through it
        for (Map.Entry<String, List<Integer>> entry : routeGroups.entrySet()) {
            routeGroup = entry.getValue();
            System.out.println(entry.getKey() + ":");
            // If there is only one element in the group, just add the long description
            if (routeGroup.size() == 1) {
                output.append(arrivalInfo.get(routeGroup.get(0)).getLongDescription());
                output.append(separator);
                continue;
            }

            // If there are multiple elements in the group, combine them all in one sentence statement
            for (Integer i : routeGroup) {
                // TODO - organize text by past, now, and future predictions
                // TODO - if scheduled, add "all according to the schedule" to the end
                System.out.println("   " + arrivalInfo.get(i).getLongDescription());
            }
            output.append(separator);
        }

        return output.toString();
    }

}