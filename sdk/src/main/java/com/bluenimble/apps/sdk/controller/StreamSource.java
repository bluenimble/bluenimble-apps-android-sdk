package com.bluenimble.apps.sdk.controller;

import java.io.InputStream;
import java.io.Serializable;

public interface StreamSource extends Serializable {

	String 		id 			();
	String 		name 		();
	String 		contentType ();
	long 		length 		();

	InputStream stream 		();

	void		close		();

}

