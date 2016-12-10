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
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

public class BarChartFactory extends ChartFactory {

	private static final long serialVersionUID = 8437367345924192857L;

	private static final String Id = "chart.bar";

	public BarChartFactory () {
		// supportEvent (EventListener.Event.check);
	}

	@Override
	public String id () {
		return Id;
	}

	@Override
	public View create (UIActivity activity, ViewGroup group, LayerSpec layer, ComponentSpec spec, DataHolder dh) {

		style = (JsonObject)spec.style ().get (Style.bar.class.getSimpleName ());

		String orientation = Json.getString (style, Style.bar.Orientation, Orientations.BottomTop);

		BarChart chart;
		if (orientation.equals (Orientations.LeftRight)) {
			chart = new HorizontalBarChart (activity);
		} else {
			chart = new BarChart (activity);
		}

		// animation
		int animate = Json.getInteger (style, Style.Animate, 0);
		if (animate > 0) {
			chart.animateY (animate);
		}

		// apply specific styles
		chart.setFitBars (Json.getBoolean (style, Style.bar.Fit, true));

		// apply global style
		return applyStyle (group, chart, spec, dh);
	}

	/**
	 * Data Model:
	 * 		JsonArray
	 * 			JsonObject [multiple series] 
	 * 				String 		title
	 * 				JsonArray 	values
	 * 					JsonArray [float...]
	 **/
	@Override
	public void bind (ComponentSpec.Binding binding, View view, ApplicationSpec applicationSpec, ComponentSpec spec, DataHolder dh) {

		if (view == null || !(view instanceof BarChart)) {
			// TODO: log
			return;
		}

		BindingSpec bindingSpec = spec.binding (binding);
		if (bindingSpec == null) {
			return;
		}

		BarChart chart = (BarChart)view;

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

				BarData data = chart.getData ();
				if (data == null) {
					data = new BarData ();
					chart.setData (data);
					// set bar width
					data.setBarWidth (Json.getFloat (style, Style.bar.Width, 0.9f));
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

					List<BarEntry> entries = new ArrayList<BarEntry> ();
					for (int j = 0; j < values.count (); j++) {
						JsonArray record = (JsonArray)values.get (j);

						JsonArray jValues = (JsonArray)record.get (0); // What's this?
						float [] fValues = null;
						if (jValues == null) {
							fValues = new float [] { 0f };
						} else {
							fValues = new float [jValues.count ()];
							for (int k = 0; k < jValues.count (); k++) {
								fValues [k] = Float.valueOf ((String)jValues.get (k));
							}
						}
						entries.add (new BarEntry (i, fValues, String.valueOf (record.get (0))));

					}

					BarDataSet dataSet = new BarDataSet (entries, Json.getString (series, Custom.Title));
					customStyle (dataSet);

					// add data set
					data.addDataSet (dataSet);
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

		// attach events

	}

}
