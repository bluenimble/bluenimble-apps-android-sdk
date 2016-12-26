package com.bluenimble.apps.sdk.utils;

import com.bluenimble.apps.sdk.Json;
import com.bluenimble.apps.sdk.application.UIActivity;
import com.bluenimble.apps.sdk.controller.ActionInstance;
import com.bluenimble.apps.sdk.controller.impls.actions.DefaultAction;
import com.bluenimble.apps.sdk.json.JsonObject;
import com.bluenimble.apps.sdk.spec.ApplicationSpec;
import com.bluenimble.apps.sdk.spec.PageSpec;
import com.bluenimble.apps.sdk.ui.effects.Effect;

import java.util.Iterator;

public class EffectsHelper {

	public static void applyEffects (ActionInstance actionInstance, String name, UIActivity activity, PageSpec page) {

		ApplicationSpec application = activity.getSpec ();

		JsonObject effects = Json.getObject (actionInstance.eventSpec (), name);

		application.logger ().debug (DefaultAction.class.getSimpleName (), "Apply Effects \n" + effects);

		if (effects == null || effects.isEmpty ()) {
			return;
		}

		Json.resolve (effects, activity.getSpec ().expressionCompiler (), actionInstance.dataHolder ());

		Iterator<String> ids = effects.keys ();
		while (ids.hasNext ()) {
			String effectId = ids.next ();
			application.logger ().debug (DefaultAction.class.getSimpleName (), "\tApply Effect [" + effectId + "]");
			Effect effect = application.effectsRegistry ().lockup (effectId);
			if (effect == null) {
				// TODO: log warning
				continue;
			}
			application.logger ().debug (DefaultAction.class.getSimpleName (), "\t-> Effect found in registry [" + effect.getClass ().getSimpleName () + "]");
			effect.apply (activity, application, page, effects.get (effectId), actionInstance.initiator (), actionInstance.dataHolder ());
		}
	}

}
