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
import java.util.concurrent.Callable;

/**
 * Retrieve the list of all routes for a particular agency by ID.
 * {@link http://code.google.com/p/onebusaway/wiki/OneBusAwayRestApi_RouteIdsForAgency}
 *
 * @author Paul Watts (paulcwatts@gmail.com)
 */
public final class ObaRouteIdsForAgencyRequest extends RequestBase
        implements Callable<ObaRouteIdsForAgencyResponse> {

    protected ObaRouteIdsForAgencyRequest(URI uri) {
        super(uri);
    }

    public static class Builder extends RequestBase.BuilderBase {

        public Builder(String agencyId) {
            super(getPathWithId("/route-ids-for-agency/", agencyId));
        }

        public ObaRouteIdsForAgencyRequest build() {
            return new ObaRouteIdsForAgencyRequest(buildUri());
        }
    }

    /**
     * Helper method for constructing new instances.
     *
     * @param agencyId The agencyId to request.
     * @return The new request instance.
     */
    public static ObaRouteIdsForAgencyRequest newRequest(String agencyId) {
        return new Builder(agencyId).build();
    }

    @Override
    public ObaRouteIdsForAgencyResponse call() {
        return call(ObaRouteIdsForAgencyResponse.class);
    }

    @Override
    public String toString() {
        return "ObaRouteIdsForAgencyRequest [mUri=" + mUri + "]";
    }
}
