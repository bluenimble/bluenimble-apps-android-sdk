package com.bluenimble.apps.sdk.ui.components.impls.chart;

import android.graphics.Color;

import com.bluenimble.apps.sdk.Json;
import com.bluenimble.apps.sdk.Lang;
import com.bluenimble.apps.sdk.json.JsonObject;
import com.bluenimble.apps.sdk.ui.components.AbstractComponentFactory;
import com.github.mikephil.charting.data.DataSet;

import java.util.Random;

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
		String DataSet          = "dataSet";
		String Color            = "color";

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

	protected void customStyle (DataSet dataSet, int size) {
		int [] colors = new int [size];

		if (style == null || style.isEmpty ()) {
			completeFilling (colors, 0);
			dataSet.setColors (colors);
			return;
		}

		JsonObject dsStyle = Json.getObject (style, Style.DataSet);
		if (dsStyle == null || dsStyle.isEmpty ()) {
			completeFilling (colors, 0);
			dataSet.setColors (colors);
			return;
		}

		String sColors = Json.getString (dsStyle, Style.Color);
		if (Lang.isNullOrEmpty (sColors)) {
			completeFilling (colors, 0);
			dataSet.setColors (colors);
			return;
		}

		String [] aColors = sColors.split (Lang.SPACE);
		for (int i = 0; i < aColors.length; i ++) {
			if (i >= size) {
				break;
			}
			colors [i] = Color.parseColor (aColors [i]);
		}

		if (aColors.length < size) {
			completeFilling (colors, aColors.length);
		}

		dataSet.setColors (colors);
	}

	private void completeFilling (int [] colors, int min) {
		for (int i = min; i < colors.length; i ++) {
			Random rnd = new Random ();
			colors [i] = Color.argb (255, rnd.nextInt (256), rnd.nextInt (256), rnd.nextInt (256));
		}
	}
}
