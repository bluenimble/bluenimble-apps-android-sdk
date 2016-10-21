package com.bluenimble.apps.sdk.controller;

import java.io.InputStream;
import java.io.Serializable;

import com.bluenimble.apps.sdk.Lang.VariableResolver;
import com.bluenimble.apps.sdk.spec.ApplicationSpec;
import com.bluenimble.apps.sdk.spec.BindingSpec;

public interface DataHolder extends VariableResolver, Serializable {

	public static final
	
	String Code 			= "code";
	String Message 			= "message";

	String DefaultStreamId 	= "stream";

	interface Namespace {
		String Error 	= "error";

		String App 		= "app";
		String Static 	= "static";
		String Device 	= "device";
		String Internal = "$INTERNAL";

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

	void 			close		();
	
}
