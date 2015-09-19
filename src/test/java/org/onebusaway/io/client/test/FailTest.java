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
package org.onebusaway.io.client.test;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.Callable;

import org.junit.Test;
import org.onebusaway.io.client.ObaApi;
import org.onebusaway.io.client.request.ObaResponse;
import org.onebusaway.io.client.request.ObaStopRequest;
import org.onebusaway.io.client.request.ObaStopResponse;
import org.onebusaway.io.client.request.RequestBase;

public class FailTest extends ObaTestCase {

	@Test
    public void test404_1() throws UnsupportedEncodingException, URISyntaxException {
        ObaStopResponse response = ObaStopRequest.newRequest("404test").call();
        assertNotNull(response);
        // Right now this is what is in the test response...
        assertEquals(ObaApi.OBA_INTERNAL_ERROR, response.getCode());
    }

    // This is a real 404
	@Test
    public void test404_2() throws URISyntaxException {
        BadResponse response =
                new BadRequest.Builder("/foo/1_29261.json").build().call();
        assertNotNull(response);
        assertEquals(ObaApi.OBA_NOT_FOUND, response.getCode());
    }

	@Test
    public void testBadJson() throws URISyntaxException {
        BadResponse response =
                new BadRequest.Builder("/stop/1_29261.xml").build().call();
        assertNotNull(response);
        assertEquals(ObaApi.OBA_INTERNAL_ERROR, response.getCode());
    }

    //
    // We can create our own test request that points to an invalid path.
    //
    private static class BadResponse extends ObaResponse {

    }

    private static class BadRequest extends RequestBase
            implements Callable<BadResponse> {

        protected BadRequest(URI uri) {
            super(uri);
        }

        @Override
        public BadResponse call() {
            return call(BadResponse.class);
        }

        static class Builder extends RequestBase.BuilderBase {

            public Builder(String path) {
                super(BASE_PATH + path);
            }

            public BadRequest build() throws URISyntaxException {
                return new BadRequest(buildUri());
            }
        }
    }

}
