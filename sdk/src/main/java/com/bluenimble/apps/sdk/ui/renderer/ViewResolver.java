package com.bluenimble.apps.sdk.ui.renderer;

import android.view.View;

import java.io.Serializable;

public interface ViewResolver extends Serializable {

    View findView (String id);

}

