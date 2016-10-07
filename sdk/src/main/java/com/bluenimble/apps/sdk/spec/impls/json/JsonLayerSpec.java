package com.bluenimble.apps.sdk.spec.impls.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bluenimble.apps.sdk.Json;
import com.bluenimble.apps.sdk.Lang;
import com.bluenimble.apps.sdk.Spec;
import com.bluenimble.apps.sdk.json.JsonArray;
import com.bluenimble.apps.sdk.json.JsonObject;
import com.bluenimble.apps.sdk.spec.ApplicationSpec;
import com.bluenimble.apps.sdk.spec.ComponentSpec;
import com.bluenimble.apps.sdk.spec.LayerSpec;
import com.bluenimble.apps.sdk.spec.PageSpec;
import com.bluenimble.apps.sdk.spec.StyleSpec;
import com.bluenimble.apps.sdk.spec.ThemeSpec;
import com.bluenimble.apps.sdk.ui.components.ComponentsRegistry;
import com.bluenimble.apps.sdk.ui.components.impls.generic.BreakFactory;
import com.bluenimble.apps.sdk.ui.themes.impls.JsonStyleSpec;

public class JsonLayerSpec extends JsonEventAwareSpec implements LayerSpec {

	private static final long serialVersionUID = -5392390555922025109L;
	
	private static final JsonObject Break = (JsonObject)new JsonObject ().set (Spec.page.layer.component.Type, BreakFactory.Id);
	
	private 	String 		id;
	protected 	JsonObject 	spec;
	
	private 	StyleSpec	style;
	
	private 	Map<String, Integer> 	componentsIds 	= new HashMap<String, Integer> ();
	
	private 	List<ComponentSpec>		components		= null;
	
	public JsonLayerSpec (String id, JsonObject spec, ApplicationSpec application) {
		super (Json.getObject (spec, Spec.Events));
		
		this.id 	= id;
		this.spec 	= spec;
		if (this.spec == null || this.spec.isEmpty ()) {
			return;
		}

		ThemeSpec theme = application.theme ();
		
		String [] styles = Lang.split (Json.getString (spec, Spec.page.layer.component.Style), Lang.BLANK, true);
		
		style = new JsonStyleSpec (theme, Lang.add (new String [] { Lang.STAR, ComponentsRegistry.Default.Layer, id () }, styles));

		JsonArray oComponents = Json.getArray (spec, Spec.page.layer.Components);
		if (oComponents == null || oComponents.isEmpty ()) {
			return;
		}
		
		this.components = new ArrayList<ComponentSpec> ();
		
		for (int i = 0; i < oComponents.count (); i++) {
			Object xComponent = oComponents.get (i);
			JsonObject oComponent = null;
			if (xComponent instanceof JsonObject) {
				oComponent = (JsonObject)xComponent;
			} else if (xComponent instanceof String) {
				oComponent = toComponentSpec ((String)xComponent);
			}

			if (oComponent == null) {
				continue;
			}

			String ref = Json.getString (oComponent, Spec.page.layer.component.Ref);
			if (!Lang.isNullOrEmpty (ref)) {
				String [] tokens = Lang.split (ref, Lang.DOT, true);
				if (tokens.length < 3) {
					continue;
				}
				String pageId 		= tokens [0];
				String layerId 		= tokens [1];
				String componentId 	= tokens [2];
				// resolve
				PageSpec page = application.page (pageId);
				if (page == null) {
					continue;
				}
				LayerSpec layer = page.layer (layerId);
				if (layer == null) {
					continue;
				}
				JsonComponentSpec component = (JsonComponentSpec)layer.component (componentId);
				if (component == null) {
					continue;
				}
				oComponent = component.spec ();
			}

			add (oComponent, i, theme);

			// add break if declared at the component level
			if (Json.getBoolean (oComponent, Spec.page.layer.component.Break, false)) {
				add (Break, Integer.MAX_VALUE, theme);
			}
		}
	}

	private JsonObject toComponentSpec (String spec) {
		// txt:id Hello
		// btn:id Press Me
		// inp:id a.b.c 	e.g.h	Enter your...
		// chk:id ? 		e.g.h
		// lst:id a.b.c 	tpl1>g.d.c=2

		return null;
	}
	
	@Override
	public String id () {
		return id;
	}

	@Override
	public StyleSpec style () {
		return style;
	}
	
	@Override
	public int count () {
		if (components == null || components.isEmpty ()) {
			return 0;
		}
		return components.size ();
	}

	@Override
	public ComponentSpec component (int index) {
		if (components == null || components.isEmpty ()) {
			return null;
		}
		return components.get (index);
	}

	@Override
	public boolean isGlobal () {
		return Json.getBoolean (spec, Spec.page.layer.Global, false);
	}
	
	@Override
	public boolean isRendered () {
		return Json.getBoolean (spec, Spec.page.layer.Render, true);
	}

	@Override
	public ComponentSpec component (String id) {
		if (!componentsIds.containsKey (id)) {
			return null;
		}
		return component (componentsIds.get (id));
	}
	
	private ComponentSpec add (JsonObject oComponent, int index, ThemeSpec appTheme) {
		ComponentSpec component = new JsonComponentSpec (oComponent, appTheme);
		this.components.add (component);
		if (!Lang.isNullOrEmpty (component.id ()) && index < Integer.MAX_VALUE) {
			componentsIds.put (component.id (), index);
		}
		return component;
	}

}
