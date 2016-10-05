package com.bluenimble.apps.sdk.ui.components.impls.chart;

import com.bluenimble.apps.sdk.json.JsonObject;
import com.bluenimble.apps.sdk.ui.components.AbstractComponentFactory;

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
	
}
