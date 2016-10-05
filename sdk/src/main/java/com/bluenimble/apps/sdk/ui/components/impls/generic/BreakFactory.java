package com.bluenimble.apps.sdk.ui.components.impls.generic;

import com.bluenimble.apps.sdk.application.UIActivity;
import com.bluenimble.apps.sdk.spec.ComponentSpec;
import com.bluenimble.apps.sdk.spec.LayerSpec;
import com.bluenimble.apps.sdk.ui.components.AbstractComponentFactory;

import android.view.View;
import android.view.ViewGroup;

public class BreakFactory extends AbstractComponentFactory {

	private static final long serialVersionUID = 8437367345924192857L;
	
	public static final String Id = "break";
	
	public BreakFactory () {
	}

	@Override
	public String id () {
		return Id;
	}
	
	@Override
	public View create (UIActivity activity, ViewGroup group, LayerSpec layer, ComponentSpec spec) {
		return applyStyle (group, new View (activity), spec);
	}

}
