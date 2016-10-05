package com.bluenimble.apps.sdk.spec.impls.json;

import com.bluenimble.apps.sdk.Json;
import com.bluenimble.apps.sdk.Lang;
import com.bluenimble.apps.sdk.Spec;
import com.bluenimble.apps.sdk.controller.DataHolder;
import com.bluenimble.apps.sdk.json.JsonObject;
import com.bluenimble.apps.sdk.spec.BindingSpec;

public class JsonBindingSpec implements BindingSpec {

	private static final long serialVersionUID = 6754533995446370576L;
	
	protected JsonObject 	spec;
	protected String [] 	property;
	
	public JsonBindingSpec (JsonObject spec) {
		this.spec = spec;
		property = Lang.split (Json.getString (spec, Spec.page.layer.component.binding.Property), Lang.DOT);
	}

	@Override
	public String source () {
		return Json.getString (spec, Spec.page.layer.component.binding.Source, DataHolder.Namespace.Static);
	}

	@Override
	public String [] property () {
		return property;
	}

}
