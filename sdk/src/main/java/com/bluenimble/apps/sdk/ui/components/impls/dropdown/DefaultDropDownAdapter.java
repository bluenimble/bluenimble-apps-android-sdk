package com.bluenimble.apps.sdk.ui.components.impls.dropdown;

import java.util.Iterator;

import com.bluenimble.apps.sdk.Lang;
import com.bluenimble.apps.sdk.application.UIActivity;
import com.bluenimble.apps.sdk.controller.DataHolder;
import com.bluenimble.apps.sdk.controller.impls.DefaultDataHolder;
import com.bluenimble.apps.sdk.json.JsonArray;
import com.bluenimble.apps.sdk.json.JsonObject;
import com.bluenimble.apps.sdk.spec.ApplicationSpec;
import com.bluenimble.apps.sdk.spec.ComponentSpec;
import com.bluenimble.apps.sdk.spec.LayerSpec;
import com.bluenimble.apps.sdk.spec.PageSpec;
import com.bluenimble.apps.sdk.ui.components.impls.dropdown.DropDownFactory.Custom;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class DefaultDropDownAdapter extends ArrayAdapter<DataHolder> {

	private static final String Namespace = "record";
	
	protected ApplicationSpec 	application;
	protected LayerSpec 		layer;
	protected PageSpec 			page;
	protected JsonArray 		records;
	protected DataHolder 		one;
	protected Context 			context;
	
	public DefaultDropDownAdapter (Context context, JsonArray records) {
		super (context, 0);
		this.records	= records;
		this.one 		= new DefaultDataHolder ();
	}
	
	public DefaultDropDownAdapter (Context context, ApplicationSpec application, ComponentSpec component, JsonArray records) {
		super (context, 0);
		
		this.context 		= context;
		this.records		= records;
		this.one 			= new DefaultDataHolder ();
		
		this.application 	= application;
		this.page 			= application.renderer ().current ();
		
		String template 	= (String) component.get (Custom.Template);
		String layerId		= template;
		
		int indexOfDot		= template.indexOf (Lang.DOT);
		if (indexOfDot > 0) {
			page 			= application.page (template.substring (0, indexOfDot));
			layerId			= template.substring (indexOfDot + 1);
		} 
		if (page == null) {
			return;
		}
		
		this.layer 			= page.layer (layerId);
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
		if (oRecord == null || !(oRecord instanceof JsonObject)) {
			return null;
		}
		
		one.set (Namespace, oRecord);
		
		return one;
	}
	
	@Override
	public View getView (int position, View convertView, ViewGroup parent) {
		DataHolder dh 	= getItem (position);
		JsonObject data = (JsonObject)dh.get (Namespace);
		
		if (convertView == null) {
			convertView = application.renderer ().render (application, layer, parent, (UIActivity)context);
		}
		
		Iterator<String> iter = data.keys ();
		while (iter.hasNext ()) {
			View v = convertView.findViewWithTag (layer.id () + Lang.DOT + iter.next ()); 
			// v.setText // how to bind value for each view on the record
		}
		
		return convertView;
	}
	
	@Override
	public View getDropDownView (int position, View convertView, ViewGroup parent) {
		DataHolder dh 	= getItem (position);
		JsonObject data = (JsonObject)dh.get (Namespace);
		
		if (convertView == null) {
			convertView = application.renderer ().render (application, layer, parent, (UIActivity)context);
		}
		
		Iterator<String> iter = data.keys ();
		while (iter.hasNext ()) {
			View v = convertView.findViewWithTag (layer.id () + Lang.DOT + iter.next ()); 
			// v.setText // how to bind value for each view on the record
		}
		
		return convertView;
	}
}