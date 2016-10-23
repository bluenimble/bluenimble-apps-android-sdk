package com.bluenimble.apps.sdk.spec;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.bluenimble.apps.sdk.controller.DataHolder;

public interface StyleSpec extends Serializable {

	interface Colors {
		int Grey	= Color.parseColor ("#CACECF");
		int White	= Color.parseColor ("#FFFFFF");
		int Black	= Color.parseColor ("#000000");
	}

	int 	DefaultShadowTick 	= 20;
	String 	Zero 				= "0";
	String 	Unknown 			= "?";

	int 	UndefinedInteger 	= Integer.MAX_VALUE;

	String 	Visible				= "visible";
	String 	Disable				= "disable";
	String 	Compact				= "compact";
	String 	Scroll				= "scroll";
	String 	Render				= "render";

	interface Group {
		String Text 		= "text";
		String Background 	= "background";
		String Border 		= "border";
		String Shadow 		= "shadow";

		String Size 		= "size";
		String Align 		= "align";

		String Margin 		= "margin";
		String Padding 		= "padding";
	}

	interface Align {
		String Vertical		= "vertical";
		String Horizontal 	= "horizontal";
		interface Values {
			String Center 	= "center";
			String Middle 	= "middle";

			String Top 		= "top";
			String Bottom 	= "bottom";

			String Left 	= "left";
			String Right 	= "right";
		}
	}

	interface Background {
		String Color 		= "color";
		String Image 		= "image";
		String Opacity		= "opacity";
		String Insets		= "insets";
		interface gradient {
			String Orientation 	= "orientation";
			String Radius		= "radius";
			String Center		= "center";
		}
	}

	interface Border {
		String Color 		= "color";
		String Size 		= "size";
		String Radius		= "radius";
	}

	interface Shadow {
		String Color 		= "color";
		String Tick 		= "tick";
	}

	interface Text {
		String Font 		= "font";
		String Size			= "size";
		String Color 		= "color";
		String Style 		= "style";
		String Align		= "align";
	}

	interface Size {
		String Width 		= "width";
		String Height 		= "height";
	}

	Object 	get		(String name);
	
	void 	apply 	(StylishSpec stylish, View view, ViewGroup parent, DataHolder dh);
	
}
