package com.bluenimble.apps.sdk.ui.effects.impls;

import android.view.View;

public class ShowEffect extends VisibilityEffect {

	private static final long serialVersionUID = 3783743185246914342L;
	
	private static final String Id = "show";

	public ShowEffect () {
		super (View.VISIBLE);
	}
	
	@Override
	public String id () {
		return Id;
	}

}
