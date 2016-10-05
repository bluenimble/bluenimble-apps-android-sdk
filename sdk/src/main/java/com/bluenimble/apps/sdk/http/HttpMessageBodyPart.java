package com.bluenimble.apps.sdk.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

public interface HttpMessageBodyPart extends Serializable {

	String 			getName();
	String 			getFileName();
	String			getContentType();
	String			getTransferEncoding();
	InputStream		toInputStream();
	void 			dump(OutputStream output, String charset) throws IOException;
	void 			close();

}
