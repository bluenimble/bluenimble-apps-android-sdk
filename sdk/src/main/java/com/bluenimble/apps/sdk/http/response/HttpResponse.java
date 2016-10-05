package com.bluenimble.apps.sdk.http.response;

import com.bluenimble.apps.sdk.http.HttpMessage;

public interface HttpResponse extends HttpMessage {

	String 				getId();
	int 				getStatus();

}
