package com.bluenimble.apps.sdk.http.request.impls;

import com.bluenimble.apps.sdk.http.HttpEndpoint;
import com.bluenimble.apps.sdk.http.HttpMethods;

public class DeleteRequest extends NoBodyAwareRequest {

	private static final long serialVersionUID = 6156567388966926923L;

	public DeleteRequest (HttpEndpoint endpoint) {
		super (HttpMethods.DELETE, endpoint);
	}
	
}
