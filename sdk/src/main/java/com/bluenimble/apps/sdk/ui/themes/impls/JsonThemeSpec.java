package com.bluenimble.apps.sdk.ui.themes.impls;

import com.bluenimble.apps.sdk.Json;
import com.bluenimble.apps.sdk.json.JsonObject;
import com.bluenimble.apps.sdk.spec.ThemeSpec;

import android.util.Log;

public class JsonThemeSpec implements ThemeSpec {

	private static final long serialVersionUID = 2424890613220031730L;
	
	protected String 		id;
	protected JsonObject 	spec;
	
	public JsonThemeSpec (String id, JsonObject spec) {
		this.id 	= id;
		this.spec = spec;
	}
	
	@Override
	public String id () {
		return id;
	}

	@Override
	public JsonObject style (String key) {
		return Json.getObject (spec, key);
	}
	
	@Override
	public void add (String key, JsonObject style) {
		if (spec == null) {
			spec = new JsonObject ();
		}
 		spec.put (key, style);
	}
	
	@Override
	public void remove (String key) {
		if (spec == null) {
			return;
		}
		spec.remove (key);
	}
	
	@Override
	public ThemeSpec merge (ThemeSpec another) {
		if (!(another instanceof JsonThemeSpec)) {
			return this;
		}
		if (spec == null) {
			spec = ((JsonThemeSpec)another).spec;
			return this;
		}
		spec = spec.merge (((JsonThemeSpec)another).spec);
		return this;
	}
	
}
