package com.bluenimble.apps.sdk.spec;

import com.bluenimble.apps.sdk.json.JsonObject;

public interface LayerSpec extends EventAwareSpec, StylishSpec {
    
	String 			id();
	
	int 			count();
	ComponentSpec	component(int index);
	ComponentSpec	component(String id);

	boolean 		isGlobal();
	
	boolean			isRendered();
	
}
