package com.bluenimble.apps.sdk.controller;

import java.io.Serializable;

import com.bluenimble.apps.sdk.templating.VariableResolver;
import com.bluenimble.apps.sdk.spec.ApplicationSpec;
import com.bluenimble.apps.sdk.spec.BindingSpec;

public interface DataHolder extends VariableResolver, Serializable {
	
	String Code 			= "code";
	String Message 			= "message";

	String DefaultStreamId 	= "stream";

	interface Namespace {
		String Internal = "$INTERNAL";

		String Error 	= "error";

		String Static 	= "static";
		String Device 	= "device";
		String View 	= "view";
		String Event 	= "event";

		String Streams 	= "streams";
	}

	interface Internal {
		String NoTag = "noTag";
	}
	
	Exception 		exception 	();
	void 			exception	(Exception exception);

	DataHolder		set 		(String namespace, Object value, String... property);
	Object			get 		(String namespace, String... property);

	void			stream 		(StreamSource stream);
	StreamSource	stream 		(String name);

	Object			valueOf 	(ApplicationSpec applicationSpec, BindingSpec bindingSpec);

	void 			destroy		();
	DataHolder		clone		();

}
