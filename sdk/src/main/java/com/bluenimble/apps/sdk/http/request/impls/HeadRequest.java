package com.bluenimble.apps.sdk.http.request.impls;

import com.bluenimble.apps.sdk.http.HttpEndpoint;
import com.bluenimble.apps.sdk.http.HttpMethods;

public class HeadRequest extends NoBodyAwareRequest {

	private static final long serialVersionUID = 6156567388966926923L;

	public HeadRequest (HttpEndpoint endpoint) {
		super (HttpMethods.HEAD, endpoint);
	}
	
}
