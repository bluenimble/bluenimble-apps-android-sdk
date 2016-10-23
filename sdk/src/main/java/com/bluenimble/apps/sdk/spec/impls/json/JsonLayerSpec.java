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
import com.bluenimble.apps.sdk.ui.components.ComponentFactory;
import com.bluenimble.apps.sdk.ui.components.ComponentsRegistry;
import com.bluenimble.apps.sdk.ui.components.impls.generic.BreakFactory;
import com.bluenimble.apps.sdk.ui.components.impls.generic.LayerFactory;
import com.bluenimble.apps.sdk.ui.themes.impls.JsonStyleSpec;
import com.bluenimble.apps.sdk.utils.SpecHelper;

public class JsonLayerSpec extends JsonEventAwareSpec implements LayerSpec {

	private static final long serialVersionUID = -5392390555922025109L;
	
	private static final JsonObject Break = (JsonObject)new JsonObject ().set (Spec.page.layer.component.Type, BreakFactory.Id);
	
	private 	String 		id;
	protected 	JsonObject 	spec;
	
	private 	StyleSpec	style;

	private 	Map<String, Integer> 	componentsIds 	= new HashMap<String, Integer> ();
	
	private 	List<ComponentSpec>		components		= null;
	
	public JsonLayerSpec (String id, JsonObject spec, PageSpec parentPage, ApplicationSpec application) {
		super (id, parentPage, Json.getObject (spec, Spec.Events), id);

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
			if (xComponent == null) {
				continue;
			}

			JsonObject oComponent = null;
			if (xComponent instanceof JsonObject) {
				oComponent = (JsonObject)xComponent;
			} else if (xComponent instanceof JsonArray) {
				oComponent = (JsonObject)new JsonObject ().set (Spec.page.layer.Components, xComponent);
			} else if (xComponent instanceof String) {
				oComponent = SpecHelper.toComponentSpec ((String)xComponent);
			}

			if (oComponent == null) {
				continue;
			}

			if (oComponent.containsKey (Spec.page.layer.Components)) {
				oComponent.set 		(Spec.page.layer.component.Type, LayerFactory.Id);
			}

			// if ref another component
			String type = Json.getString (oComponent, Spec.page.layer.component.Type);

			String ref = null;

			ComponentFactory factory = application.componentsRegistry ().lookup (type);
			if (factory == null) {
				ref = type;
			}

			if (ref != null) {
				String [] tokens = Lang.split (ref, Lang.DOT, true);
				if (tokens.length < 2) {
					continue;
				}
				String pageId 		= tokens [0];
				String layerId 		= tokens [1];
				String componentId 	= null;

				if (tokens.length == 3) {
					componentId 	= tokens [2];
				}
				// resolve
				PageSpec page = application.page (pageId);
				if (page == null) {
					continue;
				}
				LayerSpec layer = page.layer (layerId);
				if (layer == null) {
					continue;
				}

				if (!Lang.isNullOrEmpty (componentId)) {
					JsonComponentSpec refComponent = (JsonComponentSpec)layer.component (componentId);
					if (refComponent == null) {
						continue;
					}
					oComponent = refComponent.spec ().duplicate ().merge (oComponent);
				} else {
					oComponent = ((JsonLayerSpec)layer).spec ().duplicate ().merge (oComponent);
					oComponent.set (Spec.page.layer.component.Type, LayerFactory.Id);
				}
			}

			add (oComponent, i, parentPage, application);

			// add break if declared at the component level
			if (Json.getBoolean (oComponent, Spec.page.layer.component.Break, false)) {
				add (Break, Integer.MAX_VALUE, parentPage, application);
			}
		}
	}

	@Override
	public String id () {
		return id;
	}

	@Override
	public Object get (String name) {
		return Json.find (spec, Spec.page.layer.component.Custom, name);
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
	public boolean isCompact () {
		if (style == null) {
			return false;
		}
		if (isScrollable ()) {
			return true;
		}
		String sCompact = (String)style.get (StyleSpec.Compact);
		if (Lang.isNullOrEmpty (sCompact)) {
			return false;
		}
		return Lang.TrueValues.contains (sCompact.toLowerCase ());
	}

	@Override
	public boolean isScrollable () {
		if (style == null) {
			return false;
		}
		String sScrollable = (String)style.get (StyleSpec.Scroll);
		if (Lang.isNullOrEmpty (sScrollable)) {
			return false;
		}
		return Lang.TrueValues.contains (sScrollable.toLowerCase ());
	}

	@Override
	public boolean isRendered () {
		if (style == null) {
			return true;
		}

		boolean render = Json.getBoolean (spec, Spec.page.layer.Render, true);

		String sRender = (String)style.get (StyleSpec.Render);
		if (Lang.isNullOrEmpty (sRender)) {
			return render;
		}
		return Lang.TrueValues.contains (sRender.toLowerCase ());
	}

	@Override
	public ComponentSpec component (String id) {
		if (!componentsIds.containsKey (id)) {
			return null;
		}
		return component (componentsIds.get (id));
	}

	public JsonObject spec () {
		return spec;
	}

	private ComponentSpec add (JsonObject oComponent, int index, PageSpec page, ApplicationSpec application) {
		ComponentSpec component = new JsonComponentSpec (oComponent, id, page, application);
		this.components.add (component);
		if (!Lang.isNullOrEmpty (component.id ()) && index < Integer.MAX_VALUE) {
			componentsIds.put (component.id (), index);
		}
		return component;
	}

}
