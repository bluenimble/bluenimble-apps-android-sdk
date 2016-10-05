package com.bluenimble.apps.sdk.spec.impls.json;

import com.bluenimble.apps.sdk.Json;
import com.bluenimble.apps.sdk.Spec;
import com.bluenimble.apps.sdk.json.JsonObject;
import com.bluenimble.apps.sdk.spec.SdkVersion;

public class JsonSdkVersion implements SdkVersion {

	private static final long serialVersionUID = 6754533995446370576L;
	
	protected JsonObject spec;
	
	public JsonSdkVersion (JsonObject spec) {
		this.spec = spec;
	}
	
	@Override
	public int major () {
		return Json.getInteger (spec, Spec.sdkVersion.Major, 1);
	}

	@Override
	public int minor () {
		return Json.getInteger (spec, Spec.sdkVersion.Minor, 0);
	}

	@Override
	public int patch () {
		return Json.getInteger (spec, Spec.sdkVersion.Patch, 0);
	}

}
