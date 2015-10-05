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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;

import javax.ws.rs.core.UriBuilder;

import org.onebusaway.io.client.ObaApi;

public class Resources {

    /**
     * Read a resource by URL
     * @param urlString a String for the URL
     * @return a Reader for the provided URL
     */
    public static Reader read(String urlString) throws IOException {
    	System.out.println("Fetching: " + urlString);
    	URL url = new URL(urlString);
        URLConnection uc = url.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(
                                    uc.getInputStream()));
        return in;
    }

    public static <T> T readAs(String path, Class<T> cls) throws IOException {
    	System.out.println("Reading file: " + path);
    	InputStream stream = Resources.class.getClassLoader().getResourceAsStream(path);
    	if (stream == null) {
    		System.err.println("InputStream is null - file not found - " + path);
    	}
        InputStreamReader reader = new InputStreamReader(stream, "UTF-8");
        ObaApi.SerializationHandler serializer = ObaApi.getSerializer(cls);
        T response = serializer.deserialize(reader, cls);
        return response;
    }
}
