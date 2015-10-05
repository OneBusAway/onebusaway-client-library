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

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.core.UriBuilder;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.onebusaway.io.client.ObaConnection;
import org.onebusaway.io.client.ObaConnectionFactory;

public class MockConnectionFactory implements ObaConnectionFactory {

    private final UriMap mUriMap;

    public MockConnectionFactory() {
        try {
            mUriMap = Resources.readAs("urimap.json", UriMap.class);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to read urimap: " + e);
        }
    }

    public ObaConnection newConnection(URI uri) throws IOException {
        return new MockConnection(mUriMap, uri);
    }

    public static class UriMap {

        //
        // Normalize request URI.
        // This removes the scheme, host and port,
        // It removes any query parameters we know don't matter to the request
        // like the version, API key and so forth.
        // Then sort the remaining query parameters.
        //
        private static final List<String> PARAMS_LIST =
                Arrays.asList("version", "key", "app_ver", "app_uid");

        private HashMap<String, String> uris;

        public String getUri(URI uri) {
            String normalizedUri = normalizeUri(uri);
            String result = null;
//            if (uris == null) {
//                throw new RuntimeException("No uris in URIMap -- did the file parse correctly?");
//            }
//            result = uris.get(normalizedUri);
//            if (result == null) {
//                throw new RuntimeException("No response for URI: " + normalizedUri);
//            }
            return normalizedUri;
        }

        private String normalizeUri(URI uri) {
            UriBuilder builder = UriBuilder.fromUri(uri);
            // Remove all query parameters, as they interfere with getting file from Github
            builder.replaceQuery(null);

//            List<NameValuePair> params = URLEncodedUtils.parse(uri, "UTF-8");
//            params.removeAll(PARAMS_LIST);
//            for (NameValuePair pair : params) {
//                builder.queryParam(pair.getName(), pair.getValue());
//            }

            return builder.build().toString();
        }
    }
}
