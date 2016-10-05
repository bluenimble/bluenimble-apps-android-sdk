package com.bluenimble.apps.sdk.http;

import java.io.Serializable;
import java.util.List;

public interface HttpMessage extends Serializable {
	
	String 				getContentType();
	String 				getCharset();
	List<HttpHeader> 	getHeaders();
	HttpMessageBody		getBody();
	HttpHeader 			getHeader(String name);

}
