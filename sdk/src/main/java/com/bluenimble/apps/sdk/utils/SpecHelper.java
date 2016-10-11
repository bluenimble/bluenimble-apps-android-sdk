package com.bluenimble.apps.sdk.utils;

import android.view.View;

import com.bluenimble.apps.sdk.Json;
import com.bluenimble.apps.sdk.Lang;
import com.bluenimble.apps.sdk.Spec;
import com.bluenimble.apps.sdk.application.UIActivity;
import com.bluenimble.apps.sdk.controller.ActionProcessor;
import com.bluenimble.apps.sdk.controller.DataHolder;
import com.bluenimble.apps.sdk.json.JsonObject;
import com.bluenimble.apps.sdk.spec.ApplicationSpec;
import com.bluenimble.apps.sdk.spec.LayerSpec;
import com.bluenimble.apps.sdk.spec.PageSpec;
import com.bluenimble.apps.sdk.ui.components.impls.generic.BreakFactory;
import com.bluenimble.apps.sdk.ui.effects.impls.BindEffect;
import com.bluenimble.apps.sdk.ui.renderer.impls.DefaultRenderer;

public class SpecHelper {

    public static void fireCreateEvent (LayerSpec layer, UIActivity activity, View parent, DataHolder dh) {
        // run default layer create event / bind all
        ActionProcessor.process (DefaultRenderer.LifeCycleEvent.create.name (), SpecHelper.newCreateEvent (layer), activity, parent, dh);

        // run page create event if any
        JsonObject eventSpec = layer.event (DefaultRenderer.LifeCycleEvent.create.name ());
        if (eventSpec != null) {
            ActionProcessor.process (DefaultRenderer.LifeCycleEvent.create.name (), eventSpec, activity, parent, dh);
        }
    }

    public static JsonObject newCreateEvent (LayerSpec layer) {
        return (JsonObject)new JsonObject ()
                .set (Spec.Action.OnStart, new JsonObject ()
                        .set (BindEffect.Id, layer.id ()));
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

        return template;
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

        // components: txt, btn, inp, chk, rdg, list, ddn, map, chart, tabs

        JsonObject oComponent = new JsonObject ();

        spec = spec.trim ();

        if (spec.equals (Lang.SLASH))  {
            oComponent.set (Spec.page.layer.component.Type, BreakFactory.Id);
            return oComponent;
        }

        if (spec.startsWith (Lang.XMARK)) {
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

        for (int i = 2; i < tokens.length; i++) {
            String token = tokens [i].trim ();
            int indexOfEqual = token.indexOf (Lang.EQUALS);
            if (indexOfEqual <= 0) {
                continue;
            }
            String n = token.substring (0, indexOfEqual);
            String v = token.substring (indexOfEqual + 1);
            Json.set (oComponent, Lang.replace (v, Lang.PLUS, Lang.SPACE), Spec.page.layer.component.Custom.toLowerCase (), Lang.replace (n, Lang.PLUS, Lang.SPACE));
        }

        return oComponent;
    }

}
