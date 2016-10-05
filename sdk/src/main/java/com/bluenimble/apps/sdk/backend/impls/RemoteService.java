package com.bluenimble.apps.sdk.backend.impls;

import java.io.ByteArrayOutputStream;

import com.bluenimble.apps.sdk.Json;
import com.bluenimble.apps.sdk.Lang;
import com.bluenimble.apps.sdk.Lang.VariableResolver;
import com.bluenimble.apps.sdk.backend.Service;
import com.bluenimble.apps.sdk.controller.DataHolder;
import com.bluenimble.apps.sdk.http.HttpMethods;
import com.bluenimble.apps.sdk.http.response.HttpResponse;
import com.bluenimble.apps.sdk.http.utils.Http;
import com.bluenimble.apps.sdk.json.JsonObject;

public class RemoteService implements Service {

	private static final long serialVersionUID = 2077148821012512850L;
	
	@Override
	public void execute (String id, JsonObject spec, DataHolder dh) throws Exception {
		
		if (spec == null) {
			throw new Exception ("backend service spec not found");
		}
		
		spec = spec.duplicate ();
		
		// visit url
		resolve (spec, Http.Spec.Url, dh);

		// create the data object
		JsonObject rdata = Json.getObject (spec, Http.Spec.Data);
		if (rdata == null || rdata.isEmpty ()) {
			spec.set (Http.Spec.Data, dh.get (DataHolder.Namespace.App));
		} else {
			Json.resolve (rdata, dh);
		}
		
		// visit headers
		JsonObject headers = Json.getObject (spec, Http.Spec.Headers);
		if (headers != null && !headers.isEmpty ()) {
			Json.resolve (headers, dh);
		}
		
		HttpResponse response = 
			Http.send (
				Http.request (
					Json.getString (spec, Http.Spec.Verb, HttpMethods.GET).toUpperCase (), 
					spec, 
					null
				)
			);
		
		int status = response.getStatus ();
		
		if (status > 199 && status < 300) {
			if (response.getBody () == null || response.getBody ().count () == 0) {
				return;
			}
			ByteArrayOutputStream out = new ByteArrayOutputStream ();
			response.getBody ().dump (out, "UTF-8", null);
			
			dh.set (id, new JsonObject (new String (out.toByteArray ())));
		}
		
		// TODO : temporary, should inspect contentType and spec to parse or not as json
		throw new Exception ("response status " + status);
		
	}
	
	private void resolve (JsonObject o, String key, VariableResolver vr) {
		o.set (key, Lang.resolve (String.valueOf (o.get (key)), Lang.ARRAY_OPEN, Lang.ARRAY_OPEN, vr));
	}
	
}
