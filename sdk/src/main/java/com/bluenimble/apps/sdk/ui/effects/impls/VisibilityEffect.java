package com.bluenimble.apps.sdk.ui.effects.impls;

import com.bluenimble.apps.sdk.Lang;
import com.bluenimble.apps.sdk.application.UIActivity;
import com.bluenimble.apps.sdk.application.ux.LayerLayout;
import com.bluenimble.apps.sdk.controller.DataHolder;
import com.bluenimble.apps.sdk.spec.ApplicationSpec;
import com.bluenimble.apps.sdk.spec.ComponentSpec;
import com.bluenimble.apps.sdk.spec.LayerSpec;
import com.bluenimble.apps.sdk.spec.PageSpec;
import com.bluenimble.apps.sdk.ui.effects.Effect;

import android.view.View;

public abstract class VisibilityEffect implements Effect {

	private static final long serialVersionUID = 3783743185246914342L;
	
	protected int visibility;
	
	protected VisibilityEffect (int visibility) {
		this.visibility = visibility;
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
				showComponent (activity, application, layer, component, dh);
			} else {
				showLayer (activity, application, layer, dh);
			}
			
		}
		
	}
	
	private void showComponent (UIActivity activity, ApplicationSpec application, LayerSpec layer, ComponentSpec component, DataHolder dh) {
		View layerView = activity.findView (layer.id ());
		if (layerView == null || !(layerView instanceof LayerLayout)) {
			return;
		}

		View view = ((LayerLayout)layerView).findView (component.id ());
		if (view == null) {
			return;
		}
		view.setVisibility (visibility);
	}

	private void showLayer (UIActivity activity, ApplicationSpec application, LayerSpec layer, DataHolder dh) {
		View layerView = activity.findView (layer.id ());
		if (layerView == null || !(layerView instanceof LayerLayout)) {
			return;
		}
		layerView.setVisibility (visibility);
	}

}
