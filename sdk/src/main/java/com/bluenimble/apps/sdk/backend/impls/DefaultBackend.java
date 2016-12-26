package com.bluenimble.apps.sdk.backend.impls;

import java.util.HashMap;
import java.util.Map;

import com.bluenimble.apps.sdk.Json;
import com.bluenimble.apps.sdk.backend.Backend;
import com.bluenimble.apps.sdk.backend.DataVisitor;
import com.bluenimble.apps.sdk.backend.Service;
import com.bluenimble.apps.sdk.backend.impls.local.PreferencesService;
import com.bluenimble.apps.sdk.backend.impls.remote.RemoteService;
import com.bluenimble.apps.sdk.backend.impls.storage.StorageService;
import com.bluenimble.apps.sdk.json.JsonObject;

public class DefaultBackend implements Backend {

	private static final long serialVersionUID = 8651804425118492841L;

	protected JsonObject spec;
	
	protected Map<String, Service> 		services = new HashMap<String, Service>();
	protected Map<String, DataVisitor> 	visitors = new HashMap<String, DataVisitor>();
	
	public DefaultBackend () {
	}
	
	public DefaultBackend (JsonObject spec) {
		load (spec);
		register (Service.Type.Remote, 	new RemoteService());
		register (Service.Type.Local, 	new PreferencesService());
		register (Service.Type.Storage, new StorageService ());
	}
	
	@Override
	public void load (JsonObject spec) {
		//TODO : added here Mehdi
		register (Service.Type.Remote, 	new RemoteService());
		register (Service.Type.Local, 	new PreferencesService());
		register (Service.Type.Storage, new StorageService ());

		if (this.spec == null) {
			this.spec = spec;
			return;
		}
		this.spec.putAll (spec);
	}

	@Override
	public JsonObject getSpec (String id) {
		return Json.getObject (spec, id);
	}

	@Override
	public void addSpec (String id, JsonObject spec) {
		if (this.spec == null) {
			this.spec = new JsonObject ();
		}
		this.spec.set (id, spec);
	}

	@Override
	public Service lockup (String id) {
		return services.get (id);
	}

	@Override
	public void register (String id, Service service) {
		services.put (id, service);
	}

	@Override
	public void addVisitor (DataVisitor visitor) {
		visitors.put (visitor.id (), visitor);
	}

	@Override
	public DataVisitor getVisitor (String id) {
		return visitors.get (id);
	}

}
