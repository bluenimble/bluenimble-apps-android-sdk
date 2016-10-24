package com.bluenimble.apps.sdk.application.ux;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.bluenimble.apps.sdk.ui.renderer.ViewResolver;

public class LayerLayout extends LinearLayout implements ViewResolver {

	public LayerLayout (Context context) {
		super (context);
	}

	@Override
	public View findView (String id) {
		return findViewWithTag (id);
	}
}
