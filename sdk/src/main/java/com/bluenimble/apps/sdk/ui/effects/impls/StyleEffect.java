package com.bluenimble.apps.sdk.ui.effects.impls;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bluenimble.apps.sdk.Lang;
import com.bluenimble.apps.sdk.application.UIActivity;
import com.bluenimble.apps.sdk.application.ux.LayerLayout;
import com.bluenimble.apps.sdk.controller.DataHolder;
import com.bluenimble.apps.sdk.spec.ApplicationSpec;
import com.bluenimble.apps.sdk.spec.ComponentSpec;
import com.bluenimble.apps.sdk.spec.LayerSpec;
import com.bluenimble.apps.sdk.spec.PageSpec;
import com.bluenimble.apps.sdk.spec.StylishSpec;
import com.bluenimble.apps.sdk.ui.effects.Effect;
import com.bluenimble.apps.sdk.ui.themes.impls.JsonStyleSpec;
import com.bluenimble.apps.sdk.utils.SpecHelper;

public class StyleEffect implements Effect {

	private static final long serialVersionUID = 3783743185246914342L;
	
	private static final String Id = "style";

	@Override
	public String id () {
		return Id;
	}

	/**
	 *  Styling a specific layer
	 *    "style": "layer1 style1 style2" (apply both style1 and style2 to layer1 layer)
	 *  Styling a specific component
	 *    "style": "layer1:cmp1 style1 style2" (apply both style1 and style2 to cmp1 component under the layer1 layer)
	 *  Styling multiple layers and components
	 *    "style": "layer1 style1 style2 , layer1:cmp1 style1 style2 , ..."
	 *
	 **/
	@Override
	public void apply (UIActivity activity, ApplicationSpec application, PageSpec page, Object spec, View origin, DataHolder dh) {
		
		if (spec == null || !(spec instanceof String)) {
			return;
		}

		String sSpec = (String)spec;

		String [] cStyles = Lang.split (sSpec, Lang.COMMA, true);
		for (String cStyle : cStyles) {
			int indexOfColon = cStyle.indexOf (Lang.COLON);
			if (indexOfColon <= 0) {
				continue;
			}
			String cmp 		= cStyle.substring (0, indexOfColon);
			String styles 	= cStyle.substring (indexOfColon + 1);
			if (Lang.isNullOrEmpty (styles)) {
				continue;
			}
			StylishSpec stylish = SpecHelper.stylish (application, cmp);
			if (stylish == null) {
				continue;
			}
			View layerView = activity.findView (stylish.id ());
			if (layerView == null || !(layerView instanceof LayerLayout)) {
				continue;
			}
			View view = null;
			if (stylish instanceof LayerSpec) {
				view = layerView;
			} else if (stylish instanceof ComponentSpec) {
				view = ((LayerLayout)layerView).findView (stylish.id ());
			}
			if (view == null) {
				continue;
			}

			// apply style
			new JsonStyleSpec (application.theme (), Lang.split (styles, Lang.SPACE, true))
					.apply (stylish, view, (ViewGroup) view.getParent (), dh);

		}

	}

}
