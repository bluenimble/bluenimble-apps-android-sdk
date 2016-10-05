package com.bluenimble.apps.sdk.http.request.impls;

import com.bluenimble.apps.sdk.http.HttpEndpoint;
import com.bluenimble.apps.sdk.http.HttpMethods;

public class GetRequest extends NoBodyAwareRequest {

	private static final long serialVersionUID = 6156567388966926923L;

	public GetRequest (HttpEndpoint endpoint) {
		super (HttpMethods.GET, endpoint);
	}
	
}
