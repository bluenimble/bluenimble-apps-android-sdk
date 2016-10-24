package com.bluenimble.apps.sdk.ui.effects.impls;

import java.util.HashMap;
import java.util.Map;

import com.bluenimble.apps.sdk.ui.effects.Effect;
import com.bluenimble.apps.sdk.ui.effects.EffectsRegistry;

public class DefaultEffectsRegistry implements EffectsRegistry {

	private static final long serialVersionUID = 6765233831874543564L;

	protected Map<String, Effect> effects = new HashMap<String, Effect> ();
	
	public DefaultEffectsRegistry () {
		// rendering effects
		register (new HideEffect ());
		register (new ShowEffect ());

		register (new RenderEffect ());
		register (new RelocateEffect ());

		register (new StyleEffect ());

		register (new DestroyEffect ());

		// data effects
		register (new BindEffect ());
		register (new UnbindEffect ());

		// flow effects
		register (new GoToEffect ());
		register (new OpenEffect ());

		// animation effects
		register (new AnimateEffect ());

		// mostly grid related effects
		register (new SelectEffect ());
		register (new ClearEffect ());
		register (new DeleteEffect ());

		// video effects
		register (new PlayEffect ());
		register (new ResumeEffect ());
		register (new PauseEffect ());
		register (new SeekEffect ());

		// debugging effects
		register (new EchoEffect ());
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
