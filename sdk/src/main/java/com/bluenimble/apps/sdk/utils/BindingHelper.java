package com.bluenimble.apps.sdk.utils;

import android.location.Location;
import android.util.Log;
import android.view.View;

import com.bluenimble.apps.sdk.Json;
import com.bluenimble.apps.sdk.Lang;
import com.bluenimble.apps.sdk.Spec;
import com.bluenimble.apps.sdk.application.UIActivity;
import com.bluenimble.apps.sdk.application.ux.LayerLayout;
import com.bluenimble.apps.sdk.controller.Action;
import com.bluenimble.apps.sdk.controller.ActionInstance;
import com.bluenimble.apps.sdk.controller.DataHolder;
import com.bluenimble.apps.sdk.controller.impls.actions.DefaultAction;
import com.bluenimble.apps.sdk.controller.impls.data.AgnosticDataHolder;
import com.bluenimble.apps.sdk.controller.impls.data.DefaultDataHolder;
import com.bluenimble.apps.sdk.json.JsonObject;
import com.bluenimble.apps.sdk.spec.ApplicationSpec;
import com.bluenimble.apps.sdk.spec.ComponentSpec;
import com.bluenimble.apps.sdk.spec.LayerSpec;
import com.bluenimble.apps.sdk.spec.PageSpec;
import com.bluenimble.apps.sdk.ui.components.ComponentFactory;
import com.bluenimble.apps.sdk.ui.components.ComponentsRegistry;
import com.bluenimble.apps.sdk.ui.effects.impls.BindEffect;
import com.bluenimble.apps.sdk.ui.renderer.ViewResolver;

import java.util.Iterator;

public class BindingHelper {

    public static void bindComponent (String tag, ViewResolver resolver, ApplicationSpec application, LayerSpec layer, ComponentSpec component, DataHolder dh, boolean useDh) {
        Log.d (tag, "\t\t    -> Bind Component [ " + component.type () + "(" + component.id () + ") ]");
        View view = resolver.findView (component.id ());
        if (view == null) {
            application.logger ().debug (BindEffect.class.getSimpleName (), "\t\t    -> ERR: View Not found [" + layer.id () + Lang.DOT + component.id () + "]");
            return;
        }
        application.componentsRegistry ().lookup (component.type ()).bind (ComponentSpec.Binding.Set, view, application, component, useDh ? dh : AgnosticDataHolder.Instance);
    }

    public static void bindLayer (String tag, ApplicationSpec application, LayerSpec layer, LayerLayout layerView, DataHolder dh, boolean useDh) {

        if (layerView == null || layer.count () == 0) {
            return;
        }

        application.logger ().debug (tag, "\t\t  -> Bind Layer [" + layer.id () + "]");
		application.logger ().debug (tag, "\t\t  -> with \n" + dh + "\n\t\tand useDh=" + useDh);

        for (int i = 0; i < layer.count (); i++) {
            bindComponent (tag, layerView, application, layer, layer.component (i), dh, useDh);
        }
    }

    public static void bindPage (String tag, ViewResolver resolver, ApplicationSpec application, PageSpec page, DataHolder dh, boolean useDh) {
        if (page.count () == 0) {
            return;
        }

        application.logger ().debug (tag, "\t\t-> Bind Page ...");

        Iterator<String> layers = page.layers ();
        while (layers.hasNext ()) {
            String layerId = layers.next ();
			View layerView = resolver.findView (layerId);
			if (layerView == null || !(layerView instanceof LayerLayout)) {
				continue;
			}
            bindLayer (tag, application, page.layer (layerId), (LayerLayout)layerView, dh, useDh);
        }
    }

    public static void collect (UIActivity activity, PageSpec page, ActionInstance actionInstance) {

		ApplicationSpec application = activity.getSpec ();

		JsonObject spec = actionInstance.eventSpec ();

		String [] scope = Lang.split (spec.getString (Spec.page.event.Scope), Lang.COMMA, true);

		if (actionInstance.dataHolder () == null) {
			DefaultDataHolder dh = new DefaultDataHolder ();
			dh.set (DataHolder.Namespace.Device, 	getDevice (activity));
			actionInstance.dataHolder (dh);
		}

		DataHolder dh = actionInstance.dataHolder ();

		// add event declared data
		JsonObject o = Json.getObject (spec, Spec.page.event.Data);
		if (!Json.isNullOrEmpty (o)) {
			dh.set (DataHolder.Namespace.Event, Json.resolve (o.duplicate (), dh));
		}

		if (scope == null || scope.length == 0) {
            return;
        }

        if (scope.length == 1 && Action.Scope.None.equals (scope [0])) {
            return;
        }

        if (scope.length == 1 && Action.Scope.Page.equals (scope [0])) {
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

    public static void collect (UIActivity activity, ApplicationSpec application, LayerSpec layer, DataHolder dh) {
        if (layer == null || layer.count () == 0) {
            return;
        }

        application.logger ().debug (BindingHelper.class.getSimpleName (), "Collect Data from Scope [" + layer.id () + "]");

        ComponentsRegistry registry = application.componentsRegistry ();

        for (int i = 0; i < layer.count (); i++) {
            ComponentSpec c = layer.component (i);

            application.logger ().debug (BindingHelper.class.getSimpleName (), "Collect Data from   Cmp [" + c.type () + Lang.SLASH + c.id () + "]");

            if (Lang.isNullOrEmpty (c.id ())) {
                continue;
            }

			ComponentFactory factory = registry.lookup (c.type ());
            if (factory == null) {
                continue;
            }

            View layerView = activity.findView (layer.id ());
            if (layerView == null || !(layerView instanceof LayerLayout)) {
                continue;
            }

            View cView = ((LayerLayout)layerView).findView (c.id ());
            if (cView == null) {
                continue;
            }
			application.logger ().debug (BindingHelper.class.getSimpleName (), "\tView [" + cView.getClass ().getSimpleName () + "]");

            factory.bind (ComponentSpec.Binding.Get, cView, application, c, dh);
        }
    }

	public static JsonObject getDevice (UIActivity activity) {
		Location location = activity.getLocation ();

		JsonObject device = new JsonObject ();

		if (location != null) {
			device.set (Action.Device.Geo, new JsonObject ().set (Action.Device.Latitude, location.getLatitude ()).set (Action.Device.Longitude, location.getLongitude ()) );
		}

		return device;
	}

}
