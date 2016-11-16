package com.bluenimble.apps.sdk.templating;

import java.io.Serializable;

public interface ExpressionCompiler extends Serializable {

	Expression 	compile(String expression, String id);
	
}
