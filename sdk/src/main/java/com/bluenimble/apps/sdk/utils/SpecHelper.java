package com.bluenimble.apps.sdk.utils;

import android.view.View;

import com.bluenimble.apps.sdk.Json;
import com.bluenimble.apps.sdk.Lang;
import com.bluenimble.apps.sdk.Spec;
import com.bluenimble.apps.sdk.application.UIActivity;
import com.bluenimble.apps.sdk.controller.Action;
import com.bluenimble.apps.sdk.controller.Controller;
import com.bluenimble.apps.sdk.controller.DataHolder;
import com.bluenimble.apps.sdk.controller.impls.actions.DefaultActionInstance;
import com.bluenimble.apps.sdk.json.JsonObject;
import com.bluenimble.apps.sdk.spec.ApplicationSpec;
import com.bluenimble.apps.sdk.spec.ComponentSpec;
import com.bluenimble.apps.sdk.spec.EventAwareSpec;
import com.bluenimble.apps.sdk.spec.LayerSpec;
import com.bluenimble.apps.sdk.spec.PageSpec;
import com.bluenimble.apps.sdk.spec.StylishSpec;
import com.bluenimble.apps.sdk.ui.components.ComponentFactory;
import com.bluenimble.apps.sdk.ui.components.impls.generic.BreakFactory;
import com.bluenimble.apps.sdk.ui.effects.impls.BindEffect;
import com.bluenimble.apps.sdk.ui.renderer.impls.DefaultRenderer;

public class SpecHelper {

    public static void fireCreateEvent (EventAwareSpec eventAwareSpec, String bind, UIActivity activity, View parent, boolean fireDefault, DataHolder dh) {
        // run page create event if any
		Controller controller = activity.getSpec ().controller ();

		JsonObject eventSpec = eventAwareSpec.event (DefaultRenderer.LifeCycleEvent.create.name ());
        if (eventSpec != null) {
			controller.process (
				DefaultActionInstance.create (DefaultRenderer.LifeCycleEvent.create.name (), eventSpec, dh, parent),
				activity,
				false
			);
        }

        // run default layer create event / bind all
        if (fireDefault) {
			controller.process (
				DefaultActionInstance.create (DefaultRenderer.LifeCycleEvent.create.name (), SpecHelper.newCreateEvent (bind), dh, parent),
				activity,
				false
			);
        }
    }

    public static JsonObject newCreateEvent (String bind) {
        return (JsonObject)new JsonObject ()
                .set (Spec.Action.Scope, Action.Scope.None)
                .set (Spec.Action.OnStart, new JsonObject ()
                        .set (BindEffect.Id, bind));
    }

    public static ApplicationSpec application (View view) {
        if (view == null) {
            return null;
        }
        return ((UIActivity)view.getContext ()).getSpec ();
    }

    public static LayerSpec template (ApplicationSpec application, String sTemplate) {
        if (Lang.isNullOrEmpty (sTemplate)) {
            return null;
        }

        LayerSpec template = null;

        PageSpec page  = null;

        String layerId = sTemplate;

        int indexOfDot = sTemplate.indexOf (Lang.DOT);
        if (indexOfDot > 0) {
            page 	= application.page (sTemplate.substring (0, indexOfDot));
            layerId = sTemplate.substring (indexOfDot + 1);
        }
        if (page == null) {
            page = application.renderer ().current ();
        }
        template = page.layer (layerId);

		application.logger ().debug (SpecHelper.class.getSimpleName (), "Template " + sTemplate + " -> " + template);

        return template;
    }

    public static StylishSpec stylish (ApplicationSpec application, String spec) {
        if (Lang.isNullOrEmpty (spec)) {
            return null;
        }

        PageSpec page = application.renderer ().current ();

        String layerId = spec;

        int indexOfDot = spec.indexOf (Lang.DOT);
        if (indexOfDot <= 0) {
            return page.layer (layerId);
        }

        layerId         = spec.substring (0, indexOfDot);
        String cmpId    = spec.substring (indexOfDot + 1);

        LayerSpec layer = page.layer (layerId);
        if (layer == null) {
            return null;
        }

        return layer.component (cmpId);
    }

    public static String getString (ComponentSpec spec, String name) {
        return getString (spec, name, null);
    }

    public static String getString (ComponentSpec spec, String name, String defaultValue) {
        Object value = spec.get (name);
        if (value == null) {
            return defaultValue;
        }
        String sValue = String.valueOf (value);
        if (Lang.isNullOrEmpty (sValue)) {
            return defaultValue;
        }
        return sValue;
    }

