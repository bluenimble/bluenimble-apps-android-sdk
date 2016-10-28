package com.bluenimble.apps.sdk.spec;

import com.bluenimble.apps.sdk.json.JsonObject;

import java.io.Serializable;

public interface SecuritySpec extends Serializable {

	PageSpec 	accessDenied	();
	void		get 			(String... property);

}
