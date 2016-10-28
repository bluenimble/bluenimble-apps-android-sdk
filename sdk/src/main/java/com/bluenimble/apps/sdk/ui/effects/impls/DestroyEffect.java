package com.bluenimble.apps.sdk.ui.effects.impls;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewGroupCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.bluenimble.apps.sdk.Lang;
import com.bluenimble.apps.sdk.application.UIActivity;
import com.bluenimble.apps.sdk.application.ux.LayerLayout;
import com.bluenimble.apps.sdk.controller.DataHolder;
import com.bluenimble.apps.sdk.spec.ApplicationSpec;
import com.bluenimble.apps.sdk.spec.ComponentSpec;
import com.bluenimble.apps.sdk.spec.LayerSpec;
import com.bluenimble.apps.sdk.spec.PageSpec;
import com.bluenimble.apps.sdk.ui.components.ComponentFactory;
import com.bluenimble.apps.sdk.ui.effects.Effect;

public class DestroyEffect implements Effect {

	private static final long serialVersionUID = 3783743185246914342L;
	
	private static final String Id = "destroy";

	@Override
	public String id () {
		return Id;
	}

	@Override
	public void apply (UIActivity activity, ApplicationSpec application, PageSpec page, Object spec, View origin, DataHolder dh) {

		if (spec == null || !(spec instanceof String)) {
			return;
		}

		String list = (String)spec;

		if (Lang.isNullOrEmpty (list)) {
			return;
		}

		String [] uis = Lang.split (list, Lang.SPACE, true);

		for (String ui : uis) {
			String componentId = null;
			String layerId = ui;
			int indexOfDot = ui.indexOf (Lang.DOT);
			if (indexOfDot > 0) {
				layerId = ui.substring (0, indexOfDot);
				componentId = ui.substring (indexOfDot + 1);
			}
			LayerSpec layer = page.layer (layerId);
			if (layer == null) {
				// TODO log
				continue;
			}
			ComponentSpec component = null;
			if (!Lang.isNullOrEmpty (componentId)) {
				component = layer.component (componentId);
				if (component == null) {
					// TODO log
					continue;
				}
			}

			if (component != null) {
				destroyComponent (activity, application, layer, component, dh);
			} else {
				destroyLayer (activity, application, layer, dh);
			}

		}

	}

	private void destroyComponent (UIActivity activity, ApplicationSpec application, LayerSpec layer, ComponentSpec component, DataHolder dh) {

		ComponentFactory componentFactory = application.componentsRegistry ().lookup (component.id ());
		if (componentFactory == null) {
			return;
		}

		View view = null;

		View layerView = activity.findView (layer.id ());
		if (layerView != null && (layerView instanceof LayerLayout)) {
			view = ((LayerLayout)layerView).findView (component.id ());
		}

		componentFactory.destroy (activity, view, application, component);
	}

	private void destroyLayer (UIActivity activity, ApplicationSpec application, LayerSpec layer, DataHolder dh) {

		FragmentManager manager = activity.getSupportFragmentManager ();

		Fragment f = manager.findFragmentByTag (layer.id ());
		if (f == null) {
			return;
		}

		FragmentTransaction transaction = manager.beginTransaction ();

		// remove fragment
		transaction.remove (f);

		// commit
		transaction.commit ();

	}

}
