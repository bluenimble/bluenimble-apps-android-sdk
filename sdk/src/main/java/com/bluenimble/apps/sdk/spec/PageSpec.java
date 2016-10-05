package com.bluenimble.apps.sdk.spec;

import java.util.Iterator;

public interface PageSpec extends EventAwareSpec, StylishSpec {

	String 				name();
	
	int 				count();
	Iterator<String> 	layers();
	LayerSpec 			layer(String id);
	
}
