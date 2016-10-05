package com.bluenimble.apps.sdk.http.request.impls;

import com.bluenimble.apps.sdk.http.HttpEndpoint;
import com.bluenimble.apps.sdk.http.HttpMethods;

public class PutRequest extends BodyAwareRequest {

	private static final long serialVersionUID = 6156567388966926923L;

	public PutRequest (HttpEndpoint endpoint) {
		super (HttpMethods.PUT, endpoint);
	}
	
}
