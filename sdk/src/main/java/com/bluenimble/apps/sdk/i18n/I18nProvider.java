package com.bluenimble.apps.sdk.i18n;

import java.io.Serializable;

import com.bluenimble.apps.sdk.Lang.VariableResolver;
import com.bluenimble.apps.sdk.json.JsonObject;

public interface I18nProvider extends Serializable {

	void		setDefaultLanguage(String language);
	
	void		add(String key, Object value);
	
	void		add(String language, String key, Object value);
	
	Object 		get(String[] property, VariableResolver vr);
	
	JsonObject 	all();
	
}
