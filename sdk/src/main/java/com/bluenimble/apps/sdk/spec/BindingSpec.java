package com.bluenimble.apps.sdk.spec;

import java.io.Serializable;

public interface BindingSpec extends Serializable {
    
	String 			source();
	String []		property();
	
}
