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
package org.onebusaway.io.client.request;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Callable;

/**
 * Retrieve the full schedule for a stop on a particular day
 * {@link http://code.google.com/p/onebusaway/wiki/OneBusAwayRestApi_ScheduleForStop}
 *
 * @author Paul Watts (paulcwatts@gmail.com)
 */
public final class ObaScheduleForStopRequest extends RequestBase
        implements Callable<ObaScheduleForStopResponse> {

    protected ObaScheduleForStopRequest(URI uri) {
        super(uri);
    }

    public static class Builder extends RequestBase.BuilderBase {

        public Builder(String stopId) {
            super(getPathWithId("/schedule-for-stop/", stopId));
        }

        /**
         * Sets the requested date. Defaults to the current date.
         *
         * @param date The requested date.
         * @return This object.
         */
        public Builder setDate(Date date) {
            mBuilder.queryParam("date", new SimpleDateFormat("yyyy-MM-dd").format(date));
            return this;
        }

        public ObaScheduleForStopRequest build() {
            return new ObaScheduleForStopRequest(buildUri());
        }
    }

    @Override
    public ObaScheduleForStopResponse call() {
        return call(ObaScheduleForStopResponse.class);
    }

    @Override
    public String toString() {
        return "ObaScheduleForStopRequest [mUri=" + mUri + "]";
    }
}
