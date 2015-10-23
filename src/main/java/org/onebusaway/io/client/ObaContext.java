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
package org.onebusaway.io.client;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import javax.ws.rs.core.UriBuilder;

import org.onebusaway.io.client.elements.ObaRegion;

public class ObaContext {

    private static final String TAG = "ObaContext";

    private String mApiKey = "TEST";

    private int mAppVer = 0;

    private String mAppUid = null;

    private ObaConnectionFactory mConnectionFactory = ObaDefaultConnectionFactory.getInstance();

    private ObaRegion mRegion;
    
    private String mBaseUrl;

    public ObaContext() {
    }

    public void setAppInfo(int version, String uuid) {
        mAppVer = version;
        mAppUid = uuid;
    }

    public void setAppInfo(UriBuilder builder) {
        if (mAppVer != 0) {
            builder.queryParam("app_ver", String.valueOf(mAppVer));
        }
        if (mAppUid != null) {
            builder.queryParam("app_uid", mAppUid);
        }
    }

    public void setApiKey(String apiKey) {
        mApiKey = apiKey;
    }

    public String getApiKey() {
        return mApiKey;
    }

    public void setRegion(ObaRegion region) {
        mRegion = region;
    }

    public ObaRegion getRegion() {
        return mRegion;
    }

    /**
     * Connection factory
     */
    public ObaConnectionFactory setConnectionFactory(ObaConnectionFactory factory) {
        ObaConnectionFactory prev = mConnectionFactory;
        mConnectionFactory = factory;
        return prev;
    }

    public ObaConnectionFactory getConnectionFactory() {
        return mConnectionFactory;
    }
    
    /**
     * Used by external classes to set the base URL
     * @param url
     */
    public void setBaseUrl(String url) {
    	mBaseUrl = url;
    }

    /**
     * Used by the various OBA request classes to build a full URL, using either the Region
     * set in this class or the Base URL set in this class (a set Region always overrides
     * a set URL).
     * @param builder A UriBuilder with the full relative path already set 
     * (e.g., "api/where/arrivals-and-departures-for-stop/1_1622.json".  After this method
     * returns, the builder will contain the full URL for the REST API endpoint (e.g., 
     * "http://api.tampa.onebusaway.org/api/where/arrivals-and-departures-for-stop/1_1622.json"
     * 
     * @throws URISyntaxException
     */
    public void buildFullUrl(UriBuilder builder) throws URISyntaxException {
//        URI baseUrl = null;
//        System.out.println("Using region base URL '" + mRegion.getObaBaseUrl() + "'.");
//
//        baseUrl = Uri.parse(mRegion.getObaBaseUrl());
//    
//        // Copy partial path (if one exists) from the base URL
//        UriBuilder path = new UriBuilder();
//        path.encodedPath(baseUrl.getEncodedPath());
//
//        // Then, tack on the rest of the REST API method path from the Uri.Builder that was passed in
//        path.appendEncodedPath(builder.build().getPath());
//
//        // Finally, overwrite builder that was passed in with the full URL
//        builder.scheme(baseUrl.getScheme());
//        builder.encodedAuthority(baseUrl.getEncodedAuthority());
//        builder.encodedPath(path.build().getEncodedPath());
	      URI baseUrl = null;
	      
	      if (mRegion != null) {
	    	  System.out.println("Using region base URL '" + mRegion.getObaBaseUrl() + "'.");
	    	  baseUrl = new URI(mRegion.getObaBaseUrl());
	      } else {
	    	  try {
                  // URI.parse() doesn't tell us if the scheme is missing, so use URL() instead (#126)
                  URL url = new URL(mBaseUrl);
              } catch (MalformedURLException e) {
                  // Assume HTTP scheme, since without a scheme the Uri won't parse the authority
            	  mBaseUrl = "http://" + mBaseUrl;
              }

              baseUrl = new URI(mBaseUrl);
	    	  System.out.println("Using set base URL - " + baseUrl);
	      }
	  
	      // Copy partial path (if one exists) from the base URL
	      UriBuilder path = UriBuilder.fromPath(baseUrl.getPath());
	
	      // Then, tack on the rest of the REST API method path from the Uri.Builder that was passed in
	      path.path(builder.build().getPath());
	
	      // Finally, overwrite builder that was passed in with the full URL
	      builder.uri(baseUrl);
	      builder.replacePath(path.build().getPath());
    }

    @Override
    public ObaContext clone() {
        ObaContext result = new ObaContext();
        result.setApiKey(mApiKey);
        result.setAppInfo(mAppVer, mAppUid);
        result.setConnectionFactory(mConnectionFactory);
        return result;
    }
}
