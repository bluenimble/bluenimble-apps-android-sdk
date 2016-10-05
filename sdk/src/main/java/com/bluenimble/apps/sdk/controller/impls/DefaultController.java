package com.bluenimble.apps.sdk.controller.impls;

import java.util.HashMap;
import java.util.Map;

import com.bluenimble.apps.sdk.controller.Action;
import com.bluenimble.apps.sdk.controller.Controller;

public class DefaultController implements Controller {

	private static final long serialVersionUID = -2142704246187423293L;
	
	protected Map<String, Action> actions;
	
	@Override
	public Action lockup (String id) {
		if (actions == null) {
			return null;
		}
		return actions.get (id);
	}

	@Override
	public void register (Action action) {
		if (actions == null) {
			actions = new HashMap<String, Action> ();
		}
		actions.put (action.id (), action);
	}
}
