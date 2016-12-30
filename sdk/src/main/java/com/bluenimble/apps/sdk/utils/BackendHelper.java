package com.bluenimble.apps.sdk.utils;

import android.content.Context;

import com.bluenimble.apps.sdk.Json;
import com.bluenimble.apps.sdk.Lang;
import com.bluenimble.apps.sdk.Spec;
import com.bluenimble.apps.sdk.application.UIActivity;
import com.bluenimble.apps.sdk.backend.Backend;
import com.bluenimble.apps.sdk.backend.DataVisitor;
import com.bluenimble.apps.sdk.backend.Service;
import com.bluenimble.apps.sdk.controller.DataHolder;
import com.bluenimble.apps.sdk.json.JsonObject;
import com.bluenimble.apps.sdk.spec.ApplicationSpec;

public class BackendHelper {

	//TODO : check on leaks because of having passed an activity
	public static void callService (String serviceId, DataHolder dh, ApplicationSpec application, Context appContext) throws Exception {

		application.logger ().error (BackendHelper.class.getSimpleName () + " ---> callService1", "Got into the CallService Method with " + serviceId);

		Backend backend = application.backend ();
		if (backend == null) {
			application.logger ().error (BackendHelper.class.getSimpleName () + " ---> callService10", "Backend null");
			throw new Exception ("no backend attached to this application");
		}
		JsonObject spec = null;

		Service service = backend.lockup (serviceId);
		if (service == null) {
			spec = backend.getSpec (serviceId);
			if (spec != null) {
				application.logger ().error (BackendHelper.class.getSimpleName () + " ---> callService111", "Spec : " + spec.toString ());
			} else {
				application.logger ().error (BackendHelper.class.getSimpleName () + " ---> callService111", "Spec : NULL");
			}

			String type = Json.getString (spec, Spec.Service.Type, Service.Type.Remote);
			application.logger ().error (BackendHelper.class.getSimpleName () + " ---> callService112", "Type : " + String.valueOf (type));
			service = backend.lockup (type);
		}
		if (service == null) {
			application.logger ().error (BackendHelper.class.getSimpleName () + " ---> callService11", "Service null");
			throw new Exception ("service " + serviceId + " not defined in backend");
		}

		application.logger ().error (BackendHelper.class.getSimpleName () + " ---> callService12", "Service : " + service.toString ());

		String visitorId = Json.getString (spec, Spec.Service.Visitor);
		DataVisitor visitor = null;

		if (!Lang.isNullOrEmpty (visitorId)) {
			visitor = backend.getVisitor (visitorId);
		}
		if (visitor != null) {
			visitor.onRequest (dh);
		}

		application.logger ().error (BackendHelper.class.getSimpleName () + " ---> callService2", "Got into the CallService Method with " + serviceId);
		service.execute (serviceId, spec, application, dh, appContext);

		if (visitor != null) {
			visitor.onResponse (dh);
		}

	}

}
