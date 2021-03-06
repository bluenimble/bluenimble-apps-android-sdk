package com.bluenimble.apps.sdk.ui.themes;

import java.io.InputStream;
import java.io.Serializable;

import com.bluenimble.apps.sdk.spec.ThemeSpec;
import com.bluenimble.apps.sdk.spec.ViewSize;

public interface ThemesRegistry extends Serializable {

	ThemeSpec 	lookup		(String id);
	void 		register	(ThemeSpec theme);
	
	void 		load		(String id, InputStream isTheme, ViewSize screenSize) throws Exception;

}
