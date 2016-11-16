package com.bluenimble.apps.sdk.controller.impls.data;

import com.bluenimble.apps.sdk.Json;
import com.bluenimble.apps.sdk.controller.DataHolder;
import com.bluenimble.apps.sdk.controller.StreamSource;
import com.bluenimble.apps.sdk.json.JsonObject;
import com.bluenimble.apps.sdk.spec.ApplicationSpec;
import com.bluenimble.apps.sdk.spec.BindingSpec;

public class InternalDataHolder implements DataHolder  {

	private static final long serialVersionUID = 713158374746493539L;
	
	public static final DataHolder Instance = new InternalDataHolder ();

	private JsonObject internal = (JsonObject)new JsonObject ().set (Internal.NoTag, true);
	
	@Override
	public Exception exception () {
		return null;
	}

	@Override
	public Object get (String namespace, String... property) {
		if (!Namespace.Internal.equals (namespace)) {
			return null;
		}
		return Json.find (internal, property);
	}

	@Override
	public void stream (StreamSource stream) {
	}

	@Override
	public StreamSource stream (String name) {
		return null;
	}

	@Override
	public DataHolder set (String namespace, Object value, String... property) {
		return this;
	}

	@Override
	public void exception (Exception exception) {
	}

	@Override
	public Object resolve (String ns, String... name) {
		return null;
	}

	@Override
	public Object valueOf (ApplicationSpec applicationSpec, BindingSpec bindingSpec) {
		return null;
	}

	@Override
	public DataHolder clone () {
		return this;
	}

	@Override
	public void destroy () {
	}

}
