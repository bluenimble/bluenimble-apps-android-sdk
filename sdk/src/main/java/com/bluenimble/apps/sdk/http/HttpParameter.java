package com.bluenimble.apps.sdk.http;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

public interface HttpParameter extends Serializable {
	
	String 	getName();
	Object	getValue();
	
	String	dump(StringBuilder sb, String encoding) throws UnsupportedEncodingException;

}
