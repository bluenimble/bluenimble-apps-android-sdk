package com.bluenimble.apps.sdk.controller;

import java.io.Serializable;

import com.bluenimble.apps.sdk.Lang.VariableResolver;
import com.bluenimble.apps.sdk.spec.ApplicationSpec;
import com.bluenimble.apps.sdk.spec.BindingSpec;

public interface DataHolder extends VariableResolver, Serializable {
	
	String Code 	= "code";
	String Message 	= "message";

	interface Namespace {
		String Error 	= "error";
		String App 		= "app";
		String Static 	= "static";
		String Device 	= "device";
	}
	
	Exception 	exception ();
	void 		exception(Exception exception);

	DataHolder	set (String namespace, Object value, String... property);
	Object		get (String namespace, String... property);
	
	Object		valueOf (ApplicationSpec applicationSpec, BindingSpec bindingSpec);
	
}
