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
import com.bluenimble.apps.sdk.spec.ComponentSpec;
import com.bluenimble.apps.sdk.spec.LayerSpec;
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
	
	public JsonLayerSpec (String id, JsonObject spec, ThemeSpec appTheme) {
		super (Json.getObject (spec, Spec.Events));
		
		this.id 	= id;
		this.spec 	= spec;
		if (this.spec == null || this.spec.isEmpty ()) {
			return;
		}
		
		String [] styles = Lang.split (Json.getString (spec, Spec.page.layer.component.Style), Lang.BLANK, true);
		
		style = new JsonStyleSpec (appTheme, Lang.add (new String [] { Lang.STAR, ComponentsRegistry.Default.Layer, id () }, styles));

		JsonArray oComponents = Json.getArray (spec, Spec.page.layer.Components);
		if (oComponents == null || oComponents.isEmpty ()) {
			return;
		}
		
		this.components = new ArrayList<ComponentSpec> ();
		
		for (int i = 0; i < oComponents.count (); i++) {
			JsonObject oComponent = (JsonObject)oComponents.get (i);
			add (oComponent, i, appTheme);
			// add break if declared at the component level
			if (Json.getBoolean (oComponent, Spec.page.layer.component.Break, false)) {
				add (Break, Integer.MAX_VALUE, appTheme);
			}
		}
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
