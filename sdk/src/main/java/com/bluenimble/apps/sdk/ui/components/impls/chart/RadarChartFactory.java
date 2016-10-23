package com.bluenimble.apps.sdk.ui.components.impls.chart;

import java.util.ArrayList;
import java.util.List;

import com.bluenimble.apps.sdk.Json;
import com.bluenimble.apps.sdk.application.UIActivity;
import com.bluenimble.apps.sdk.controller.DataHolder;
import com.bluenimble.apps.sdk.json.JsonArray;
import com.bluenimble.apps.sdk.json.JsonObject;
import com.bluenimble.apps.sdk.spec.ApplicationSpec;
import com.bluenimble.apps.sdk.spec.BindingSpec;
import com.bluenimble.apps.sdk.spec.ComponentSpec;
import com.bluenimble.apps.sdk.spec.LayerSpec;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;

import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

public class RadarChartFactory extends ChartFactory {

	private static final long serialVersionUID = 8437367345924192857L;
	
	private static final String Id = "chart.radar";

	public RadarChartFactory () {
		// supportEvent (EventListener.Event.check);
	}

	@Override
	public String id () {
		return Id;
	}
	
	@Override
	public View create (UIActivity activity, ViewGroup group, LayerSpec layer, ComponentSpec spec, DataHolder dh) {
		
		style = (JsonObject)spec.style ().get (Style.radar.class.getSimpleName ());

		RadarChart chart = new RadarChart (activity);
		
		// animation
		int animate = Json.getInteger (style, Style.Animate, 0);
		if (animate > 0) {
			chart.animateY (animate);
		}
		
		return applyStyle (group, chart, spec, dh);
	}

	/**
	 * Data Model:
	 * 		JsonArray
	 * 			JsonObject [multiple series] 
	 * 				title
	 * 				JsonArray [float] values
	 * 					
	 **/
	@Override
	public void bind (ComponentSpec.Binding binding, View view, ApplicationSpec applicationSpec, ComponentSpec spec, DataHolder dh) {
		
		if (view == null || !(view instanceof RadarChart)) {
			// TODO: log
			return;
		}
		
		BindingSpec bindingSpec = spec.binding (binding);
		if (bindingSpec == null) {
			return;
		}
		
		RadarChart chart = (RadarChart)view;
		
		switch (binding) {
			case Set:
				if (dh == null) {
					chart.clear ();
					return;
				}
				Object value = dh.valueOf (applicationSpec, bindingSpec);
				if (value == null) {
					return;
				}
				
				if (!(value instanceof JsonArray)) {
					// TODO: log
					return;
				}
				
				JsonArray array = (JsonArray)value;
				if (array.isEmpty ()) {
					// TODO: log
					return;
				}

				RadarData data = chart.getData ();
				if (data == null) {
					data = new RadarData ();
					chart.setData (data);
				}
				
				for (int i = 0; i < array.count (); i++) {
					JsonObject series = (JsonObject)array.get (i);
					if (Json.isNullOrEmpty (series)) {
						continue;
					}
					JsonArray values = Json.getArray (series, Record.Values);
					if (values == null || values.isEmpty ()) {
						continue;
					}
					
					List<RadarEntry> entries = new ArrayList<RadarEntry> ();
					for (int j = 0; j < values.count (); j++) {
						entries.add (new RadarEntry (Float.valueOf ((String)values.get (j))));
					}
					data.addDataSet ( new RadarDataSet (entries, Json.getString (series, Custom.Title)) );
				}

				chart.invalidate (); // refresh

				break;
			default:
				break;
		}
		
	}

	@Override
	public void addEvent (UIActivity activity, View view, ApplicationSpec applicationSpec, ComponentSpec component, String eventName, JsonObject eventSpec) {
		if (view == null || !(view instanceof RadioGroup)) {
			// TODO: log
			return;
		}
		
		if (!isEventSupported (eventName)) {
			// TODO: log
			return;
		}
		
		// attach event
		
	}
	
}
