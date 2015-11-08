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
 * Retrieve info for a specific transit agency
 * {@link http://code.google.com/p/onebusaway/wiki/OneBusAwayRestApi_Agency}
 *
 * @author Paul Watts (paulcwatts@gmail.com)
 */
public final class ObaAgencyRequest extends RequestBase implements Callable<ObaAgencyResponse> {

    protected ObaAgencyRequest(URI uri) {
        super(uri);
    }

    public static class Builder extends RequestBase.BuilderBase {

        public Builder(String agencyId) {
            super(getPathWithId("/agency/", agencyId));
        }

        public ObaAgencyRequest build() {
            return new ObaAgencyRequest(buildUri());
        }
    }

    /**
     * Helper method for constructing new instances.
     *
     * @param agencyId The agency Id to request.
     * @return The new request instance.
     */
    public static ObaAgencyRequest newRequest(String agencyId) {
        return new Builder(agencyId).build();
    }

    @Override
    public ObaAgencyResponse call() {
        return call(ObaAgencyResponse.class);
    }


    @Override
    public String toString() {
        return "ObaAgencyRequest [mUri=" + mUri + "]";
    }
}
