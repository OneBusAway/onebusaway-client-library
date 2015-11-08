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
 * Retrieve a shape (the path traveled by a transit vehicle) by ID.
 * {@link http://code.google.com/p/onebusaway/wiki/OneBusAwayRestApi_Shape}
 *
 * @author Paul Watts (paulcwatts@gmail.com)
 */
public final class ObaShapeRequest extends RequestBase implements Callable<ObaShapeResponse> {

    protected ObaShapeRequest(URI uri) {
        super(uri);
    }

    public static class Builder extends RequestBase.BuilderBase {

        public Builder(String shapeId) {
            super(getPathWithId("/shape/", shapeId));
        }

        public ObaShapeRequest build() {
            return new ObaShapeRequest(buildUri());
        }
    }

    /**
     * Helper method for constructing new instances.
     *
     * @param stopId The stopId to request.
     * @return The new request instance.
     */
    public static ObaShapeRequest newRequest(String stopId) {
        return new Builder(stopId).build();
    }

    @Override
    public ObaShapeResponse call() {
        return call(ObaShapeResponse.class);
    }

    @Override
    public String toString() {
        return "ObaShapeRequest [mUri=" + mUri + "]";
    }
}
