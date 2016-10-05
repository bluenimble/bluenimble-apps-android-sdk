package com.bluenimble.apps.sdk.ui.effects.impls;

import android.view.View;

public class HideEffect extends VisibilityEffect {

	private static final long serialVersionUID = 3783743185246914342L;
	
	public static final String Id = "hide";

	public HideEffect () {
		super (View.GONE);
	}
	
	@Override
	public String id () {
		return Id;
	}

}
