/*
 * Copyright (C) 2012 Paul Watts (paulcwatts@gmail.com)
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
package org.onebusaway.io.client.mock;

import org.onebusaway.io.client.ObaConnection;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URI;

public class MockConnection implements ObaConnection {

    private static final String TAG = "MockConnection";

    private final MockConnectionFactory.UriMap mUriMap;

    private final URI mUri;

    private int mResponseCode = HttpURLConnection.HTTP_OK;

    MockConnection(MockConnectionFactory.UriMap map,  URI uri) {
        mUriMap = map;
        mUri = uri;
    }

    public void disconnect() {
    }

    public Reader get() throws IOException {
        System.out.println("Get URI: " + mUri);
        // Find a mock response for this URI.
        String response = mUriMap.getUri(mUri);
        if ("__404__".equals(response)) {
            mResponseCode = HttpURLConnection.HTTP_NOT_FOUND;
            throw new FileNotFoundException();
        }
        return Resources.read(response);
    }

    public Reader post(String string) throws IOException {
        throw new RuntimeException("Not implemented");
    }

    public int getResponseCode() throws IOException {
        return mResponseCode;
    }
}
