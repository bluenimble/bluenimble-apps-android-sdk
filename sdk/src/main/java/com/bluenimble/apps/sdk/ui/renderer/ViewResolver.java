package com.bluenimble.apps.sdk.ui.renderer;

import android.view.View;

import java.io.Serializable;

public interface ViewResolver extends Serializable {

    View layer      (String id);
    View component  (String layerId, String componentId);

}

