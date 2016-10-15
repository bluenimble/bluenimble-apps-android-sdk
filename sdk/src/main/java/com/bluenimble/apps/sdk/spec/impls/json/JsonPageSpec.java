package com.bluenimble.apps.sdk.spec.impls.json;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.bluenimble.apps.sdk.Json;
import com.bluenimble.apps.sdk.Lang;
import com.bluenimble.apps.sdk.Spec;
import com.bluenimble.apps.sdk.controller.Action;
import com.bluenimble.apps.sdk.json.JsonArray;
import com.bluenimble.apps.sdk.json.JsonObject;
import com.bluenimble.apps.sdk.spec.ApplicationSpec;
import com.bluenimble.apps.sdk.spec.LayerSpec;
import com.bluenimble.apps.sdk.spec.PageSpec;
import com.bluenimble.apps.sdk.spec.StyleSpec;
import com.bluenimble.apps.sdk.ui.components.ComponentsRegistry;
import com.bluenimble.apps.sdk.ui.themes.impls.JsonStyleSpec;

public class JsonPageSpec extends JsonEventAwareSpec implements PageSpec {

	private static final long serialVersionUID = -5392390555922025109L;

	private String		id;
	private JsonObject 	spec;
	private StyleSpec	style;
	
	private Map<String, LayerSpec> layers;
	
	public JsonPageSpec (String id, InputStream stream, ApplicationSpec application) throws Exception {
		this (id, Json.load (stream), application);
	}
	
	public JsonPageSpec (String id, JsonObject spec, ApplicationSpec application) {
		super (Json.getObject (spec, Spec.Events), Action.Scope.None);

		this.id = id;
		
		JsonObject oLayers = Json.getObject (spec, Spec.page.Layers);
		
		// check if it's a simplified page / no layers directly declaring its components
		JsonArray oComponents = Json.getArray (spec, Spec.page.layer.Components);
		if (oComponents != null) {
			if (oLayers == null) {
				oLayers = new JsonObject ();
			} else {
				spec.remove (Spec.page.Layers);
			}
			oLayers.set (id, spec);
			spec = (JsonObject)new JsonObject ().set (Spec.page.Layers, oLayers);
		}
		
		this.spec = spec;

		String [] styles = Lang.split (Json.getString (spec, Spec.page.layer.component.Style), Lang.BLANK, true);
		
		style = new JsonStyleSpec (application.theme (), Lang.add (new String [] { Lang.STAR, ComponentsRegistry.Default.Page, id }, styles));
		
		if (oLayers == null || oLayers.isEmpty ()) {
			return;
		}
		
		// create SpecLayer objects
		Iterator<String> layers = oLayers.keys ();
		while (layers.hasNext ()) {
			String lyrId = layers.next ();
			JsonObject oLayer = null;
			Object o = oLayers.get (lyrId);
			if (o instanceof JsonObject) {
				oLayer = (JsonObject)o;
			} else if (o instanceof JsonArray) {
				JsonArray aLayer = (JsonArray)o;
				oLayer = (JsonObject)new JsonObject ().set (Spec.page.layer.Components, aLayer);
			}
			if (oLayer == null) {
				oLayer = new JsonObject ();
			}
			addLayer (lyrId, oLayer, application);
		}
		
	}

	@Override
	public String id () {
		return id;
	}

	@Override
	public String name () {
		return Json.getString (spec, Spec.Name);
	}

	@Override
	public StyleSpec style () {
		return style;
	}

	@Override
	public int count () {
		JsonObject layers = Json.getObject (spec, Spec.page.Layers);
		if (layers == null || layers.isEmpty ()) {
			return 0;
		}
		return layers.count ();
	}

	@Override
	public Iterator<String> layers () {
		JsonObject layers = Json.getObject (spec, Spec.page.Layers);
		if (layers == null || layers.isEmpty ()) {
			return null;
		}
		return layers.keys ();
	}

	@Override
	public LayerSpec layer (String id) {
		if (layers == null || layers.isEmpty ()) {
			return null;
		}
		if (!layers.containsKey (id)) {
			return null;
		}
		return layers.get (id);
	}
	
	public void addLayer (String id, JsonObject layer, ApplicationSpec application) {
		if (layers == null) {
			layers = new HashMap<String, LayerSpec> ();
		}
		layers.put (id, new JsonLayerSpec (id, layer, application));
	}

}
