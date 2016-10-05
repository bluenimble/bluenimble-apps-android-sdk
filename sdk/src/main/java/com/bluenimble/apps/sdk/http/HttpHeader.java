package com.bluenimble.apps.sdk.http;

import java.io.Serializable;

public interface HttpHeader extends Serializable {
	
	String 			getName();
	String [] 		getValues();
}
