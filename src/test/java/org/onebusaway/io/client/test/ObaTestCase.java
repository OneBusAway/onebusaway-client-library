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

import org.onebusaway.io.client.ObaApi;
import org.onebusaway.io.client.mock.ObaMock;
import org.onebusaway.io.client.request.ObaResponse;

import junit.framework.TestCase;

public abstract class ObaTestCase extends TestCase {

    public static final String DEFAULT_BASE_URL = "https://raw.githubusercontent.com/OneBusAway/onebusaway-client-library/master/src/test/resources/";
	
	private ObaMock mMock;

    public static void assertOK(ObaResponse response) {
        assertNotNull(response);
        assertEquals(ObaApi.OBA_OK, response.getCode());
    }

    @Override
    protected void setUp() {
        mMock = new ObaMock();

        /*
         * Set to the mock Github URL where the test responses are remotely stored
         */        
        ObaApi.getDefaultContext().setBaseUrl(DEFAULT_BASE_URL);
    }

    @Override
    protected void tearDown() {
        mMock.finish();
    }
}
