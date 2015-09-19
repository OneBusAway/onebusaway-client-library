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

import org.onebusaway.io.client.ObaApi;
import org.onebusaway.io.client.ObaConnectionFactory;
import org.onebusaway.io.client.elements.ObaRegion;

public class ObaMock {

    private final MockConnectionFactory mMockFactory;

    private final ObaConnectionFactory mOldFactory;

    private ObaRegion mOldRegion = null;

    private String mOldCustomApiUrl = null;

    public ObaMock() {
        mMockFactory = new MockConnectionFactory();
        mOldFactory = ObaApi.getDefaultContext().setConnectionFactory(mMockFactory);

        // Save the current region or custom API URL
        if (ObaApi.getDefaultContext().getRegion() != null) {
            mOldRegion = ObaApi.getDefaultContext().getRegion();
        } 
    }

    public void finish() {
        ObaApi.getDefaultContext().setConnectionFactory(mOldFactory);

        /*
         * Restore the previous region or custom API URL
         */
        if (mOldRegion == null && mOldCustomApiUrl == null) {
        	 ObaApi.getDefaultContext().setRegion(null);
            return;
        }

        // A region or a custom API was previously saved
        if (mOldRegion != null) {
        	 ObaApi.getDefaultContext().setRegion(mOldRegion);
        }
    }
}
