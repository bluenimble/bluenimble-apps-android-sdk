package com.bluenimble.apps.sdk.ui.effects.impls;

public class UnbindEffect extends BindEffect {

	private static final long serialVersionUID = 3783743185246914342L;
	
	private static final String Id = "unbind";

	public UnbindEffect () {
		super (false);
	}
	
	@Override
	public String id () {
		return Id;
	}

}
