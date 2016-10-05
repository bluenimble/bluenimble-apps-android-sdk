package com.bluenimble.apps.sdk.ui.components;

import java.io.Serializable;

public interface ComponentsRegistry extends Serializable {
	
	interface Default {
		String Page 		= "page";
		String Layer 		= "layer";
		String Break 		= "break";
		String Text 		= "text";
	}

	ComponentFactory 	lookup(String id);
	void 				register(ComponentFactory factory);
	
}
