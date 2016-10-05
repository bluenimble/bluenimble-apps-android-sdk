package com.bluenimble.apps.sdk.ui.effects;

import java.io.Serializable;

public interface EffectsRegistry extends Serializable {

	Effect 	lockup(String id);
	void 	register(Effect effect);
	
}
