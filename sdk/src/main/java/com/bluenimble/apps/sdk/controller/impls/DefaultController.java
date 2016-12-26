package com.bluenimble.apps.sdk.controller.impls;

import java.util.HashMap;
import java.util.Map;

import com.bluenimble.apps.sdk.Json;
import com.bluenimble.apps.sdk.Lang;
import com.bluenimble.apps.sdk.Spec;
import com.bluenimble.apps.sdk.application.UIActivity;
import com.bluenimble.apps.sdk.controller.Action;
import com.bluenimble.apps.sdk.controller.ActionInstance;
import com.bluenimble.apps.sdk.controller.Controller;
import com.bluenimble.apps.sdk.utils.SecurityHelper;

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

	@Override
	public void process (ActionInstance actionInstance, UIActivity activity, boolean checkPermissions) {

		activity.getSpec ().logger ().debug (DefaultController.class.getSimpleName (), "Process ActionInstance " + actionInstance);

		// TODO check permissions

		if (checkPermissions) {
			String [] permissions = Lang.split (Json.getString (actionInstance.eventSpec (), Spec.page.event.Permissions), Lang.SPACE, true);
			int requestCode = SecurityHelper.askPermission (permissions, activity);
			if (requestCode > SecurityHelper.NoRequestCode) {
				activity.registerActionInstance (requestCode, actionInstance);
				return;
			}
		}

		Action action = null;

		String actionId = Json.getString (actionInstance.eventSpec (), Spec.page.event.Action);

		if (!Lang.isNullOrEmpty (actionId)) {
			action = lockup (actionId);
		}
		if (action == null) {
			action = Action.Default;
		}

		// resolve all params in the event spec and call the action
		action.execute (actionInstance, activity);
	}

}
