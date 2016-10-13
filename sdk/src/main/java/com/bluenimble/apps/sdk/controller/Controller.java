package com.bluenimble.apps.sdk.controller;

import java.io.Serializable;

public interface Controller extends Serializable {

	Action lockup(String id);
	void 	register(Action action);
	
}
