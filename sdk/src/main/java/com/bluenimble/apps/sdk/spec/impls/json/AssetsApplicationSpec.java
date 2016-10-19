package com.bluenimble.apps.sdk.spec.impls.json;

import java.io.InputStream;
import java.util.Iterator;

import com.bluenimble.apps.sdk.IOUtils;
import com.bluenimble.apps.sdk.Lang;
import com.bluenimble.apps.sdk.application.UIApplication;
import com.bluenimble.apps.sdk.json.JsonObject;
import com.bluenimble.apps.sdk.spec.ViewSize;

import android.content.res.AssetManager;
import android.graphics.Typeface;

public class AssetsApplicationSpec extends JsonApplicationSpec {

	private static final long serialVersionUID = -5392390555922025109L;

	public AssetsApplicationSpec (UIApplication application) throws Exception {
		
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
		loadPages 	(assetManager, id + Lang.SLASH + UIApplication.Resources.Pages);
		
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
	
	private void loadThemes (AssetManager assetManager, ViewSize screenSize) throws Exception {
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
			logger ().debug (AssetsApplicationSpec.class.getSimpleName (), "\tLoad font " + f);
			String id = f;
			if (f.indexOf (Lang.DOT) > 0) {
				id = f.substring (0, f.indexOf (Lang.DOT));
			}

			id = id.toLowerCase ();

			logger ().debug (AssetsApplicationSpec.class.getSimpleName (), "\tRegister font " + id + Lang.GREATER + fontsPath + Lang.SLASH + f);
			fontsRegistry.register (id, Typeface.createFromAsset (assetManager, fontsPath + Lang.SLASH + f));
		}
	}
	
	private void loadPages (AssetManager assetManager, String path) throws Exception {
		// load pages from pages folder
		//
		String [] pagesIds = assetManager.list (path);
		if (pagesIds == null || pagesIds.length == 0) {
			return;
		}
		// load all pages
		for (String fPage : pagesIds) {

			String child = path + Lang.SLASH + fPage;

			logger ().debug (AssetsApplicationSpec.class.getSimpleName (), "Load page " + child);

			InputStream stream = null;

			boolean isFolder = true;

			try {
				stream = assetManager.open (child);
				isFolder = false;
			} catch (Exception ex) {
				// ignore
			}

			logger ().debug (AssetsApplicationSpec.class.getSimpleName (), "\t isFolder " + isFolder);

			// if it's a folder, load pages inside
			if (isFolder) {
				loadPages (assetManager, child);
				continue;
			}

			// load the page
			try {
				if (stream == null || !fPage.endsWith (UIApplication.PageExtension)) {
					continue;
				}
				add (
					child.substring (
						child.indexOf (UIApplication.Resources.Pages + Lang.SLASH) + (UIApplication.Resources.Pages + Lang.SLASH).length (),
						child.lastIndexOf (Lang.DOT)
					),
					new JsonObject (stream)
				);
			} finally {
				IOUtils.closeQuietly (stream);
			}
		}
	}
	
}
