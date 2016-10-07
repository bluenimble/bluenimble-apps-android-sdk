package com.bluenimble.apps.sdk.ui.themes.impls;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.bluenimble.apps.sdk.Json;
import com.bluenimble.apps.sdk.Lang;
import com.bluenimble.apps.sdk.application.UIActivity;
import com.bluenimble.apps.sdk.application.UIApplication;
import com.bluenimble.apps.sdk.json.JsonObject;
import com.bluenimble.apps.sdk.spec.ComponentSpec;
import com.bluenimble.apps.sdk.spec.LayerSpec;
import com.bluenimble.apps.sdk.spec.PageSpec;
import com.bluenimble.apps.sdk.spec.StyleSpec;
import com.bluenimble.apps.sdk.spec.StylishSpec;
import com.bluenimble.apps.sdk.spec.ThemeSpec;
import com.bluenimble.apps.sdk.ui.components.impls.generic.BreakFactory;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class JsonStyleSpec implements StyleSpec {

	private static final long serialVersionUID = -2681053059579403387L;
	
	private JsonObject style;
	
	private static final int 		DefaultShadowTick 	= 20;
	
	interface Colors {
		int Grey	= Color.parseColor ("#CACECF"); 
		int White	= Color.parseColor ("#FFFFFF"); 
		int Black	= Color.parseColor ("#000000"); 
	}
	
	private static final String 	Zero 				= "0";

	private static final int 		UndefinedInteger 	= Integer.MAX_VALUE;
	
	private static final String 	Visible	= "visible";

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
	
	private static final Set<String> Groups = new HashSet<String> ();
	static {
		Groups.add (Group.Text); Groups.add (Group.Background); Groups.add (Group.Size); 
		Groups.add (Group.Align); Groups.add (Group.Margin); Groups.add (Group.Padding);
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
	
	private static Map<String, Integer> ComponentAlign = new HashMap<String, Integer> ();
	static {
		ComponentAlign.put (Align.Values.Center, 	RelativeLayout.CENTER_HORIZONTAL);
		ComponentAlign.put (Align.Values.Middle, 	RelativeLayout.CENTER_VERTICAL);
		ComponentAlign.put (Align.Values.Left, 		RelativeLayout.ALIGN_PARENT_LEFT);
		ComponentAlign.put (Align.Values.Right, 	RelativeLayout.ALIGN_PARENT_RIGHT);
		ComponentAlign.put (Align.Values.Top, 		RelativeLayout.ALIGN_PARENT_TOP);
		ComponentAlign.put (Align.Values.Bottom, 	RelativeLayout.ALIGN_PARENT_BOTTOM);
	}
	
	private static Map<String, Integer> TextStyle = new HashMap<String, Integer> ();
	static {
		TextStyle.put ("bold", 			Typeface.BOLD);
		TextStyle.put ("italic", 		Typeface.ITALIC);
		TextStyle.put ("bold_italic", 	Typeface.BOLD_ITALIC);
		TextStyle.put ("underline", 	Paint.UNDERLINE_TEXT_FLAG);
	}
	
	private static Map<String, Integer> TextAlign = new HashMap<String, Integer> ();
	static {
		TextAlign.put (Align.Values.Center, Gravity.CENTER_HORIZONTAL);
		TextAlign.put (Align.Values.Middle, Gravity.CENTER_VERTICAL);
		TextAlign.put (Align.Values.Left, 	Gravity.LEFT);
		TextAlign.put (Align.Values.Right, 	Gravity.RIGHT);
		TextAlign.put (Align.Values.Top, 	Gravity.TOP);
		TextAlign.put (Align.Values.Bottom, Gravity.BOTTOM);
	}
	
	private static final Map<String, Orientation> 	GradientTypes 				= new HashMap<String, Orientation> ();
	static {
		GradientTypes.put ("tb", Orientation.TOP_BOTTOM);
		GradientTypes.put ("bt", Orientation.BOTTOM_TOP);
		GradientTypes.put ("lr", Orientation.LEFT_RIGHT);
		GradientTypes.put ("rl", Orientation.RIGHT_LEFT);
		GradientTypes.put ("bl-tr", Orientation.BL_TR);
		GradientTypes.put ("tl-br", Orientation.TL_BR);
		GradientTypes.put ("bl-tl", Orientation.BR_TL);
		GradientTypes.put ("tl-bl", Orientation.TR_BL);
	}
	
	public JsonStyleSpec (ThemeSpec appTheme, String [] aStyles) {
		
		if (appTheme == null) {
			return;
		}
		
		if (aStyles == null || aStyles.length == 0) {
			return;
		}
		
		Set<JsonObject> styles = new HashSet<JsonObject> (); 

		for (String style : aStyles) {
			if (style == null) {
				continue;
			}
			if (appTheme != null) {
				JsonObject sAppStyle = appTheme.style (style);
				if (sAppStyle != null) {
					styles.add (sAppStyle);
				}
			}
		}
		style = merge (styles);
	}
	
	private static JsonObject merge (Set<JsonObject> styles) {
		if (styles == null || styles.isEmpty ()) {
			return null;
		}
		
		JsonObject merged = new JsonObject ();
		
		for (JsonObject style : styles) {
			merged.merge (style);
		}
		
		if (merged.isEmpty ()) {
			return null;
		}
		
		return merged.shrink ();
	}
	
	@Override
	public void apply (StylishSpec stylish, final View view, final ViewGroup group) {
		
		Log.d (JsonStyleSpec.class.getSimpleName (), "Json of > " + view.getClass ().getSimpleName () + Lang.ENDLN + String.valueOf (style));
		
		if (Json.isNullOrEmpty (style) || stylish == null || view == null) {
			return;
		}
		
		float [] screenSize = UIApplication.getScreenSize ();
		Log.d (JsonStyleSpec.class.getSimpleName (), "\tScreen Size > width:" + screenSize [0] + Lang.COMMA + Lang.SPACE + "height: " + screenSize [1]);
		
		boolean isPage 		= isPage 	(stylish);
		boolean isLayer 	= isLayer 	(stylish);
		boolean isBreak 	= isBreak 	(stylish);
		
		Log.d (JsonStyleSpec.class.getSimpleName (), "Styling View " + Lang.ARRAY_OPEN + (group == null ? "Activity" : group.getId ()) + Lang.SLASH + view.getId () + Lang.COLON + stylish.getClass ().getSimpleName () + Lang.ARRAY_CLOSE);
		
		// set width & height / default to width->match , height (layout->wrap, all->match)
		if (!isPage) {
			ViewGroup.LayoutParams params = view.getLayoutParams ();
			JsonObject oSize = Json.getObject (style, Group.Size);
			
			int width = (int)parseFloat (oSize, Size.Width, isLayer ? (int)screenSize [0] : group.getWidth (), UndefinedInteger);
			if (width < 0) {
				int groupWidth = group.getLayoutParams ().width;
				width = (groupWidth > 0 ? groupWidth : group.getWidth ()) + width;
			}
			
			if (width == UndefinedInteger) {
				if (isLayer || isBreak) {
					width = ViewGroup.LayoutParams.MATCH_PARENT;
				} else {
					width = ViewGroup.LayoutParams.WRAP_CONTENT;
				}
			}
			Log.d (JsonStyleSpec.class.getSimpleName (), "\t-> width " + Lang.ARRAY_OPEN + width + Lang.ARRAY_CLOSE);
			
			int height = (int)parseFloat (oSize, Size.Height, isLayer ? (int)screenSize [1] : group.getHeight (), UndefinedInteger);
			if (height < 0) {
				int groupHeight = group.getLayoutParams ().height;
				height = (groupHeight > 0 ? groupHeight : group.getHeight ()) + height;
			}

			if (height == UndefinedInteger) {
				if (isLayer) {
					height = ViewGroup.LayoutParams.MATCH_PARENT;
				} else {
					height = ViewGroup.LayoutParams.WRAP_CONTENT;
				}
			}
			Log.d (JsonStyleSpec.class.getSimpleName (), "\t-> height " + Lang.ARRAY_OPEN + height + Lang.ARRAY_CLOSE);
			
			if (params == null) {
				if (isLayer) {
					params = new LinearLayout.LayoutParams (width, height);
				} else {
					params = new RelativeLayout.LayoutParams (width, height);
				}
				if (params instanceof RelativeLayout.LayoutParams && !isBreak && group.getChildCount () > 0) {
					int sibling = group.getChildAt (group.getChildCount () - 1).getId ();
					Log.d (JsonStyleSpec.class.getSimpleName (), "\t-> addRule RIGHT_OF sibling " + Lang.ARRAY_OPEN + sibling + Lang.ARRAY_CLOSE);
					((RelativeLayout.LayoutParams)params).addRule (RelativeLayout.RIGHT_OF, sibling);
				}
				view.setLayoutParams (params);
			} else {
				params.width = width;
				params.height = height;
			}
			
			// apply align.vertical & align.horizontal
			applyAlign (params);
			
			// apply margins
			applyMargin (params, isLayer, group);
			
		}

		UIApplication application = (UIApplication)((UIActivity)view.getContext ()).getApplication ();
		
		// apply font
		applyFont (view, application);
		
		// apply visible
		applyVisible (view);
		
		// apply padding
		applyPadding (view);
		
		// apply background (gradient, border, shadow, insets)
		applyBackground (view, application);

	}

	private boolean isBreak (StylishSpec stylish) {
		if (!(stylish instanceof ComponentSpec)) {
			return false;
		}
		
		ComponentSpec spec = (ComponentSpec)stylish;
		
		return BreakFactory.Id.equals (spec.type ());
	}

	private boolean isLayer (StylishSpec stylish) {
		return stylish instanceof LayerSpec;
	}
	
	private boolean isPage (StylishSpec stylish) {
		return stylish instanceof PageSpec;
	}
	
	private void applyMargin (ViewGroup.LayoutParams params, boolean isLayout, ViewGroup group) {
		String margin = Json.getString (style, Group.Margin);
		if (Lang.isNullOrEmpty (margin)) {
			return;
		}
		float [] bounds = bounds (margin, group.getLayoutParams ().width, group.getLayoutParams ().height);
		
		((RelativeLayout.LayoutParams)params).topMargin 	= (int)bounds [0];
		((RelativeLayout.LayoutParams)params).rightMargin 	= (int)bounds [1];
		((RelativeLayout.LayoutParams)params).bottomMargin 	= (int)bounds [2];
		((RelativeLayout.LayoutParams)params).leftMargin 	= (int)bounds [3];
	}

	private void applyAlign (ViewGroup.LayoutParams params) {
		if (!(params instanceof RelativeLayout.LayoutParams)) {
			return;
		}
		
		RelativeLayout.LayoutParams layoutParams = ((RelativeLayout.LayoutParams)params);
		
		String [] aligns = Lang.split (Json.getString (style, Group.Align), Lang.SPACE);
		if (aligns == null) {
			return;
		}
		
		for (String align : aligns) {
			Integer a = ComponentAlign.get (align.trim ());
			if (a == null) {
				continue;
			}
			layoutParams.addRule (a);
		}
	}
	
	private void applyFont (View component, UIApplication application) {
		if (!TextView.class.isAssignableFrom (component.getClass ())) {
			Log.d (JsonStyleSpec.class.getSimpleName (), "!!! Font Unsupported for " + component.getClass ().getSimpleName ());
			return;
		}
		
		Log.d (JsonStyleSpec.class.getSimpleName (), "\t applyFont on " + component.getClass ().getSimpleName ());
		
		TextView text = (TextView)component;
		
		JsonObject oFont = Json.getObject (style, Group.Text);
		
		String family = Json.getString (oFont, Text.Font);
		Log.d (JsonStyleSpec.class.getSimpleName (), "\t-> Text.Font " + Lang.ARRAY_OPEN + family + Lang.ARRAY_CLOSE);
		if (!Lang.isNullOrEmpty (family)) {
			text.setTypeface (application.getSpec ().fontsRegistry ().lookup (family));
		}
		
		String [] fontStyles = Lang.split (Json.getString (oFont, Text.Style), Lang.SPACE);
		if (fontStyles != null && fontStyles.length > 0) {
			for (String fs : fontStyles) {
				Integer iFontStyle = TextStyle.get (fs.toLowerCase ());
				if (iFontStyle == null) {
					continue;
				}
				if (iFontStyle == Paint.UNDERLINE_TEXT_FLAG) {
					text.setPaintFlags (text.getPaintFlags ()|Paint.UNDERLINE_TEXT_FLAG);
				} else {
					text.setTypeface (null, iFontStyle);
				}
			}
		}

		float fontSize = Json.getFloat (oFont, Text.Size, 0);
		Log.d (JsonStyleSpec.class.getSimpleName (), "\t-> Text.Size " + Lang.ARRAY_OPEN + fontSize + Lang.ARRAY_CLOSE);
		if (fontSize != 0) {
			text.setTextSize (fontSize);
		}
		
		String [] aligns = Lang.split (Json.getString (oFont, Text.Align), Lang.SPACE);
		if (aligns == null) {
			return;
		}
		
		for (String align : aligns) {
			Integer g = TextAlign.get (align.trim ());
			if (g == null) {
				continue;
			}
			text.setGravity (g);
		}
		
	}
	
	private void applyBackground (View view, UIApplication application) {
		JsonObject oBackground 	= Json.getObject (style, Group.Background);
		JsonObject oShadow 		= Json.getObject (style, Group.Shadow);
		JsonObject oBorder 		= Json.getObject (style, Group.Border);
		
		if (Json.isNullOrEmpty (oBackground) && Json.isNullOrEmpty (oBorder) && Json.isNullOrEmpty (oShadow)) {
			return;
		}
		
		// set image
		String image = Json.getString (oBackground, Background.Image);
		if (!Lang.isNullOrEmpty (image)) {
			Drawable dr = application.drawable (image);
			if (dr != null) {
				view.setBackground (application.drawable (image));
				return;
			}
		}
		
		Log.d (JsonStyleSpec.class.getSimpleName (), "\t-> applyBackground ");
		
		// create a gradient 
		GradientDrawable gradient = new GradientDrawable ();
	    
		// set gradient background color
		int [] colors 	= colors (Json.getString (oBackground, Background.Color));
		
		if (colors != null) {
			Log.d (JsonStyleSpec.class.getSimpleName (), "\t->     Colors.Count " + colors.length);
			if (colors.length == 1) {
				gradient.setColor (colors [0]); // no gradient
			} else {
				gradient.setColors (colors);
			}
		}
		
		// set opacity
		gradient.setAlpha ((int) (Json.getFloat (oBackground, Background.Opacity, 1) * 255));

		// set border and radius
		if (!Json.isNullOrEmpty (oBorder)) {
	 		int iBorderColor = Colors.Grey;
			String 	borderColor 	= Json.getString (oBorder, Border.Color);
	 		if (!Lang.isNullOrEmpty (borderColor)) {
	 			iBorderColor = Color.parseColor (borderColor);
	 		}
	 		int 	borderSize 		= Json.getInteger (oBorder, Border.Size, 1);
	 		
	 		gradient.setStroke (borderSize, iBorderColor);
	 		
	 		// apply radius
	 		applyRadius (view, gradient, Json.getString (oBorder, Border.Radius));
		}
		
		// set gradient orientation and type
		applyGradient (gradient, oBackground, false);
		
		// set gradient insets
		float [] insets = null;
		String sInsets = Json.getString (oBackground, Background.Insets);
		if (Lang.isNullOrEmpty (sInsets)) {
			insets = bounds (sInsets, view.getWidth (), view.getHeight ());
		}

		// shadow
		Drawable background = applyShadow (view, gradient, oShadow, oBorder, insets);
		
		// set background
		view.setBackground (background);
		
	}
	
	private void applyGradient (GradientDrawable gradient, JsonObject oBackground, boolean addDefault) {
		JsonObject oGradient = Json.getObject (oBackground, Background.gradient.class.getSimpleName ());
		if (Json.isNullOrEmpty (oGradient)) {
			if (addDefault) {
				oGradient = JsonObject.Blank;
			} else {
				return;
			}
		}
		
		Log.d (JsonStyleSpec.class.getSimpleName (), "\t-> applyGradient " + oGradient);
		
		Orientation orientation = null;
		
		String sOrientation = Json.getString (oGradient, Background.gradient.Orientation);
		Log.d (JsonStyleSpec.class.getSimpleName (), "\t-> S.Orientation " + Lang.ARRAY_OPEN + sOrientation + Lang.ARRAY_CLOSE);
		if (!Lang.isNullOrEmpty (sOrientation)) {
			orientation = GradientTypes.get (sOrientation);
		}
		
		Log.d (JsonStyleSpec.class.getSimpleName (), "\t-> N.Orientation " + Lang.ARRAY_OPEN + orientation + Lang.ARRAY_CLOSE);
		
		if (orientation != null) {
			gradient.setOrientation (orientation);
		} else {
			Log.d (JsonStyleSpec.class.getSimpleName (), "\t-> Apply Radial ");
			
			gradient.setGradientType (GradientDrawable.RADIAL_GRADIENT);
			gradient.setGradientRadius (Json.getFloat (oGradient, Background.gradient.Radius, 200));
			
			String sCenter = Json.getString (oGradient, Background.gradient.Center);
			if (!Lang.isNullOrEmpty (sCenter)) {
				String [] xy = Lang.split (sCenter, Lang.SPACE);
				if (xy.length == 2) {
					try {
						gradient.setGradientCenter (Float.parseFloat (xy [0]), Float.parseFloat (xy [1]));
					} catch (Exception ex) {
						// TODO log
					}
				}
			}
		}
	}
	
	private Drawable applyShadow (View component, GradientDrawable gradient, JsonObject oShadow, JsonObject oBorder, float [] insets) {
		if (Json.isNullOrEmpty (oShadow)) {
			if (insets != null) {
				// apply insets
				return new InsetDrawable (gradient, (int)insets [1], (int)insets [0], (int)insets [3], (int)insets [2]);
			} else {
				return gradient;
			}
		}
		
		// shadow drawable
		GradientDrawable shadow = new GradientDrawable ();
		
		// set gradient background color
		int [] colors 	= colors (Json.getString (oShadow, Shadow.Color));
		if (colors == null) {
			colors = new int [] { Colors.Grey, Colors.White };
		}
		if (colors.length == 1) {
			shadow.setColor (colors [0]); // no gradient
		} else {
			shadow.setColors (colors);
		}
		
		// apply background gradient
		applyGradient (shadow, oShadow, false);
		
		applyRadius (component, shadow, Json.getString (oBorder, Border.Radius));
		
		// create the layer
		LayerDrawable layer = new LayerDrawable (new Drawable [] { shadow, gradient });
		
		// set shadow and background insets
		float [] bounds = bounds (Json.getString (oShadow, Shadow.Tick, String.valueOf (DefaultShadowTick)), component.getWidth (), component.getHeight ());
		
		int [] bgInsets = new int [4];
		int [] shInsets = new int [4];
		
		for (int i = 0; i < bounds.length; i++) {
			int b = (int)bounds [i];
			int shin = 0;
			int bgin = 0;
			if (b < 0) {
				shin = (-1 * b);
			} else if (b > 0) {
				bgin = b;
			}
			shInsets [i] = shin;
			bgInsets [i] = bgin;
		}
		
		layer.setLayerInset (0, shInsets [3], shInsets [0], shInsets [1], shInsets [2]);
		layer.setLayerInset (1, bgInsets [3], bgInsets [0], bgInsets [1], bgInsets [2]);
		
		return layer;
	}

	private void applyPadding (View component) {
		String padding = Json.getString (style, Group.Padding);
		if (Lang.isNullOrEmpty (padding)) {
			return;
		}
		
		Log.d (JsonStyleSpec.class.getSimpleName (), "\t-> Apply padding " + Lang.ARRAY_OPEN + padding + Lang.ARRAY_CLOSE);
		
		float [] bounds = bounds (padding, component.getLayoutParams ().width, component.getLayoutParams ().height);
		
		Log.d (JsonStyleSpec.class.getSimpleName (), "\t->               " + Lang.ARRAY_OPEN + (int)bounds [3] + Lang.COMMA + (int)bounds [0] + Lang.COMMA + (int)bounds [1] + Lang.COMMA + (int)bounds [2] + Lang.ARRAY_CLOSE);
		
		component.setPadding ((int)bounds [3], (int)bounds [0], (int)bounds [1], (int)bounds [2]);
	}
	
	private void applyVisible (View component) {
		component.setVisibility (Json.getBoolean (style, Visible, true) ?  View.VISIBLE : View.GONE);
	}

	private void applyRadius (View component, GradientDrawable gradient, String radius) {
		if (Lang.isNullOrEmpty (radius)) {
			return;
		}
		
		float [] bounds = bounds (radius, component.getLayoutParams ().width, component.getLayoutParams ().height);
		
		gradient.setCornerRadii (new float [] {
			bounds [0], bounds [0],
			bounds [1], bounds [1],
			bounds [2], bounds [2],
			bounds [3], bounds [3]
		});
	}
	
	private float parseFloat (JsonObject source, String name, int max, float defaultValue) {
		return parseFloat (Json.getString (source, name), max, defaultValue);
	}
	
	private float parseFloat (String sValue, int max, float defaultValue) {
		if (Lang.isNullOrEmpty (sValue)) {
			return defaultValue;
		}
		
		sValue = Lang.replace (sValue, Lang.SPACE, Lang.BLANK);
		
		if (Lang.PERCENT.equals (sValue)) {
			return defaultValue;
		}
		
		boolean isPercent = sValue.endsWith (Lang.PERCENT);
		if (isPercent && max > 0) {
			Log.d (JsonStyleSpec.class.getSimpleName (), "\tGet % value > max:" + max);
			try {
				float value = max * (Float.valueOf (sValue.substring (0, sValue.length () -1)) / 100);
				Log.d (JsonStyleSpec.class.getSimpleName (), "\t% value > value:" + value);
				return value;
			} catch (NumberFormatException nfex) {
				// TODO : Log
				return defaultValue;
			}
		}
		
		try {
			return Float.valueOf (sValue);
		} catch (NumberFormatException nfex) {
			// TODO : Log
			return defaultValue;
		}
	}
	
	private float [] bounds (String sBounds, int wmax, int hmax) {
		
		float [] bounds = new float [] {0f, 0f, 0f, 0f};
		
		String [] aBounds = Lang.split (sBounds, Lang.SPACE, true);
		if (aBounds == null) {
			return bounds;
		}
		
		if (aBounds.length == 1) {
			aBounds = Lang.add (aBounds, new String [] { aBounds [0], aBounds [0], aBounds [0]});
		} else if (aBounds.length < 4) {
			aBounds = Lang.add (aBounds, new String [] { Zero, Zero});
		}
		
		for (int i = 0; i < aBounds.length; i++) {
			bounds [i] = parseFloat (aBounds [i], i % 2 == 0 ? hmax : wmax, 0);
		}
		
		return bounds;
		
	}
	
	private int [] colors (String sColors) {
		String [] colors 	= Lang.split (sColors, Lang.SPACE, true);
		if (colors == null || colors.length == 0) {
			return null;
		}
		int [] iColors = new int [colors.length];
		for (int i = 0; i < colors.length; i++) {
			iColors [i] = Color.parseColor (colors [i]);
		}
		return iColors;
	}

	@Override
	public Object get (String name) {
		if (Json.isNullOrEmpty (style)) {
			return null;
		}
		return style.get (name);
	}

}