    public static boolean getBoolean (ComponentSpec spec, String name, boolean defaultValue) {
        Object value = spec.get (name);
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof Boolean || value.getClass ().equals (Boolean.TYPE)) {
            return (Boolean)value;
        }
        if (!(value instanceof String)) {
            return defaultValue;
        }
        String sValue = (String)value;
        return !Lang.isNullOrEmpty (sValue) && Lang.TrueValues.contains (sValue.trim ());
    }

    public static int getInteger (ComponentSpec spec, String name, int defaultValue) {
        Object value = spec.get (name);
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof Integer || value.getClass ().equals (Integer.TYPE)) {
            return (Integer)value;
        }
        if (!(value instanceof String)) {
            return defaultValue;
        }
        String sValue = (String)value;
        if (Lang.isNullOrEmpty (sValue)) {
            return defaultValue;
        }
        try {
            return Integer.valueOf (sValue.trim ());
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }

    public static double getDouble (ComponentSpec spec, String name, double defaultValue) {
        Object value = spec.get (name);
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof Double || value.getClass ().equals (Double.TYPE)) {
            return (Integer)value;
        }
        if (!(value instanceof String)) {
            return defaultValue;
        }
        String sValue = (String)value;
        if (Lang.isNullOrEmpty (sValue)) {
            return defaultValue;
        }
        try {
            return Double.valueOf (sValue.trim ());
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }

    public static float getFloat (ComponentSpec spec, String name, float defaultValue) {
        Object value = spec.get (name);
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof Float || value.getClass ().equals (Float.TYPE)) {
            return (Float)value;
        }
        if (!(value instanceof String)) {
            return defaultValue;
        }
        String sValue = (String)value;
        if (Lang.isNullOrEmpty (sValue)) {
            return defaultValue;
        }
        try {
            return Float.valueOf (sValue.trim ());
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }

    public static long getLong (ComponentSpec spec, String name, long defaultValue) {
        Object value = spec.get (name);
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof Integer || value.getClass ().equals (Integer.TYPE) ||
            value instanceof Long || value.getClass ().equals (Long.TYPE)) {
            return (Long)value;
        }
        if (!(value instanceof String)) {
            return defaultValue;
        }
        String sValue = (String)value;
        if (Lang.isNullOrEmpty (sValue)) {
            return defaultValue;
        }
        try {
            return Long.valueOf (sValue.trim ());
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }

    public static JsonObject getObject (ComponentSpec spec, String name) {
        Object value = spec.get (name);
        if (value == null) {
            return null;
        }
        if (!(value instanceof JsonObject)) {
            return null;
        }
        return (JsonObject)value;
    }

    public static JsonObject toComponentSpec (String spec) {
        // ! Hello
        // txt 			Hello
        // txt:id 		Hello

        // btn:id 		Hello
        // inp:id 		a.b.c 		e.g.h	placeholder=Enter your...
        // chk:id 		? 			e.g.h
        // lst:id 		a.b.c 		?		tpl1=g.d.c=2

        // type:id   	set   		get   	[proper cmp spec]

        // components: text, button, input, check, rdg, list, ddn, map, chart, tabs

        JsonObject oComponent = new JsonObject ();

        spec = spec.trim ();

        if (spec.equals (Lang.SLASH))  {
            oComponent.set (Spec.page.layer.component.Type, BreakFactory.Id);
            return oComponent;
        }

        if (spec.startsWith (Lang.XMARK)) {
            Json.set (oComponent, true, new String [] { Spec.page.layer.component.Custom, ComponentFactory.Custom.Constant});
            oComponent.set (Spec.page.layer.component.binding.class.getSimpleName (), new JsonObject ().set (Spec.page.layer.component.binding.Set, spec.substring (1).trim ()));
            return oComponent;
        }

        String typeAndId 	= null;
        String remaining 	= null;

        int indexOfSpace = spec.indexOf (Lang.SPACE);
        if (indexOfSpace < 0) {
            typeAndId = spec;
        } else {
            typeAndId = spec.substring (0, indexOfSpace);
            remaining = spec.substring (indexOfSpace).trim ();
        }

        String type = null;
        String id 	= null;

        int indexOfColon = typeAndId.indexOf (Lang.COLON);
        if (indexOfColon < 0) {
            type 	= typeAndId;
        } else {
            type 	= typeAndId.substring (0, indexOfColon);
            id 		= typeAndId.substring (indexOfColon + 1).trim ();
        }

        oComponent.set (Spec.page.layer.component.Type, type);
        oComponent.set (Spec.page.layer.component.Id, 	id);

        if (Lang.isNullOrEmpty (remaining)) {
            return oComponent;
        }

        // remaining
        remaining.trim ();

        String [] tokens = Lang.split (remaining, Lang.SPACE, true);

        String set = tokens [0];

        if (!set.equals (Lang.QMARK)) {
            Json.set (oComponent, Lang.replace (set, Lang.PLUS, Lang.SPACE), Spec.page.layer.component.binding.class.getSimpleName (), Spec.page.layer.component.binding.Set);
        }
        if (tokens.length == 1) {
            return oComponent;
        }

        String get = tokens [1];

        if (!get.equals (Lang.QMARK)) {
            Json.set (oComponent, Lang.replace (get, Lang.PLUS, Lang.SPACE), Spec.page.layer.component.binding.class.getSimpleName (), Spec.page.layer.component.binding.Get);
        }
        if (tokens.length == 2) {
            return oComponent;
        }

        String style = Lang.BLANK;

        for (int i = 2; i < tokens.length; i++) {
            String token = tokens [i].trim ();
            int indexOfEqual = token.indexOf (Lang.EQUALS);
            if (indexOfEqual <= 0) {
                style += Lang.SPACE + token;
                continue;
            }
            String n = token.substring (0, indexOfEqual);
            String v = token.substring (indexOfEqual + 1);
            Json.set (oComponent, Lang.replace (v, Lang.PLUS, Lang.SPACE), Spec.page.layer.component.Custom.toLowerCase (), Lang.replace (n, Lang.PLUS, Lang.SPACE));
        }

        if (!Lang.isNullOrEmpty (style)) {
            Json.set (oComponent, style.trim (), Spec.page.layer.component.Style);
        }

        return oComponent;
    }

}
