package com.bluenimble.apps.sdk.json;

import java.io.Serializable;

import com.bluenimble.apps.sdk.Null;

public interface JsonEntity extends Serializable {

	public static final Null		NULL 			= new Null ();
	
	public static final String 	 	NULL_VALUE 		= "null";
	public static final String 	 	FALSE 			= "false";
	public static final String 	 	TRUE 			= "true";
	public static final String 	 	EMPTY_OBJECT 	= "{}";
	public static final String 	 	EMPTY_ARRAY 	= "{}";
	
	public static final String 		FIND_SEP 		= "/";
	
	public static final String 		PRINT_AS_ARRAY 	= "PrintAsArray";
	
	JsonEntity 	set(String name, Object value);
	void 		clear();
}
