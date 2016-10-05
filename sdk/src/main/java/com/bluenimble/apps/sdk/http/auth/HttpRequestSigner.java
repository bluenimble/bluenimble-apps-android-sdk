package com.bluenimble.apps.sdk.http.auth;

import java.io.Serializable;

import com.bluenimble.apps.sdk.http.request.HttpRequest;

public interface HttpRequestSigner extends Serializable {

	void sign(HttpRequest request) throws HttpRequestSignerException;
	
}
