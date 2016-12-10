package com.bluenimble.apps.sdk.ui.components.impls.chart;

import java.util.ArrayList;
import java.util.List;

import com.bluenimble.apps.sdk.Json;
import com.bluenimble.apps.sdk.R;
import com.bluenimble.apps.sdk.application.UIActivity;
import com.bluenimble.apps.sdk.controller.DataHolder;
import com.bluenimble.apps.sdk.json.JsonArray;
import com.bluenimble.apps.sdk.json.JsonObject;
import com.bluenimble.apps.sdk.spec.ApplicationSpec;
import com.bluenimble.apps.sdk.spec.BindingSpec;
import com.bluenimble.apps.sdk.spec.ComponentSpec;
import com.bluenimble.apps.sdk.spec.LayerSpec;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

public class PieChartFactory extends ChartFactory {

	private static final long serialVersionUID = 8437367345924192857L;
	
	private static final String Id = "chart.pie";

	public PieChartFactory () {
		// supportEvent (EventListener.Event.check);
	}

	@Override
	public String id () {
		return Id;
	}
	
	@Override
	public View create (UIActivity activity, ViewGroup group, LayerSpec layer, ComponentSpec spec, DataHolder dh) {
		
		style = (JsonObject)spec.style ().get (Style.pie.class.getSimpleName ());

		PieChart chart = new PieChart (activity);
		
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
	 * 			JsonArray (2 dim) 
	 * 				0: title
	 * 				1: value
	 **/
	@Override
	public void bind (ComponentSpec.Binding binding, View view, ApplicationSpec applicationSpec, ComponentSpec spec, DataHolder dh) {
		
		if (view == null || !(view instanceof PieChart)) {
			// TODO: log
			return;
		}
		
		BindingSpec bindingSpec = spec.binding (binding);
		if (bindingSpec == null) {
			return;
		}
		
		PieChart chart = (PieChart)view;
		
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

				PieData data = chart.getData ();
				if (data == null) {
					data = new PieData ();
				}

				List<PieEntry> entries = new ArrayList<PieEntry> ();
				for (int i = 0; i < array.count (); i++) {
					JsonArray record = (JsonArray)array.get (i);
					entries.add (new PieEntry (Float.valueOf ((String)record.get (1)), (String)record.get (0)));
				}

				PieDataSet dataSet = new PieDataSet (entries, (String)spec.get (Custom.Title));
				customStyle (dataSet);

				data.addDataSet (dataSet);
				chart.setData (data);

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
		
		// attach events
		
	}
	
}
