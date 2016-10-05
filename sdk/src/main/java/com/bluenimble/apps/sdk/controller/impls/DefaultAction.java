package com.bluenimble.apps.sdk.controller.impls;

import java.util.Iterator;

import com.bluenimble.apps.sdk.Json;
import com.bluenimble.apps.sdk.Lang;
import com.bluenimble.apps.sdk.Spec;
import com.bluenimble.apps.sdk.application.UIActivity;
import com.bluenimble.apps.sdk.backend.Backend;
import com.bluenimble.apps.sdk.backend.DataVisitor;
import com.bluenimble.apps.sdk.backend.Service;
import com.bluenimble.apps.sdk.controller.Action;
import com.bluenimble.apps.sdk.controller.DataHolder;
import com.bluenimble.apps.sdk.json.JsonObject;
import com.bluenimble.apps.sdk.spec.ApplicationSpec;
import com.bluenimble.apps.sdk.spec.ComponentSpec;
import com.bluenimble.apps.sdk.spec.ComponentSpec.Binding;
import com.bluenimble.apps.sdk.spec.LayerSpec;
import com.bluenimble.apps.sdk.spec.PageSpec;
import com.bluenimble.apps.sdk.ui.components.ComponentFactory;
import com.bluenimble.apps.sdk.ui.components.ComponentsRegistry;
import com.bluenimble.apps.sdk.ui.effects.Effect;

import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

public class DefaultAction implements Action {

	private static final long serialVersionUID = 1572206118405797117L;
	
	private static final String Default = "default";
	
	interface Scope {
		String Page = "page";
	}
	
	interface Device {
		String Geo 			= "geo";
			String Latitude 	= "lat";
			String Longitude 	= "long";
		String Ids 			= "ids";
			String Provider = "provider";
	}
	
	@Override
	public String id () {
		return Default;
	}
	
	@Override
	public void execute (final JsonObject spec, View view, final UIActivity activity, DataHolder dh) {
		
		final ApplicationSpec application = activity.getSpec ();
		
		final PageSpec 		page 		= application.renderer ().current ();
		
		String [] scope = Lang.split (spec.getString (Spec.page.layer.component.event.Scope), Lang.COMMA, true);
		
		if (dh == null) {
			dh = new DefaultDataHolder ();
			dh.set (DataHolder.Namespace.Device, 	getDevice (activity));
			dh.set (DataHolder.Namespace.App, 		new JsonObject ());
			
			// collect data even if there is no call. use-case: copy data between layers
			collect (activity, application, page, scope, dh);
			
			Log.d (DefaultAction.class.getSimpleName (), "DataHolder \n" + dh.toString ());
		}
		
		final DataHolder fdh = dh;
		
		applyEffects (Json.getObject (spec, Spec.Action.OnStart), activity, application, page, dh);

		final JsonObject oCall = Json.getObject (spec, Spec.Action.call.class.getSimpleName ());
		final String sServices = Json.getString (oCall, Spec.Action.call.Services);
		
		if (oCall == null || oCall.isEmpty () || Lang.isNullOrEmpty (sServices)) {
			return;
		}
		
		new AsyncTask<Void, Void, DataHolder> () {
			@Override
			protected DataHolder doInBackground (Void... params) {
				// make the call
				String [] services = Lang.split (sServices, Lang.COMMA, true);
				try {
					for (String s : services) {
						callService (s, fdh, application, page);
					}
				} catch (Exception ex) {
					fdh.exception (ex);
				}
				
				return fdh;
			}
			
			@Override
			protected void onPostExecute (DataHolder result) {
				if (result.exception () != null) {
					// TODO log exception
				}
				// onSuccess / onFailure
				applyEffects (Json.getObject (spec, (result.exception () != null) ? Spec.Action.call.OnError : Spec.Action.call.OnSuccess), activity, application, page, fdh);
				// onFinish
				applyEffects (Json.getObject (spec, Spec.Action.call.OnFinish), activity, application, page, result);
			}
		}.execute ();
		
	}
	
	void applyEffects (JsonObject effects, UIActivity activity, ApplicationSpec application, PageSpec page, DataHolder dh) {
		
		Log.d (DefaultAction.class.getSimpleName (), "Apply Effects \n" + effects);

		if (effects == null || effects.isEmpty ()) {
			return;
		}
		
		Iterator<String> ids = effects.keys ();
		while (ids.hasNext ()) {
			String effectId = ids.next ();
			Log.d (DefaultAction.class.getSimpleName (), "\tApply Effect [" + effectId + "]");
			Effect effect = application.effectsRegistry ().lockup (effectId);
			if (effect == null) {
				// TODO: log warning
				continue;
			}
			Log.d (DefaultAction.class.getSimpleName (), "\t-> Effect found in registry [" + effect.getClass ().getSimpleName () + "]");
			effect.apply (activity, application, page, effects.get (effectId), dh);
		}
	}

	private void callService (String serviceId, DataHolder dh, ApplicationSpec application, PageSpec page) throws Exception {
		
		Backend backend = application.backend ();
		if (backend == null) {
			throw new Exception ("no backend attached to this application");
		}
		JsonObject spec = null;
		
		Service service = backend.lockup (serviceId);
		if (service == null) {
			spec = backend.getSpec (serviceId);
			String type = Json.getString (spec, Spec.Service.Type, Service.Type.Remote);
			service = application.backend ().lockup (type);
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
		
		service.execute (serviceId, spec, dh);
		
		if (visitor != null) {
			visitor.onResponse (dh);
		}
		
	}

	private void collect (UIActivity activity, ApplicationSpec application, PageSpec page, String [] scope, DataHolder dh) {
		if (scope == null || scope.length == 0) {
			return;
		}
		
		if (scope.length == 1 && Scope.Page.equals (scope [0])) {
			Iterator<String> layers = page.layers ();
			while (layers.hasNext ()) {
				collect (activity, application, page.layer (layers.next ()), dh);
			}
		} else {
			for (String s : scope) {
				collect (activity, application, page.layer (s), dh);
			}
		}
		
	}
	
	private void collect (UIActivity activity, ApplicationSpec application, LayerSpec layer, DataHolder dh) {
		if (layer == null || layer.count () == 0) {
			return;
		}
		
		Log.d (DefaultAction.class.getSimpleName (), "Collect Data from Scope [" + layer.id () + "]");
		
		ComponentsRegistry registry = application.componentsRegistry ();
		
		for (int i = 0; i < layer.count (); i++) {
			ComponentSpec c = layer.component (i);
			
			Log.d (DefaultAction.class.getSimpleName (), "Collect Data from   Cmp [" + c.id () + "]");

			if (Lang.isNullOrEmpty (c.id ())) {
				continue;
			}
			ComponentFactory factory = registry.lookup (c.type ());
			if (factory == null) {
				continue;
			}
			
			Log.d (DefaultAction.class.getSimpleName (), "\tFactory [" + factory.id () + "]");
			
			View cView = activity.component (layer.id (), c.id ());
			Log.d (DefaultAction.class.getSimpleName (), "\tView [" + cView + "]");
			if (cView == null) {
				continue;
			}
			factory.bind (Binding.Get, cView, application, c, dh);
		}
	}
	
	private JsonObject getDevice (UIActivity activity) {
		Location location = activity.getLocation ();
		
		JsonObject device = new JsonObject ();
		
		if (location != null) {
			device.set (Device.Geo, new JsonObject ().set (Device.Latitude, location.getLatitude ()).set (Device.Longitude, location.getLongitude ()) );
		}
		
		return device;
	}
	
}
