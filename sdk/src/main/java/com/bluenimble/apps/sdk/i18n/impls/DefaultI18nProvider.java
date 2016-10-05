package com.bluenimble.apps.sdk.i18n.impls;

import java.util.Locale;

import com.bluenimble.apps.sdk.Json;
import com.bluenimble.apps.sdk.Lang.VariableResolver;
import com.bluenimble.apps.sdk.i18n.I18nProvider;
import com.bluenimble.apps.sdk.json.JsonObject;

public class DefaultI18nProvider implements I18nProvider {

	private static final long serialVersionUID = -1077735279428664273L;
	
	protected JsonObject 	i18n;
	protected String 		defaultLang;
	protected String 		deviceLang 	= Locale.getDefault ().getLanguage ().toLowerCase (); 
	
	public DefaultI18nProvider () {
		this.i18n = new JsonObject ();
	}
	
	public DefaultI18nProvider (JsonObject i18n) {
		this.i18n = i18n;
	}
	
	@Override
	public Object get (String [] property, VariableResolver vr) {
		JsonObject data = (JsonObject)Json.find (i18n, property);
		if (data == null) {
			return null;
		}
		String language = deviceLang;
		if (!data.containsKey (language)) {
			language = defaultLang;
		}
		Object value = data.get (language);
		if (value == null) {
			return null;
		}
		
		return Json.resolve (value, vr);
	}

	@Override
	public JsonObject all () {
		return i18n;
	}

	@Override
	public void add (String language, String key, Object value) {
		if (i18n == null) {
			i18n = new JsonObject ();
		}
		Json.set (i18n, value, key, language);
	}

	@Override
	public void add (String key, Object value) {
		if (i18n == null) {
			i18n = new JsonObject ();
		}
		i18n.set (key, value);
	}

	@Override
	public void setDefaultLanguage (String defaultLang) {
		this.defaultLang = defaultLang;
	}

}
