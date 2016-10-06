package com.bluenimble.apps.sdk.ui.effects.impls;

import java.util.HashMap;
import java.util.Map;

import com.bluenimble.apps.sdk.ui.effects.Effect;
import com.bluenimble.apps.sdk.ui.effects.EffectsRegistry;

public class DefaultEffectsRegistry implements EffectsRegistry {

	private static final long serialVersionUID = 6765233831874543564L;

	protected Map<String, Effect> effects = new HashMap<String, Effect> ();
	
	public DefaultEffectsRegistry () {
		register (new BindEffect ());
		register (new UnbindEffect ());
		register (new GoToEffect ());
		register (new HideEffect ());
		register (new ShowEffect ());
		register (new OpenEffect ());
		register (new RelocateEffect ());
		register (new RenderEffect ());
		register (new AnimateEffect ());
	}
	
	@Override
	public Effect lockup (String id) {
		return effects.get (id.toLowerCase ());
	}

	@Override
	public void register (Effect effect) {
		effects.put (effect.id ().toLowerCase (), effect);
	}

}
