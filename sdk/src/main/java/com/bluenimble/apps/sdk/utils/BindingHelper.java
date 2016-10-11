package com.bluenimble.apps.sdk.utils;

import android.util.Log;
import android.view.View;

import com.bluenimble.apps.sdk.Lang;
import com.bluenimble.apps.sdk.controller.DataHolder;
import com.bluenimble.apps.sdk.controller.impls.AgnosticDataHolder;
import com.bluenimble.apps.sdk.spec.ApplicationSpec;
import com.bluenimble.apps.sdk.spec.ComponentSpec;
import com.bluenimble.apps.sdk.spec.LayerSpec;
import com.bluenimble.apps.sdk.spec.PageSpec;
import com.bluenimble.apps.sdk.ui.effects.impls.BindEffect;
import com.bluenimble.apps.sdk.ui.renderer.ViewResolver;

import java.util.Iterator;

public class BindingHelper {

    public static void bindComponent (String tag, ViewResolver resolver, ApplicationSpec application, LayerSpec layer, ComponentSpec component, DataHolder dh, boolean useDh) {
        Log.d (tag, "\t\t    -> Bind Component [" + component.type () + "/" + component.id () + "]");
        View view = resolver.component (layer.id (), component.id ());
        if (view == null) {
            application.logger ().debug (BindEffect.class.getSimpleName (), "\t\t    -> ERR: View Not found [" + layer.id () + Lang.DOT + component.id () + "]");
            return;
        }
        application.componentsRegistry ().lookup (component.type ()).bind (ComponentSpec.Binding.Set, view, application, component, useDh ? dh : AgnosticDataHolder.Instance);
    }

    public static void bindLayer (String tag, ApplicationSpec application, LayerSpec layer, View layerView, DataHolder dh, boolean useDh) {

        if (layerView == null || layer.count () == 0) {
            return;
        }

        application.logger ().debug (tag, "\t\t  -> Bind Layer [" + layer.id () + "]");

        final View fLayerView = layerView;
        ViewResolver vr = new ViewResolver () {
            @Override
            public View layer (String id) {
                return fLayerView.findViewWithTag (id);
            }

            @Override
            public View component (String layerId, String componentId) {
                return fLayerView.findViewWithTag (layerId + Lang.DOT + componentId);
            }
        };
        for (int i = 0; i < layer.count (); i++) {
            bindComponent (tag, vr, application, layer, layer.component (i), dh, useDh);
        }
    }

    public static void bindPage (String tag, ViewResolver resolver, ApplicationSpec application, PageSpec page, DataHolder dh, boolean useDh) {
        if (page.count () == 0) {
            return;
        }

        application.logger ().debug (tag, "\t\t-> Bind Page ...");

        Iterator<String> layers = page.layers ();
        while (layers.hasNext ()) {
            String layerId = layers.next ();
            bindLayer (tag, application, page.layer (layerId), resolver.layer (layerId), dh, useDh);
        }
    }

}
