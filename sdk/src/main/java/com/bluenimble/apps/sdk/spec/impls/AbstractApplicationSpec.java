package com.bluenimble.apps.sdk.spec.impls;

import java.util.HashMap;
import java.util.Map;

import com.bluenimble.apps.sdk.Lang;
import com.bluenimble.apps.sdk.backend.Backend;
import com.bluenimble.apps.sdk.backend.impls.DefaultBackend;
import com.bluenimble.apps.sdk.controller.Controller;
import com.bluenimble.apps.sdk.controller.impls.DefaultController;
import com.bluenimble.apps.sdk.i18n.I18nProvider;
import com.bluenimble.apps.sdk.i18n.impls.DefaultI18nProvider;
import com.bluenimble.apps.sdk.logging.Logger;
import com.bluenimble.apps.sdk.logging.impls.DefaultLogger;
import com.bluenimble.apps.sdk.spec.ApplicationSpec;
import com.bluenimble.apps.sdk.spec.PageSpec;
import com.bluenimble.apps.sdk.spec.SdkVersion;
import com.bluenimble.apps.sdk.spec.impls.json.JsonEventAwareSpec;
import com.bluenimble.apps.sdk.ui.components.ComponentsRegistry;
import com.bluenimble.apps.sdk.ui.components.impls.DefaultComponentsRegistry;
import com.bluenimble.apps.sdk.ui.effects.EffectsRegistry;
import com.bluenimble.apps.sdk.ui.effects.impls.DefaultEffectsRegistry;
import com.bluenimble.apps.sdk.ui.renderer.Renderer;
import com.bluenimble.apps.sdk.ui.renderer.impls.DefaultRenderer;
import com.bluenimble.apps.sdk.ui.themes.FontsRegistry;
import com.bluenimble.apps.sdk.ui.themes.ThemesRegistry;
import com.bluenimble.apps.sdk.ui.themes.impls.DefaultFontsRegistry;
import com.bluenimble.apps.sdk.ui.themes.impls.DefaultThemesRegistry;

public abstract class AbstractApplicationSpec extends JsonEventAwareSpec implements ApplicationSpec {

	private static final long serialVersionUID = 6142608624996785182L;

	protected String id;

	protected SdkVersion 			version;
	
	protected ComponentsRegistry 	componentsRegistry 	= new DefaultComponentsRegistry (this);
	protected EffectsRegistry 		effectsRegistry		= new DefaultEffectsRegistry	();
	
	protected Controller 			controller			= new DefaultController 		();
	protected Backend 				backend				= new DefaultBackend 			();
	
	protected Renderer 				renderer			= new DefaultRenderer 			();
	protected Logger				logger				= new DefaultLogger 			();
	
	protected I18nProvider			i18nProvider		= new DefaultI18nProvider 		();
	protected ThemesRegistry 		themesRegistry		= new DefaultThemesRegistry 	();
	protected FontsRegistry 		fontsRegistry		= new DefaultFontsRegistry		();

	protected Map<String, PageSpec> pages = new HashMap<String, PageSpec> ();

	@Override
	public String id () {
		return id;
	}

	@Override
	public PageSpec page (String id) {
		if (pages.isEmpty ()) {
			return null;
		}
		if (Lang.isNullOrEmpty (id)) {
			return null;
		}
		return pages.get (id);
	}

	@Override
	public PageSpec first () {
		if (pages.isEmpty ()) {
			return null;
		}
		return pages.entrySet ().iterator ().next ().getValue ();
	}

	@Override
	public void remove (String id) {
		pages.remove (id);
	}
	
	@Override
	public ThemesRegistry themesRegistry () {
		return themesRegistry;
	}

	@Override
	public FontsRegistry fontsRegistry () {
		return fontsRegistry;
	}

	@Override
	public ComponentsRegistry componentsRegistry () {
		return componentsRegistry;
	}

	@Override
	public EffectsRegistry effectsRegistry () {
		return effectsRegistry;
	}

	@Override
	public Renderer renderer () {
		return renderer;
	}

	@Override
	public Controller controller () {
		return controller;
	}

	@Override
	public Backend backend () {
		return backend;
	}

	@Override
	public I18nProvider i18nProvider () {
		return i18nProvider;
	}

	@Override
	public Logger logger () {
		return logger;
	}

	@Override
	public SdkVersion version () {
		return version;
	}

	@Override
	public boolean isDiskBased () {
		return false;
	}

	public AbstractApplicationSpec componentsRegistry (ComponentsRegistry componentsRegistry) {
		this.componentsRegistry = componentsRegistry;
		return this;
	}

	public AbstractApplicationSpec effectsRegistry (EffectsRegistry effectsRegistry) {
		this.effectsRegistry = effectsRegistry;
		return this;
	}

	public AbstractApplicationSpec themesRegistry (ThemesRegistry themesRegistry) {
		this.themesRegistry = themesRegistry;
		return this;
	}

	public AbstractApplicationSpec pageRenderer (Renderer renderer) {
		this.renderer = renderer;
		return this;
	}

	public AbstractApplicationSpec controller (Controller controller) {
		this.controller = controller;
		return this;
	}

	public AbstractApplicationSpec backend (Backend backend) {
		this.backend = backend;
		return this;
	}

	public AbstractApplicationSpec i18nProvider (I18nProvider i18nProvider) {
		this.i18nProvider = i18nProvider;
		return this;
	}

	public AbstractApplicationSpec logger (Logger logger) {
		this.logger = logger;
		return this;
	}

}
