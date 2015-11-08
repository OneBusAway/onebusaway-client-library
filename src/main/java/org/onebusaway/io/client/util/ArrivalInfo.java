/*
 * Copyright (C) 2010-2015 Paul Watts (paulcwatts@gmail.com),
 * University of South Florida (sjbarbeau@gmail.com)
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

import java.awt.*;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import static org.onebusaway.io.client.util.ArrivalInfo.LongDescription.*;
import static org.onebusaway.io.client.util.ArrivalInfo.Status.*;

/**
 * Class that transforms the raw arrival and departure information into human-readable colors and text
 */
public final class ArrivalInfo {

    final static class InfoComparator implements Comparator<ArrivalInfo> {

        public int compare(ArrivalInfo lhs, ArrivalInfo rhs) {
            return (int) (lhs.mEta - rhs.mEta);
        }
    }

    /**
     * Converts the ObaArrivalInfo array received from the server to an ArrayList for the adapter
     *
     * @param arrivalInfo
     * @param filter      routeIds to filter for
     * @param ms          current time in milliseconds
     * @return ArrayList of arrival info to be used with the adapter
     */
    public static final ArrayList<ArrivalInfo> convertObaArrivalInfo(ObaArrivalInfo[] arrivalInfo,
                                                                     ArrayList<String> filter, long ms) {
        final int len = arrivalInfo.length;
        ArrayList<ArrivalInfo> result = new ArrayList<ArrivalInfo>(len);
        if (filter != null && filter.size() > 0) {
            // Only add routes that haven't been filtered out
            for (int i = 0; i < len; ++i) {
                ObaArrivalInfo arrival = arrivalInfo[i];
                if (filter.contains(arrival.getRouteId())) {
                    ArrivalInfo info = new ArrivalInfo(arrival, ms);
                    result.add(info);
                }
            }
        } else {
            // Add arrivals for all routes
            for (int i = 0; i < len; ++i) {
                ArrivalInfo info = new ArrivalInfo(arrivalInfo[i], ms);
                result.add(info);
            }
        }

        // Sort by ETA
        Collections.sort(result, new InfoComparator());
        return result;
    }

    /**
     * Returns the index in the provided infoList for the first non-negative arrival ETA in the
     * list, or -1 if no non-negative ETAs exist in the list
     *
     * @param infoList list to search for non-negative arrival times, ordered by relative ETA from
     *                 negative infinity to positive infinity
     * @return the index in the provided infoList for the first non-negative arrival ETA in the
     * list, or -1 if no non-negative ETAs exist in the list
     */
    public static int findFirstNonNegativeArrival(ArrayList<ArrivalInfo> infoList) {
        for (int i = 0; i < infoList.size(); i++) {
            ArrivalInfo info = infoList.get(i);
            if (info.getEta() >= 0) {
                return i;
            }
        }
        // We didn't find any non-negative ETAs
        return -1;
    }

    private final ObaArrivalInfo mInfo;

    private final long mEta;

    private final long mDisplayTime;

    private final String mStatusText;

    private final String mLongDescription;

    private final Color mColor;

    private static final int ms_in_mins = 60 * 1000;

    private final boolean mPredicted;

    public ArrivalInfo(ObaArrivalInfo info, long now) {
        mInfo = info;
        // First, all times have to have to be converted to 'minutes'
        final long nowMins = now / ms_in_mins;
        long scheduled, predicted;
        // If this is the first stop in the sequence, show the departure time.
        if (info.getStopSequence() != 0) {
            scheduled = info.getScheduledArrivalTime();
            predicted = info.getPredictedArrivalTime();
        } else {
            scheduled = info.getScheduledDepartureTime();
            predicted = info.getPredictedDepartureTime();
        }

        final long scheduledMins = scheduled / ms_in_mins;
        final long predictedMins = predicted / ms_in_mins;

        if (predicted != 0) {
            mPredicted = true;
            mEta = predictedMins - nowMins;
            mDisplayTime = predicted;
        } else {
            mPredicted = false;
            mEta = scheduledMins - nowMins;
            mDisplayTime = scheduled;
        }

        mColor = computeColor(scheduled, predicted);

        mStatusText = computeStatusLabel(info, now, predicted,
                scheduledMins, predictedMins);

        mLongDescription = computeLongDescription();
    }

