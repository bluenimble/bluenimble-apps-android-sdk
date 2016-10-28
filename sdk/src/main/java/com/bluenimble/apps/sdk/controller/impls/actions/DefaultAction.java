package com.bluenimble.apps.sdk.controller.impls.actions;

import java.util.Iterator;

import com.bluenimble.apps.sdk.Json;
import com.bluenimble.apps.sdk.Lang;
import com.bluenimble.apps.sdk.Spec;
import com.bluenimble.apps.sdk.application.UIActivity;
import com.bluenimble.apps.sdk.controller.Action;
import com.bluenimble.apps.sdk.controller.ActionInstance;
import com.bluenimble.apps.sdk.controller.DataHolder;
import com.bluenimble.apps.sdk.controller.impls.data.DefaultDataHolder;
import com.bluenimble.apps.sdk.json.JsonObject;
import com.bluenimble.apps.sdk.spec.ApplicationSpec;
import com.bluenimble.apps.sdk.spec.PageSpec;
import com.bluenimble.apps.sdk.ui.effects.Effect;
import com.bluenimble.apps.sdk.utils.BackendHelper;
import com.bluenimble.apps.sdk.utils.BindingHelper;
import com.bluenimble.apps.sdk.utils.EffectsHelper;

import android.location.Location;
import android.os.AsyncTask;
import android.view.View;

public class DefaultAction implements Action {

	private static final long serialVersionUID = 1572206118405797117L;
	
	private static final String Default = "default";
	
	@Override
	public String id () {
		return Default;
	}
	
	@Override
	public void execute (final ActionInstance actionInstance, final UIActivity activity) {
		
		final ApplicationSpec application = activity.getSpec ();
		
		final PageSpec 		page 		= application.renderer ().current ();
		
		// collect data even if there is no call. use-case: copy data between layers
		BindingHelper.collect (activity, page, actionInstance);

		application.logger ().debug (DefaultAction.class.getSimpleName (), "DataHolder \n" + actionInstance.dataHolder ());

		EffectsHelper.applyEffects (actionInstance, Spec.Action.OnStart, activity, page);

		final JsonObject oCall = Json.getObject (actionInstance.eventSpec (), Spec.Action.call.class.getSimpleName ());
		final String sServices = Json.getString (oCall, Spec.Action.call.Services);

		final DataHolder dataHolder = actionInstance.dataHolder ();

		if (oCall == null || oCall.isEmpty () || Lang.isNullOrEmpty (sServices)) {
			// destroy dataHolder
			// TODO: dataHolder.destroy ();
			return;
		}

		new AsyncTask<Void, Void, DataHolder> () {
			@Override
			protected DataHolder doInBackground (Void... params) {
				// make the call
				String [] services = Lang.split (sServices, Lang.COMMA, true);
				try {
					for (String s : services) {
						BackendHelper.callService (s, dataHolder, application);
					}
				} catch (Exception ex) {
					dataHolder.exception (ex);
				}
				
				return dataHolder;
			}
			
			@Override
			protected void onPostExecute (DataHolder result) {
				if (result.exception () != null) {
					// TODO log exception
				}
				// onSuccess / onFailure
				EffectsHelper.applyEffects (
					actionInstance,
					(result.exception () != null) ? Spec.Action.call.OnError : Spec.Action.call.OnSuccess,
					activity, page
				);
				// onFinish
				EffectsHelper.applyEffects (actionInstance, Spec.Action.call.OnFinish, activity, page);

				// destroy dataHolder
				// TODO: result.destroy ();
			}
		}.execute ();
		
	}
	
}
