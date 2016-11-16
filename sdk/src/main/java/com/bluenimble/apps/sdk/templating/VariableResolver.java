package com.bluenimble.apps.sdk.templating;

import java.io.Serializable;

public interface VariableResolver extends Serializable {

	Object resolve (String namespace, String... property);
	
}
