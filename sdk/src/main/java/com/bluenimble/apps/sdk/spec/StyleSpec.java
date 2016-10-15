package com.bluenimble.apps.sdk.spec;

import java.io.Serializable;

import android.view.View;
import android.view.ViewGroup;

public interface StyleSpec extends Serializable {

	Object 	get		(String name);
	
	void 	apply 	(StylishSpec stylish, View view, ViewGroup parent);
	
}
