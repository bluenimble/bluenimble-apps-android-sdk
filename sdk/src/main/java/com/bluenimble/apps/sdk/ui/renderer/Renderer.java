package com.bluenimble.apps.sdk.ui.renderer;

import java.io.Serializable;

import com.bluenimble.apps.sdk.application.UIActivity;
import com.bluenimble.apps.sdk.controller.DataHolder;
import com.bluenimble.apps.sdk.spec.ApplicationSpec;
import com.bluenimble.apps.sdk.spec.LayerSpec;
import com.bluenimble.apps.sdk.spec.PageSpec;

import android.view.View;
import android.view.ViewGroup;

public interface Renderer extends Serializable {

	enum LifeCycleEvent {
		create,
		destroy,
		rotate,
		crash,
		warn,
		error
	}

	void 		render 	(PageSpec page, UIActivity activity, DataHolder dh);
	View		render 	(ApplicationSpec application, LayerSpec layer, DataHolder dh, ViewGroup container, UIActivity activity);
	
	void 		clear	(ApplicationSpec application, UIActivity activity);
	
	PageSpec 	current	();

}
