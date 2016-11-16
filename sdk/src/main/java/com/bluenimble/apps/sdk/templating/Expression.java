package com.bluenimble.apps.sdk.templating;

import java.io.Serializable;

public interface Expression extends Serializable {

	Object 	eval(VariableResolver vr);
	
	void	node(Node node);
	Node	node(int index);
	
	int		nodes();

}
