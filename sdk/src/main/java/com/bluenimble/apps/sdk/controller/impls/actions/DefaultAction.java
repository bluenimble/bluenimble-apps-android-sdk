package com.bluenimble.apps.sdk.controller.impls.actions;

import java.util.Iterator;

import com.bluenimble.apps.sdk.Json;
import com.bluenimble.apps.sdk.Lang;
import com.bluenimble.apps.sdk.Spec;
import com.bluenimble.apps.sdk.application.UIActivity;
import com.bluenimble.apps.sdk.controller.Action;
import com.bluenimble.apps.sdk.controller.DataHolder;
import com.bluenimble.apps.sdk.controller.impls.data.DefaultDataHolder;
import com.bluenimble.apps.sdk.json.JsonObject;
import com.bluenimble.apps.sdk.spec.ApplicationSpec;
import com.bluenimble.apps.sdk.spec.ComponentSpec;
import com.bluenimble.apps.sdk.spec.ComponentSpec.Binding;
import com.bluenimble.apps.sdk.spec.LayerSpec;
import com.bluenimble.apps.sdk.spec.PageSpec;
import com.bluenimble.apps.sdk.ui.components.ComponentFactory;
import com.bluenimble.apps.sdk.ui.components.ComponentsRegistry;
import com.bluenimble.apps.sdk.ui.effects.Effect;
import com.bluenimble.apps.sdk.utils.BackendHelper;

import android.location.Location;
import android.os.AsyncTask;
import android.view.View;

public class DefaultAction implements Action {

	private static final long serialVersionUID = 1572206118405797117L;
	
	private static final String Default = "default";
	
	@Override
	public String id () {
		return Default;
	}
	
	@Override
	public void execute (final JsonObject spec, View view, final UIActivity activity, DataHolder dh) {
		
		final ApplicationSpec application = activity.getSpec ();
		
		final PageSpec 		page 		= application.renderer ().current ();
		
		String [] scope = Lang.split (spec.getString (Spec.page.event.Scope), Lang.COMMA, true);
		
		if (dh == null) {
			dh = new DefaultDataHolder();
			dh.set (DataHolder.Namespace.Device, 	getDevice (activity));
			dh.set (DataHolder.Namespace.App, 		new JsonObject ());
		}

		// collect data even if there is no call. use-case: copy data between layers
		collect (activity, application, page, scope, dh);

		// add event declared data
		JsonObject o = Json.getObject (spec, Spec.page.event.Data);
		if (Json.isNullOrEmpty (o)) {
			dh.set (Spec.page.event.class.getSimpleName (), Json.resolve (o, dh));
		}

		application.logger ().debug (DefaultAction.class.getSimpleName (), "DataHolder \n" + dh.toString ());

		applyEffects (Json.getObject (spec, Spec.Action.OnStart), activity, application, page, dh);

		final JsonObject oCall = Json.getObject (spec, Spec.Action.call.class.getSimpleName ());
		final String sServices = Json.getString (oCall, Spec.Action.call.Services);
		
		if (oCall == null || oCall.isEmpty () || Lang.isNullOrEmpty (sServices)) {
			// close result
			dh.close ();// close result
			return;
		}

		final DataHolder fdh = dh;

		new AsyncTask<Void, Void, DataHolder> () {
			@Override
			protected DataHolder doInBackground (Void... params) {
				// make the call
				String [] services = Lang.split (sServices, Lang.COMMA, true);
				try {
					for (String s : services) {
						BackendHelper.callService (s, fdh, application);
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

				// close result
				result.close ();
			}
		}.execute ();
		
	}
	
	void applyEffects (JsonObject effects, UIActivity activity, ApplicationSpec application, PageSpec page, DataHolder dh) {

		application.logger ().debug (DefaultAction.class.getSimpleName (), "Apply Effects \n" + effects);

		if (effects == null || effects.isEmpty ()) {
			return;
		}
		
		Iterator<String> ids = effects.keys ();
		while (ids.hasNext ()) {
			String effectId = ids.next ();
			application.logger ().debug (DefaultAction.class.getSimpleName (), "\tApply Effect [" + effectId + "]");
			Effect effect = application.effectsRegistry ().lockup (effectId);
			if (effect == null) {
				// TODO: log warning
				continue;
			}
			application.logger ().debug (DefaultAction.class.getSimpleName (), "\t-> Effect found in registry [" + effect.getClass ().getSimpleName () + "]");
			effect.apply (activity, application, page, effects.get (effectId), dh);
		}
	}

	private void collect (UIActivity activity, ApplicationSpec application, PageSpec page, String [] scope, DataHolder dh) {
		if (scope == null || scope.length == 0) {
			return;
		}

		if (scope.length == 1 && Scope.None.equals (scope [0])) {
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

		application.logger ().debug (DefaultAction.class.getSimpleName (), "Collect Data from Scope [" + layer.id () + "]");
		
		ComponentsRegistry registry = application.componentsRegistry ();
		
		for (int i = 0; i < layer.count (); i++) {
			ComponentSpec c = layer.component (i);

			application.logger ().debug (DefaultAction.class.getSimpleName (), "Collect Data from   Cmp [" + c.id () + "]");

			if (Lang.isNullOrEmpty (c.id ())) {
				continue;
			}
			ComponentFactory factory = registry.lookup (c.type ());
			if (factory == null) {
				continue;
			}

			application.logger ().debug (DefaultAction.class.getSimpleName (), "\tFactory [" + factory.id () + "]");
			
			View cView = activity.component (layer.id (), c.id ());
			application.logger ().debug (DefaultAction.class.getSimpleName (), "\tView [" + cView + "]");
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
