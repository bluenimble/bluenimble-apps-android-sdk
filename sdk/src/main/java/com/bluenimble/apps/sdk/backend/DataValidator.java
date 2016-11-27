package com.bluenimble.apps.sdk.backend;

import java.io.Serializable;

import com.bluenimble.apps.sdk.controller.DataHolder;
import com.bluenimble.apps.sdk.json.JsonObject;

public interface DataValidator extends Serializable {

	interface Spec {
		
		String Fields		= "fields";
		
		String Name 		= "name";
		String Title 		= "title";
		
		String Type 		= "type";
		
		String Value		= "value";
		
		String Required 	= "required";
		
		String Secret 		= "secret";
		
		String Min 			= "min";
		String Max 			= "max";

		String VType 		= "vType";
		String SType 		= "sType";
		
		String Format 		= "format";
		String TimeZone		= "timeZone";
		
		String Scope		= "scope";
		
		String ErrMsg		= "errMsg";

		String Facets		= "facets";

	}

	void validate (JsonObject spec, DataHolder dh) throws DataValidatorException;
	
}
