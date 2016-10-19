package com.bluenimble.apps.sdk.spec.impls.json;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Locale;

import com.bluenimble.apps.sdk.Json;
import com.bluenimble.apps.sdk.Lang;
import com.bluenimble.apps.sdk.Spec;
import com.bluenimble.apps.sdk.json.JsonObject;
import com.bluenimble.apps.sdk.spec.PageSpec;
import com.bluenimble.apps.sdk.spec.ThemeSpec;
import com.bluenimble.apps.sdk.spec.impls.AbstractApplicationSpec;
import com.bluenimble.apps.sdk.ui.renderer.impls.DefaultRenderer;

import android.util.Log;

public class JsonApplicationSpec extends AbstractApplicationSpec {

	private static final long serialVersionUID = -5392390555922025109L;

	private ThemeSpec theme;
	
	private JsonObject spec;
	
	protected JsonApplicationSpec() throws Exception {
		spec = new JsonObject ();
	}
	
	public JsonApplicationSpec(InputStream archive) throws Exception {
		// unzip, read app, pages, themes
	}
	
	public JsonApplicationSpec(String id, JsonObject spec) throws Exception {
		this.id = id;
		init (spec);
	}

	protected void init (JsonObject spec) throws Exception {
		this.spec = spec;
		version = new JsonSdkVersion (Json.getObject (spec, Spec.sdkVersion.class.getSimpleName ()));
		
		initializeTheme (Json.getString (spec, Spec.Theme));
		
		events = Json.getObject (spec, Spec.Events);
		
		// load page declared inside app.json spec
		JsonObject pages = Json.getObject (spec, Spec.Pages);
		if (pages == null || pages.isEmpty ()) {
			return;
		}
		
		// load pages
		Iterator<String> ids = pages.keys ();
		while (ids.hasNext ()) {
			String pageId = ids.next ();
			add	(pageId, Json.getObject (pages, pageId)); 
		}
	}
	
	@Override
	public String name () {
		return Json.getString (spec, Spec.Name);
	}

	@Override
	public String description () {
		return Json.getString (spec, Spec.Description);
	}

	@Override
	public ThemeSpec theme () {
		return theme;
	}

	@Override
	public String logLevel () {
		return Json.getString (spec, Spec.LogLevel);
	}

	@Override
	public PageSpec main () {
		logger ().debug (JsonApplicationSpec.class.getSimpleName (), "Get Main Page " + Lang.ARRAY_OPEN + Json.getString (spec, Spec.Main) + Lang.ARRAY_CLOSE);
		PageSpec main = page (Json.getString (spec, Spec.Main));
		if (main != null) {
			logger ().debug (JsonApplicationSpec.class.getSimpleName (), "... Main Page Found ");
			return main;
		}
		return first ();
	}
	

	public PageSpec add	(String id, JsonObject pageSpec) {
		Log.d (JsonApplicationSpec.class.getSimpleName (), "Adding Page " + id);
		if (Lang.isNullOrEmpty (id) || Json.isNullOrEmpty (pageSpec)) {
			return null;
		}
		PageSpec page = new JsonPageSpec (id, pageSpec, this);
		this.pages.put (id, page);
		return page;
	}

	@Override
	public String defaultLanguage () {
		return Json.getString (spec, Spec.DefaultLanguage, Locale.getDefault ().getLanguage ());
	}

	private void initializeTheme (String themes) {
		String [] aThemes = null;
		if (Lang.isNullOrEmpty (themes)) {
			aThemes = new String [] { ThemeSpec.Default };
		} else {
			aThemes = Lang.split (themes, Lang.BLANK, true);
			if (!Lang.existsIn (ThemeSpec.Default, aThemes)) {
				aThemes = Lang.add (new String [] { ThemeSpec.Default }, aThemes);
			}
		}
		
		ThemeSpec merged = null;
		for (String t : aThemes) {
			if (t == null) {
				continue;
			}
			ThemeSpec theme = themesRegistry.lookup (t);
			if (theme == null) {
				continue;
			}
			if (merged == null) {
				merged = theme;
			} else {
				merged.merge (theme);
			}
		}
		theme = merged;

	}
	

}