    /**
     * Returns the status color to be used, depending on whether the vehicle is running early,
     * late,
     * ontime,
     * or if we don't have real-time info (i.e., scheduled)
     *
     * @param scheduled the scheduled time
     * @param predicted the predicted time
     * @return the status color to be used, depending on whether the vehicle is running early, late,
     * ontime,
     * or if we don't have real-time info (i.e., scheduled)
     */
    public static Color computeColor(final long scheduled, final long predicted) {
        if (predicted != 0) {
            return computeColorFromDeviation(predicted - scheduled);
        } else {
            // Use scheduled color
            return Color.decode("#777");
        }
    }

    /**
     * Returns the status color to be used, depending on whether the vehicle is running early,
     * late,
     * ontime,
     * or if we don't have real-time info (i.e., scheduled)
     *
     * @param delay the deviation from the scheduled time - positive means bus is running late,
     *              negative means early
     * @return the status color to be used, depending on whether the vehicle is running early, late,
     * ontime,
     * or if we don't have real-time info (i.e., scheduled)
     */
    public static Color computeColorFromDeviation(final long delay) {
        // Bus is arriving
        if (delay > 0) {
            // Arriving delayed
            return Color.decode("#504caf");
        } else if (delay < 0) {
            // Arriving early
            return Color.decode("#af504c");
        } else {
            // Arriving on time
            return Color.decode("#4CAF50");
        }
    }

    private String computeStatusLabel(ObaArrivalInfo info,
                                      final long now,
                                      final long predicted,
                                      final long scheduledMins,
                                      final long predictedMins) {
        ObaArrivalInfo.Frequency frequency = info.getFrequency();

        StringBuilder sb = new StringBuilder();

        if (frequency != null) {

            int headwayAsMinutes = (int) (frequency.getHeadway() / 60);
            DateFormat formatter = DateFormat.getTimeInstance(DateFormat.SHORT);

            sb.append("Every ");
            sb.append(headwayAsMinutes);
            sb.append(" minutes ");
            long time = 0;

            if (now < frequency.getStartTime()) {
                time = frequency.getStartTime();
                sb.append("from ");
            } else {
                time = frequency.getEndTime();
                sb.append("until ");
            }
            String label = formatter.format(new Date(time));
            sb.append(label);

            return sb.toString();
        }

        if (predicted != 0) {
            long delay = predictedMins - scheduledMins;

            if (mEta >= 0) {
                // Bus is arriving
                return computeArrivalLabelFromDelay(delay);
            } else {
                // Bus is departing
                if (delay > 0) {
                    // Departing delayed
                    sb.append((int) delay);
                    sb.append(Status.MINUTE_DELAY);
                    return sb.toString();
                } else if (delay < 0) {
                    // Departing early
                    delay = -delay;
                    sb.append(Status.DEPARTED);
                    sb.append((int) delay);
                    if (delay < 2) {
                        sb.append(MINUTE_EARLY);
                    } else {
                        sb.append(MINUTES_EARLY);
                    }
                    return sb.toString();
                } else {
                    // Departing on time
                    return Status.ON_TIME;
                }
            }
        } else {
            if (mEta > 0) {
                return SCHEDULED_ARRIVAL;
            } else {
                return SCHEDULED_DEPARTURE;
            }
        }
    }

    private String computeLongDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append(ROUTE);
        sb.append(SPACE);
        sb.append(mInfo.getShortName());
        sb.append(SPACE);
        sb.append(mInfo.getHeadsign());
        sb.append(SPACE);

