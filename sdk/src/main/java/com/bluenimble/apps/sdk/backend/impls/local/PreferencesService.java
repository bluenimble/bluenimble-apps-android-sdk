package com.bluenimble.apps.sdk.backend.impls.local;

import android.content.Context;
import android.content.SharedPreferences;

import com.bluenimble.apps.sdk.Json;
import com.bluenimble.apps.sdk.Lang;
import com.bluenimble.apps.sdk.backend.Service;
import com.bluenimble.apps.sdk.controller.DataHolder;
import com.bluenimble.apps.sdk.json.JsonObject;
import com.bluenimble.apps.sdk.spec.ApplicationSpec;

public class PreferencesService implements Service {

	private static final long serialVersionUID = 2077148821012512850L;

	public interface Spec {
		String Verb     = "verb";
		String Bucket   = "bucket";
		String Data     = "data";
		String Key      = "key";
		String Value    = "value";
	}

	interface Response {
		String Status   = "status";
	}

	private enum Verb {
		GET,
		POST,
		DELETE
	}

	@Override
	public void execute (String id, JsonObject masterSpec, ApplicationSpec application, DataHolder dh, Context appContext) throws Exception {
		application.logger ().info (PreferencesService.class.getSimpleName () + " ---> Execute", "Service " + id);

		if (masterSpec == null) {
			throw new Exception ("backend service spec not found");
		}

		application.logger ().info (PreferencesService.class.getSimpleName () + " ---> Execute", "Master Spec : " + masterSpec.toString ());

		final JsonObject spec = masterSpec.duplicate ();

		Verb verb;
		try {
			verb = Verb.valueOf (Json.getString (spec, Spec.Verb, Verb.GET.name ()).toUpperCase ());
		} catch (Exception ex) {
			verb = Verb.GET;
		}

		String bucket = Json.getString (spec, Spec.Bucket);
		if (Lang.isNullOrEmpty (bucket)) {
			throw new Exception ("SharedPreferences Bucket not specified.");
		}

		// resolve and add data - if no data section defined at the service level, it should be default to the View ns
		JsonObject rdata = Json.getObject (spec, Spec.Data);
		if (Json.isNullOrEmpty (rdata)) {
			rdata = new JsonObject ();
			rdata.set (Spec.Key, id);
			rdata.set (Spec.Value, dh.get (DataHolder.Namespace.View));
			spec.set (Spec.Data, rdata);
		} else {
			Json.resolve (rdata, application.expressionCompiler (), dh);
		}

		SharedPreferences sharedPrefs = appContext.getSharedPreferences (bucket, Context.MODE_PRIVATE);

		if (verb == Verb.GET) {
			dh.set (id, new JsonObject (sharedPrefs.getString (Json.getString (rdata, Spec.Key), Lang.BLANK)));
		} else {
			SharedPreferences.Editor editor = sharedPrefs.edit ();
			if (verb == Verb.POST) {
				editor.putString (Json.getString (rdata, Spec.Key), Json.getObject (rdata, Spec.Value).toString ());
			} else if (verb == Verb.DELETE) {
				editor.remove (Json.getString (rdata, Spec.Key));
			}

			JsonObject resp = new JsonObject ();
			resp.set (Response.Status, editor.commit ());
			dh.set (id, resp);
		}
	}
}