package com.bluenimble.apps.sdk.spec.impls.json;

import android.graphics.Typeface;

import com.bluenimble.apps.sdk.Json;
import com.bluenimble.apps.sdk.Lang;
import com.bluenimble.apps.sdk.application.UIApplication;
import com.bluenimble.apps.sdk.json.JsonObject;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Iterator;

public class DiskApplicationSpec extends JsonApplicationSpec {

	private static final long serialVersionUID = -5392390555922025109L;

	public DiskApplicationSpec () throws Exception {
	}

	public DiskApplicationSpec (UIApplication application) throws Exception {
		this (application.getString (UIApplication.Meta.Application, UIApplication.Defaults.Folder), application);
	}

	public DiskApplicationSpec (String folder, UIApplication application) throws Exception {
		this (new File (application.getFilesDir (), folder), application);
	}

	public DiskApplicationSpec (File root, UIApplication application) throws Exception {
		setup (root, application);
	}

	protected File getAppsFolder (UIApplication application) {
		File appsFolder = new File (application.getFilesDir (), UIApplication.Resources.Apps);
		if (!appsFolder.exists ()) {
			appsFolder.mkdir ();
		}
		return appsFolder;
	}

	protected void setup (File root, UIApplication application) throws Exception {
		this.id = root.getName ();

		// load themes
		loadThemes (root, application.getSize (false));

		// load descriptor app.json
		init (Json.load (new File (root, UIApplication.Resources.App)));

		// loadPages
		loadPages 	(root);

		// load i18n
		loadI18n (root);

		// load backend
		loadBackend (root);
	}
	
	private void loadI18n (File root) throws Exception {
		JsonObject i18n = Json.load (new File (root, UIApplication.Resources.Static));
		if (i18n.isEmpty ()) {
			return;
		}
		Iterator<String> keys = i18n.keys ();
		while (keys.hasNext ()) {
			String key = keys.next ();
			i18nProvider.add (key, i18n.get (key));
		}
	}
	
	private void loadBackend (File root) throws Exception {
		JsonObject oBackend = Json.load (new File (root, UIApplication.Resources.Backend));
		if (oBackend == null) {
			return;
		}
		backend.load (oBackend);
	}
	
	private void loadThemes (File root, float [] screenSize) throws Exception {
		File themesFolder = new File (root, UIApplication.Resources.Themes);
		if (!themesFolder.exists () || !themesFolder.isDirectory ()) {
			return;
		}
		File [] themes = themesFolder.listFiles(new FileFilter() {
			@Override
			public boolean accept (File file) {
				return file.isDirectory ();
			}
		});
		if (themes == null || themes.length == 0) {
			return;
		}
		
		// load all themes
		for (File fTheme : themes) {
			File theme = new File (fTheme, UIApplication.Resources.Theme);
			if (!theme.exists ()) {
				continue;
			}

			InputStream stream = new FileInputStream (new File (fTheme, UIApplication.Resources.Theme));
			themesRegistry.load (fTheme.getName (), stream, screenSize);

			// load fonts
			File fonts = new File (fTheme, UIApplication.Resources.Fonts);
			if (!fonts.exists () || !fonts.isDirectory ()) {
				continue;
			}
			File [] fontsList = fonts.listFiles ();
			if (fontsList == null || fontsList.length == 0) {
				continue;
			}
			loadFonts (fontsList);
		}
	}

	private void loadFonts (File [] fontsList) throws Exception {
		for (File f : fontsList) {
			String id = f.getName ();
			if (id.indexOf (Lang.DOT) > 0) {
				id = id.substring (0, id.indexOf (Lang.DOT));
			}
			fontsRegistry.register (id, Typeface.createFromFile (f));
		}
	}
	
	private void loadPages (File root) throws Exception {
		// load pages from pages folder
		File pagesFolder = new File (root, UIApplication.Resources.Pages);
		if (!pagesFolder.exists () || !pagesFolder.isDirectory ()) {
			return;
		}
		File [] pages = pagesFolder.listFiles ();
		if (pages == null || pages.length == 0) {
			return;
		}
		// load all pages
		for (File fPage : pages) {
			String name = fPage.getName ();
			add (name.substring (0, name.indexOf (Lang.DOT)), Json.load (fPage));
		}
	}

	@Override
	public boolean isDiskBased () {
		return true;
	}

}
