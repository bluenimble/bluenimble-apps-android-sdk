package com.bluenimble.apps.sdk.ui.components.impls.chart;

import android.graphics.Color;

import com.bluenimble.apps.sdk.Json;
import com.bluenimble.apps.sdk.json.JsonArray;
import com.bluenimble.apps.sdk.json.JsonObject;
import com.bluenimble.apps.sdk.ui.components.AbstractComponentFactory;
import com.github.mikephil.charting.data.DataSet;


public abstract class ChartFactory extends AbstractComponentFactory {

	private static final long serialVersionUID = 8437367345924192857L;
	
	interface Custom {
		String Title 	= "title";
	}

	interface Record {
		String Name 	= "name";
		String Values 	= "values";
	}
	
	interface Orientations {
		String BottomTop = "bt";
		String TopBottom = "tb";
		String LeftRight = "lr";
		String RightLeft = "rl";
	} 
	
	interface Style {
		String Animate 			= "animate";
		String Background       = "background";
		String Colors           = "colors";

		interface bar	{
			String 	Width 		= "width";
			String 	Fit	 		= "fit";
			String	Orientation = "orientation";
		}
		interface line {
			
		}
		interface bubble {
			
		}
		interface pie {
			
		}
		interface scatter {
			
		}
		interface radar {
			
		}
	}
	
	protected JsonObject style;

	protected void customStyle (DataSet dataSet) {
		if (style == null || style.isEmpty ()) {
			return;
		}

		JsonArray aColors = Json.getArray (style, Style.Colors);
		if (aColors != null && aColors.size () > 0) {
			int[] colors = new int [aColors.size ()];
			for (int i = 0; i < aColors.size (); i ++) {
				colors [i] = Color.parseColor ((String) aColors.get (i));
			}
			dataSet.setColors (colors);
		}
	}
}
