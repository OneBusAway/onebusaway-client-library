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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.ws.rs.core.UriBuilder;

import org.onebusaway.io.client.ObaApi;

public class Resources {

    public static URI getTestUri(String path) {
//    	UriBuilder builder = UriBuilder.fromPath(path);
//        return builder.build();
    	
//    	try {
//			URI uri = new URI("file:///test/" + path);
//			System.out.println("URI is: " + uri.toString());
//			return uri;
//		} catch (URISyntaxException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
    	URL url = Resources.class.getResource(path);
    	try {
			System.out.println("Looking for: " + path);
			return url.toURI();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return null;
    }

    /**
     * Read a resource by Uri
     */
    public static Reader read(URI uri) throws IOException {
        //InputStream stream = uri.toURL().openStream();
    	InputStream stream = new FileInputStream(uri.toString());
        InputStreamReader reader = new InputStreamReader(stream, "UTF-8");
        return reader;
    }

    public static <T> T readAs(URI uri, Class<T> cls) throws IOException {
        Reader reader = read(uri);
        ObaApi.SerializationHandler serializer = ObaApi.getSerializer(cls);
        T response = serializer.deserialize(reader, cls);
        return response;
    }
}
