package com.bluenimble.apps.sdk.spec;

import com.bluenimble.apps.sdk.backend.Backend;
import com.bluenimble.apps.sdk.controller.Controller;
import com.bluenimble.apps.sdk.i18n.I18nProvider;
import com.bluenimble.apps.sdk.logging.Logger;
import com.bluenimble.apps.sdk.ui.components.ComponentsRegistry;
import com.bluenimble.apps.sdk.ui.effects.EffectsRegistry;
import com.bluenimble.apps.sdk.ui.renderer.Renderer;
import com.bluenimble.apps.sdk.ui.themes.ThemesRegistry;

public interface ApplicationSpec extends EventAwareSpec {

	String 				name();
	String 				description();
	SdkVersion 			version();
	String 				defaultLanguage();
	String 				logLevel();

	ThemeSpec 			theme();

	PageSpec 			main();
	PageSpec 			first();
	PageSpec 			page(String id);
	void				remove(String id);

	ThemesRegistry 		themesRegistry();

	I18nProvider 		i18nProvider();
	
	ComponentsRegistry 	componentsRegistry();

	EffectsRegistry 	effectsRegistry();

	Controller 			controller();

	Backend 			backend();

	Renderer 			renderer();

	Logger 				logger();

}
