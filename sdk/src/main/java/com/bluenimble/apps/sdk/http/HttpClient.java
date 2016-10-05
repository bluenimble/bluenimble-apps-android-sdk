package com.bluenimble.apps.sdk.http;

import java.io.Serializable;

import com.bluenimble.apps.sdk.http.request.HttpRequest;
import com.bluenimble.apps.sdk.http.response.HttpResponse;

public interface HttpClient extends Serializable {

	HttpResponse 	send(HttpRequest request) throws HttpClientException;

}