        if (mEta < 0) {
            // Route just left
            long invertEta = -mEta;
            sb.append(LongDescription.DEPARTED);
            sb.append(SPACE);
            sb.append(invertEta);
            sb.append(SPACE);
            if (invertEta < 2) {
                sb.append(MINUTE_AGO);
            } else {
                sb.append(MINUTES_AGO);
            }
        } else if (mEta == 0) {
            // Route is now arriving
            sb.append(IS_NOW_ARRIVING);
        } else {
            // Route is arriving in future
            sb.append(IS_ARRIVING_IN);
            sb.append(SPACE);
            sb.append(mEta);
            sb.append(SPACE);
            if (mEta < 2) {
                sb.append(MINUTE);
            } else {
                sb.append(MINUTES);
            }
        }

        // If its not real-time info, add statement about schedule
        if (!mPredicted) {
            sb.append(SPACE);
            sb.append(BASED_ON_SCHEDULE);
        }
        return sb.toString();
    }

    /**
     * Computes the arrival status label from the delay (i.e., schedule deviation), where positive
     * means the bus is running late and negative means the bus is running ahead of schedule
     *
     * @param delay schedule deviation, in minutes, for this vehicle where positive
     *              means the bus is running late and negative means the bus is running ahead of
     *              schedule
     * @return the arrival status label based on the deviation
     */
    public static String computeArrivalLabelFromDelay(long delay) {
        StringBuilder sb = new StringBuilder();
        if (delay > 0) {
            // Arriving delayed
            sb.append((int) delay);
            sb.append(Status.MINUTE_DELAY);
            return sb.toString();
        } else if (delay < 0) {
            // Arriving early
            delay = -delay;
            sb.append((int) delay);
            if (delay < 2) {
                sb.append(MINUTE_EARLY);
            } else {
                sb.append(MINUTES_EARLY);
            }
            return sb.toString();
        } else {
            // Arriving on time
            return Status.ON_TIME;
        }
    }

    public final ObaArrivalInfo getInfo() {
        return mInfo;
    }

    public final long getEta() {
        return mEta;
    }

    public final long getDisplayTime() {
        return mDisplayTime;
    }

    public final String getStatusText() {
        return mStatusText;
    }

    /**
     * Returns the color that should be used for the arrival time
     *
     * @return the color that should be used for the arrival time
     */
    public final Color getColor() {
        return mColor;
    }

    /**
     * Returns true if there is real-time arrival info available for this trip, false if there is not
     *
     * @return true if there is real-time arrival info available for this trip, false if there is not
     */
    public final boolean getPredicted() {
        return mPredicted;
    }

    /**
     * Returns a long description of this arrival information that is suitable for reading to a user
     * via a voice interface such as an IVR phone system
     *
     * @return a long description of this arrival information that is suitable for reading to a user
     * via a voice interface such as an IVR phone system
     */
    public final String getLongDescription() {
        return mLongDescription;
    }

    /**
     * Strings used to compose the status of an arrival or departure
     */
    public interface Status {
        String ON_TIME = "On time";
        String SCHEDULED_ARRIVAL = "Scheduled arrival";
        String SCHEDULED_DEPARTURE = "Scheduled departure";
        String MINUTE_DELAY = " minute delay";
        String DEPARTED = "Departed ";
        String MINUTE_EARLY = " minute early";
        String MINUTES_EARLY = " minutes early";
    }

    /**
     * Strings used to compose the long description of an arrival or departure
     */
    public interface LongDescription {
        String ROUTE = "Route";
        String SPACE = " ";
        String DEPARTED = "departed";
        String MINUTE_AGO = "minute ago";
        String MINUTES_AGO = "minutes ago";
        String IS_NOW_ARRIVING = "is now arriving";
        String IS_ARRIVING_IN = "is arriving in";
        String MINUTE = "minute";
        String MINUTES = "minutes";
        String BASED_ON_SCHEDULE = "based on the schedule";
    }
}