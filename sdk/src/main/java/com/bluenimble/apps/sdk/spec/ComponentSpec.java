package com.bluenimble.apps.sdk.spec;

public interface ComponentSpec extends EventAwareSpec, StylishSpec {

	enum Binding {
		Set,
		Get
	}
	
	String 				type 	();
	Object 				get 	(String name);

	LayerSpec			layer 	();
	
	BindingSpec			binding	(Binding binding);

}
