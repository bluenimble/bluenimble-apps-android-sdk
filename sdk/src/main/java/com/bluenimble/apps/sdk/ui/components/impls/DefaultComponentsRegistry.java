package com.bluenimble.apps.sdk.ui.components.impls;

import java.util.HashMap;
import java.util.Map;

import com.bluenimble.apps.sdk.ui.components.ComponentFactory;
import com.bluenimble.apps.sdk.ui.components.ComponentsRegistry;
import com.bluenimble.apps.sdk.ui.components.impls.button.ButtonFactory;
import com.bluenimble.apps.sdk.ui.components.impls.chart.BarChartFactory;
import com.bluenimble.apps.sdk.ui.components.impls.chart.BubbleChartFactory;
import com.bluenimble.apps.sdk.ui.components.impls.chart.LineChartFactory;
import com.bluenimble.apps.sdk.ui.components.impls.chart.PieChartFactory;
import com.bluenimble.apps.sdk.ui.components.impls.chart.RadarChartFactory;
import com.bluenimble.apps.sdk.ui.components.impls.chart.ScatterChartFactory;
import com.bluenimble.apps.sdk.ui.components.impls.checkbox.CheckboxFactory;
import com.bluenimble.apps.sdk.ui.components.impls.generic.BreakFactory;
import com.bluenimble.apps.sdk.ui.components.impls.image.ImageFactory;
import com.bluenimble.apps.sdk.ui.components.impls.input.InputTextFactory;
import com.bluenimble.apps.sdk.ui.components.impls.map.MapFactory;
import com.bluenimble.apps.sdk.ui.components.impls.radio.RadioGroupFactory;
import com.bluenimble.apps.sdk.ui.components.impls.text.TextFactory;

public class DefaultComponentsRegistry implements ComponentsRegistry {

	private static final long serialVersionUID = 4244907635115742302L;

	protected Map<String, ComponentFactory> factories = new HashMap<String, ComponentFactory> ();
	
	public DefaultComponentsRegistry () {
		register (new TextFactory ());
		register (new CheckboxFactory ());
		register (new InputTextFactory ());
		register (new ButtonFactory ());
		register (new RadioGroupFactory ());
		register (new BreakFactory ());
		register (new ImageFactory ());
		
		register (new MapFactory ());

		register (new BarChartFactory ());
		register (new BubbleChartFactory ());
		register (new LineChartFactory ());
		register (new PieChartFactory ());
		register (new RadarChartFactory ());
		register (new ScatterChartFactory ());
	}
	
	@Override
	public ComponentFactory lookup (String id) {
		return factories.get (id);
	}

	@Override
	public void register (ComponentFactory factory) {
		factories.put (factory.id (), factory);
	}

}
