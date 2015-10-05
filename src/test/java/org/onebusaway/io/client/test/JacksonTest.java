/*
 * Copyright (C) 2012 individual contributors.
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

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Reader;

import org.onebusaway.io.client.JacksonSerializer;
import org.onebusaway.io.client.ObaApi;
import org.onebusaway.io.client.mock.Resources;
import org.onebusaway.io.client.request.ObaResponse;
import org.onebusaway.io.client.request.ObaStopsForLocationResponse;

public class JacksonTest extends ObaTestCase {

    private static final int mCode = 47421;

    private static final String mErrText = "Here is an error";

    protected JacksonSerializer mSerializer;

    public void testPrimitive() {
        mSerializer = (JacksonSerializer) JacksonSerializer.getInstance();
        String test = mSerializer.toJson("abc");
        assertEquals("\"abc\"", test);

        test = mSerializer.toJson("a\\b\\c");
        assertEquals("\"a\\\\b\\\\c\"", test);
    }

    public void testError() {
        mSerializer = (JacksonSerializer) JacksonSerializer.getInstance();
        ObaResponse response = mSerializer.createFromError(ObaResponse.class, mCode, mErrText);
        assertEquals(mCode, response.getCode());
        assertEquals(mErrText, response.getText());
    }

    public void testSerialization() {
        mSerializer = (JacksonSerializer) JacksonSerializer.getInstance();
        String errJson = mSerializer.serialize(new MockResponse());
        System.out.println("*** test - " + errJson);
        String expected = String
                .format("{\"code\":%d,\"version\":\"2\",\"text\":\"%s\"}", mCode, mErrText);
        System.out.println("*** expect - " + expected);
        assertEquals(expected, errJson);
    }

    public void testStopsForLocation() throws Exception {
        Reader reader = Resources
                .read("https://github.com/OneBusAway/onebusaway-client-library/raw/master/src/test/resources/api/where/stops_for_location_downtown_seattle1.json");
        ObaApi.SerializationHandler serializer = ObaApi
                .getSerializer(ObaStopsForLocationResponse.class);
        ObaStopsForLocationResponse response = serializer
                .deserialize(reader, ObaStopsForLocationResponse.class);
        assertNotNull(response);
    }

    @JsonPropertyOrder(value = {"code", "version", "text"})
    public class MockResponse {

        @SuppressWarnings("unused")
        private final String version;

        @SuppressWarnings("unused")
        private final int code;

        @SuppressWarnings("unused")
        private final String text;

        protected MockResponse() {
            version = "2";
            code = mCode;
            text = mErrText;
        }
    }
}
