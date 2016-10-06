package com.bluenimble.apps.sdk.spec.impls.json;

import java.io.InputStream;
import java.util.Iterator;

import com.bluenimble.apps.sdk.IOUtils;
import com.bluenimble.apps.sdk.Lang;
import com.bluenimble.apps.sdk.application.UIApplication;
import com.bluenimble.apps.sdk.json.JsonObject;

import android.content.res.AssetManager;
import android.graphics.Typeface;

public class AssetsApplicationSpec extends JsonBasedApplicationSpec {

	private static final long serialVersionUID = -5392390555922025109L;

	public AssetsApplicationSpec(UIApplication application) throws Exception {
		
		id = application.getString (UIApplication.Meta.Application, UIApplication.Defaults.Folder);
		
		AssetManager assetManager = application.getAssets ();
		
		// load themes
		loadThemes (assetManager, application.getSize (false));

		InputStream stream = null;
		try {
			stream = assetManager.open (id + Lang.SLASH + UIApplication.Resources.App);
			if (stream == null) {
				throw new Exception ("application spec file not found in assets/bluenimble [app.json]");
			}
			
			// init
			init 		(new JsonObject (stream));
			
		} finally {
			IOUtils.closeQuietly (stream);
		}
			
		// loadPages
		loadPages 	(assetManager);
		
		// load i18n
		loadI18n (assetManager);
		
		// load backend
		loadBackend (assetManager);

	}
	
	private void loadI18n (AssetManager assetManager) throws Exception {
		InputStream stream = null;
		try {
			stream = assetManager.open (id + Lang.SLASH + UIApplication.Resources.Static);
		} catch (Exception ex) {
			// Ignore
		}	
		try {	
			if (stream == null) {
				return;
			}
			JsonObject i18n = new JsonObject (stream);
			if (i18n.isEmpty ()) {
				return;
			}
			Iterator<String> keys = i18n.keys ();
			while (keys.hasNext ()) {
				String key = keys.next ();
				i18nProvider.add (key, i18n.get (key));
			}
		} finally {
			IOUtils.closeQuietly (stream);
		}
	}
	
	private void loadBackend (AssetManager assetManager) throws Exception {
		InputStream stream = null;
		try {
			stream = assetManager.open (id + Lang.SLASH + UIApplication.Resources.Backend);
		} catch (Exception ex) {
			// Ignore
		}	
		try {	
			if (stream == null) {
				return;
			}
			backend.load (new JsonObject (stream));
		} finally {
			IOUtils.closeQuietly (stream);
		}
	}
	
	private void loadThemes (AssetManager assetManager, float [] screenSize) throws Exception {
		String [] themes = assetManager.list (id + Lang.SLASH + UIApplication.Resources.Themes);
		if (themes == null || themes.length == 0) {
			return;
		}
		
		// load all themes
		for (String fTheme : themes) {
			String themePath = id + Lang.SLASH + UIApplication.Resources.Themes + Lang.SLASH + fTheme;
			InputStream stream = assetManager.open (themePath + Lang.SLASH + UIApplication.Resources.Theme);
			if (stream == null) {
				continue;
			}
			themesRegistry.load (fTheme, stream, screenSize);

			// load fonts
			String fontsPath = themePath + Lang.SLASH + UIApplication.Resources.Fonts;
			String [] fonts = assetManager.list (fontsPath);
			if (fonts == null || fonts.length == 0) {
				continue;
			}
			loadFonts (assetManager, fontsPath, fonts);
		}
	}

	private void loadFonts (AssetManager assetManager, String fontsPath, String [] fonts) throws Exception {
		for (String f : fonts) {
			String id = f;
			if (f.indexOf (Lang.DOT) > 0) {
				id = f.substring (0, f.indexOf (Lang.DOT));
			}
			fontsRegistry.register (id, Typeface.createFromAsset (assetManager, fontsPath + Lang.SLASH + f));
		}
	}
	
	private void loadPages (AssetManager assetManager) throws Exception {
		// load pages from pages folder
		String [] pagesIds = assetManager.list (id + Lang.SLASH + UIApplication.Resources.Pages);
		if (pagesIds == null || pagesIds.length == 0) {
			return;
		}
		// load all pages
		for (String fPage : pagesIds) {
			InputStream stream = null;
			try {
				stream = assetManager.open (id + Lang.SLASH + UIApplication.Resources.Pages + Lang.SLASH + fPage);
				if (stream == null) {
					continue;
				}
				add (fPage.substring (0, fPage.indexOf (Lang.DOT)), new JsonObject (stream));
			} finally {
				IOUtils.closeQuietly (stream);
			}
		}
	}
	
}
