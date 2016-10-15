package com.bluenimble.apps.sdk.ui.themes.impls;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.bluenimble.apps.sdk.IOUtils;
import com.bluenimble.apps.sdk.Json;
import com.bluenimble.apps.sdk.Lang;
import com.bluenimble.apps.sdk.application.UIApplication;
import com.bluenimble.apps.sdk.json.JsonObject;
import com.bluenimble.apps.sdk.spec.ThemeSpec;
import com.bluenimble.apps.sdk.spec.ViewSize;
import com.bluenimble.apps.sdk.ui.themes.ThemesRegistry;
import com.bluenimble.apps.sdk.utils.AppResources;

import android.util.Log;

public class DefaultThemesRegistry implements ThemesRegistry {

	private static final long serialVersionUID = 1660627548394509356L;
	
	public static final String DefaultTheme = ThemeSpec.Default + ".theme";

	private static final String WbyH 		= "x";
	
	protected Map<String, ThemeSpec> themes = new HashMap<String, ThemeSpec> ();
	
	@Override
	public ThemeSpec lookup (String id) {
		if (Lang.isNullOrEmpty (id)) {
			return null;
		}
		return themes.get (id);
	}

	@Override
	public void register (ThemeSpec theme) {
		themes.put (theme.id (), theme);
	}
	
	@Override
	public void load (String id, InputStream isTheme, ViewSize screenSize) throws Exception {
		ThemeSpec defaultTheme = lookup (ThemeSpec.Default);
		if (defaultTheme == null) {
			_load (ThemeSpec.Default, getClass ().getResourceAsStream (DefaultTheme), screenSize);
		}
		_load (id, isTheme, screenSize);
	}

	private void _load (String id, InputStream isTheme, ViewSize screenSize) throws Exception {
		
		try {
		
			JsonObject allResolutions = Json.load (isTheme);
			if (allResolutions.isEmpty ()) {
				return;
			}

			JsonObject common = Json.getObject (allResolutions, Lang.STAR);
			if (!Json.isNullOrEmpty (common)) {
				resolveImagesAndFonts (id, Lang.STAR, common);
			}
			
			JsonObject theme = null;
			
			float 	diffInWidth = 0;
			String 	selected 	= null;
					
			Iterator<String> allKeys = allResolutions.keys ();
			while (allKeys.hasNext ()) {
				String key = allKeys.next ();
				float diff = select (screenSize, key);
				if (diff > 0) {
					continue;
				}
				if (diff > diffInWidth) {
					diffInWidth = diff;
				}
				theme 		= allResolutions.getObject (key);
				selected	= key;
			}
			
			if (theme == null) {
				theme = common;
			} else {
				resolveImagesAndFonts (id, selected, theme);
			}
			
			if (theme != null) {
				register (new JsonThemeSpec (id, theme));
			}
		
		} finally {
			IOUtils.closeQuietly (isTheme);
		}
	}

	private float select (ViewSize size, String resolution) {
		
		if (Lang.STAR.equals (resolution)) {
			return 1;
		}

		int indexOfX = resolution.indexOf (WbyH);
		if (indexOfX <= 0) {
			return 1;
		}
		
		String sWidth = resolution.substring (0, indexOfX);
		int resWidth = 0;
		try {
			resWidth = Integer.valueOf (sWidth);
		} catch (NumberFormatException nfex) { /* Ignore */ }
		
		if (resWidth == 0) {
			return 1;
		}
		
		return size.getWidth () - resWidth;
	}

	private void resolveImagesAndFonts (String id, String resolution, JsonObject theme) {
		if (Json.isNullOrEmpty (theme)) {
			return;
		}

		if (Lang.STAR.equals (resolution)) {
			resolution = UIApplication.Resources.Common;
		}

		Iterator<String> styles = theme.keys ();
		while (styles.hasNext ()) {
			String styleId = styles.next ();
			JsonObject style = Json.getObject (theme, styleId);
			// change background image path
			String image = (String)Json.find (style, JsonStyleSpec.Group.Background, JsonStyleSpec.Background.Image);
			if (!AppResources.exists (image)) {
				Json.set (style, id + Lang.SLASH + UIApplication.Resources.Images + Lang.SLASH + resolution + Lang.SLASH + image);
			}
		}

	}

}
