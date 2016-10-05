package com.bluenimble.apps.sdk.backend;

import java.io.Serializable;

import com.bluenimble.apps.sdk.json.JsonObject;

public interface Backend extends Serializable {

	Service 	lockup(String id);
	void 		register(String id, Service service);
	
	void		load(JsonObject spec);
	
	JsonObject 	getSpec(String id);
	void 		addSpec(String id, JsonObject spec);
	
	void		addVisitor(DataVisitor visitor);
	DataVisitor	getVisitor(String id);
	
}
