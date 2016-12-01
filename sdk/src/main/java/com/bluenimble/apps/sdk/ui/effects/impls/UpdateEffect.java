package com.bluenimble.apps.sdk.ui.effects.impls;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bluenimble.apps.sdk.Json;
import com.bluenimble.apps.sdk.Lang;
import com.bluenimble.apps.sdk.application.UIActivity;
import com.bluenimble.apps.sdk.application.ux.LayerLayout;
import com.bluenimble.apps.sdk.controller.DataHolder;
import com.bluenimble.apps.sdk.json.JsonObject;
import com.bluenimble.apps.sdk.spec.ApplicationSpec;
import com.bluenimble.apps.sdk.spec.PageSpec;
import com.bluenimble.apps.sdk.ui.components.impls.list.DefaultListAdapter;
import com.bluenimble.apps.sdk.ui.effects.Effect;

public class UpdateEffect implements Effect {

	private static final long serialVersionUID = 3783743185246914342L;
	
	private static final String Id = "update";

	@Override
	public String id () {
		return Id;
	}

	@Override
	public void apply (UIActivity activity, ApplicationSpec application, PageSpec page, Object spec, View origin, DataHolder dh) {
		
		if (spec == null || !(spec instanceof String)) {
			return;
		}

		String layerAndGridId = (String)spec;

		int indexOfDot = layerAndGridId.indexOf (Lang.DOT);
		if (indexOfDot < 0) {
			return;
		}

		View vLayerView = activity.findView (layerAndGridId.substring (0, indexOfDot));
		if (vLayerView == null) {
			return;
		}
		if (!(vLayerView instanceof LayerLayout)) {
			return;
		}

		String gridId 	= layerAndGridId.substring (indexOfDot + 1);
		String dhNs		= DataHolder.Namespace.View;

		int indexOfSlash = gridId.indexOf (Lang.SLASH);
		if (indexOfSlash > 0) {
			dhNs 	= gridId.substring (indexOfSlash + 1);
			gridId 	= gridId.substring (0, indexOfSlash);
		}

		View list = ((LayerLayout)vLayerView).findView (gridId);
		if (list == null || !(list instanceof RecyclerView)) {
			return;
		}

		DefaultListAdapter adapter = (DefaultListAdapter)((RecyclerView)list).getAdapter ();

		JsonObject updatedRecord = Json.getObject ((JsonObject) dh.get (dhNs), gridId);

		adapter.updateSelected (updatedRecord);

	}

}
