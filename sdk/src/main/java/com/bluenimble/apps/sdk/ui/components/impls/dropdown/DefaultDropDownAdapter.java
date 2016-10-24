package com.bluenimble.apps.sdk.ui.components.impls.dropdown;

import com.bluenimble.apps.sdk.application.UIActivity;
import com.bluenimble.apps.sdk.application.ux.LayerLayout;
import com.bluenimble.apps.sdk.controller.DataHolder;
import com.bluenimble.apps.sdk.controller.impls.data.DefaultDataHolder;
import com.bluenimble.apps.sdk.json.JsonArray;
import com.bluenimble.apps.sdk.json.JsonObject;
import com.bluenimble.apps.sdk.spec.ApplicationSpec;
import com.bluenimble.apps.sdk.spec.ComponentSpec;
import com.bluenimble.apps.sdk.spec.LayerSpec;
import com.bluenimble.apps.sdk.utils.BindingHelper;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class DefaultDropDownAdapter extends ArrayAdapter<DataHolder> {

	private static final String LogTag = DefaultDropDownAdapter.class.getSimpleName ();

	interface DefaultRecord {
		String Id 		= "id";
		String Value 	= "value";
	}

	protected LayerSpec 		template;
	protected JsonArray 		records;

	protected DataHolder 		one;
	protected String			recordNs;

	protected UIActivity 		activity;
	
	public DefaultDropDownAdapter (UIActivity activity, ComponentSpec component, LayerSpec template, String recordNs) {
		super (activity, 0);
		
		this.activity 		= activity;
		this.recordNs		= recordNs;

		this.one 			= new DefaultDataHolder ();
		
		this.template 		= template;
	}

	void load (JsonArray records) {
		this.records = records;
	}

	JsonArray getRecords () {
		return records;
	}

	@Override
	public int getCount () {
		if (records == null) {
			return 0;
		}
		return records.count ();
	}
	
	@Override
	public DataHolder getItem (int position) {
		if (records == null) {
			return null;
		}
		// here get the value used in getView / getDropDownView
		Object oRecord = records.get (position);
		if (oRecord == null) {
			return null;
		}

		if (oRecord instanceof JsonObject) {
			return one.set (recordNs, oRecord);
		}

		if (oRecord instanceof String) {
			return one.set (recordNs, new JsonObject ().set (DefaultRecord.Id, position).set (DefaultRecord.Value, oRecord));
		}
		
		return one.set (recordNs, JsonObject.Blank);
	}
	
	@Override
	public View getView (int position, View convertView, ViewGroup parent) {
		ApplicationSpec application = activity.getSpec ();

		DataHolder dh = getItem (position);

		if (convertView == null) {
			convertView = application.renderer ().render (application, template, dh, parent, activity);
			convertView.setTag (null);
		}

		// binding data and any other additional effects
		BindingHelper.bindLayer (
				LogTag,
				activity.getSpec (),
				template, (LayerLayout)convertView, one.set (recordNs, (JsonObject) records.get (position)),
				true
		);

		return convertView;
	}
	
	@Override
	public View getDropDownView (int position, View convertView, ViewGroup parent) {
		ApplicationSpec application = activity.getSpec ();

		if (convertView == null) {
			convertView = application.renderer ().render (application, template, getItem (position), parent, activity);
		}

		return convertView;
	}
}