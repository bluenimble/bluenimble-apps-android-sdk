package com.bluenimble.apps.sdk.spec;

import java.io.Serializable;

import com.bluenimble.apps.sdk.json.JsonObject;

public interface ThemeSpec extends Serializable {
	
	String 		Default = "default";

	String 		id();
	
	JsonObject	style(String key);
	
	void		add(String key, JsonObject spec);

	void		remove(String key);
	
	ThemeSpec	merge(ThemeSpec another);

}
