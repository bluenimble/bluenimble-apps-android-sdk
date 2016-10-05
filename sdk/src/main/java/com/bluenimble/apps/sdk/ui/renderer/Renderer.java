package com.bluenimble.apps.sdk.ui.renderer;

import java.io.Serializable;

import com.bluenimble.apps.sdk.application.UIActivity;
import com.bluenimble.apps.sdk.spec.ApplicationSpec;
import com.bluenimble.apps.sdk.spec.LayerSpec;
import com.bluenimble.apps.sdk.spec.PageSpec;

import android.view.View;
import android.view.ViewGroup;

public interface Renderer extends Serializable {

	void 		render(PageSpec page, UIActivity activity);
	View		render(ApplicationSpec application, LayerSpec layer, ViewGroup container, UIActivity activity);
	
	void 		clear(ApplicationSpec application, UIActivity activity);
	
	PageSpec 	current();

}
