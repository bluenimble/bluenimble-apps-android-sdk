package com.bluenimble.apps.sdk.http;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;

public interface HttpMessageBody extends Serializable {
	
	String CRLF = "\r\n"; // Line separator required by multipart/form-data.

	int 				count();
	HttpMessageBodyPart get(int index);
	void 				add(HttpMessageBodyPart part);
	void 				dump(OutputStream output, String charset, String boundary) throws IOException;

}
