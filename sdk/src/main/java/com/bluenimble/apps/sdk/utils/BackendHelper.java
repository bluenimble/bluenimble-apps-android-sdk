package com.bluenimble.apps.sdk.utils;

import com.bluenimble.apps.sdk.Json;
import com.bluenimble.apps.sdk.Lang;
import com.bluenimble.apps.sdk.Spec;
import com.bluenimble.apps.sdk.backend.Backend;
import com.bluenimble.apps.sdk.backend.DataVisitor;
import com.bluenimble.apps.sdk.backend.Service;
import com.bluenimble.apps.sdk.controller.DataHolder;
import com.bluenimble.apps.sdk.json.JsonObject;
import com.bluenimble.apps.sdk.spec.ApplicationSpec;

public class BackendHelper {

	public static void callService (String serviceId, DataHolder dh, ApplicationSpec application) throws Exception {

		Backend backend = application.backend ();
		if (backend == null) {
			throw new Exception ("no backend attached to this application");
		}
		JsonObject spec = null;

		Service service = backend.lockup (serviceId);
		if (service == null) {
			spec = backend.getSpec (serviceId);
			String type = Json.getString (spec, Spec.Service.Type, Service.Type.Remote);
			service = backend.lockup (type);
		}
		if (service == null) {
			throw new Exception ("service " + serviceId + " not defined in backend");
		}

		String visitorId = Json.getString (spec, Spec.Service.Visitor);
		DataVisitor visitor = null;

		if (!Lang.isNullOrEmpty (visitorId)) {
			visitor = backend.getVisitor (visitorId);
		}
		if (visitor != null) {
			visitor.onRequest (dh);
		}

		service.execute (serviceId, spec, application, dh);

		if (visitor != null) {
			visitor.onResponse (dh);
		}

	}

}
