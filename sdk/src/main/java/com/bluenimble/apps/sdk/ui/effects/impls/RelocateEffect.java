package com.bluenimble.apps.sdk.ui.effects.impls;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.bluenimble.apps.sdk.Lang;
import com.bluenimble.apps.sdk.application.UIActivity;
import com.bluenimble.apps.sdk.controller.DataHolder;
import com.bluenimble.apps.sdk.spec.ApplicationSpec;
import com.bluenimble.apps.sdk.spec.LayerSpec;
import com.bluenimble.apps.sdk.spec.PageSpec;
import com.bluenimble.apps.sdk.ui.effects.Effect;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

public class RelocateEffect implements Effect {

	private static final long serialVersionUID = 3783743185246914342L;
	
	private static final String Id = "relocate";

	@Override
	public String id () {
		return Id;
	}

	/**
	 * 
	 * "relocate": "a, b, c > x" (bring a, b and c layers "in front of" layer x) or "relocate": "a, b, c < x" (send back under layer x)
	 * if (x == *) all layers should go under/infront the other remaining layers
	 * 
	 **/
	@Override
	public void apply (UIActivity activity, ApplicationSpec application, PageSpec page, Object spec, View origin, DataHolder dh) {
		
		if (spec == null || !(spec instanceof String)) {
			return;
		}

		String sSpec = ((String)spec).trim ();
		if (sSpec.endsWith (Lang.GREATER)) {
			sSpec = sSpec.substring (sSpec.length () -1);
		}
		if (sSpec.endsWith (Lang.LESS)) {
			sSpec = sSpec.substring (sSpec.length () -1);
		}
		
		int indexOfGt = sSpec.indexOf (Lang.GREATER);
		int indexOfLt = sSpec.indexOf (Lang.LESS);
		
		if (indexOfGt > 0 && indexOfLt > 0) {
			// TODO log
			return;
		}
		
		boolean bringToFront = !(indexOfLt > 0);
		
		// get the pivot layer
		int pivotIndex = 0;
		String pivot = null;
		if (indexOfGt > 0) {
			pivotIndex = indexOfGt;
		}
		if (indexOfLt > 0) {
			pivotIndex = indexOfLt;
		}
		pivot = sSpec.substring (pivotIndex + 1);
		
		String sLayers = sSpec;
		if (pivotIndex > 0) {
			sLayers = sSpec.substring (0, pivotIndex);
		}		
		
		if (Lang.isNullOrEmpty (sLayers)) {
			// TODO log
			return;
		}
		
		String [] layersIds = Lang.split (sLayers, Lang.COMMA, true);
		
		relocate (activity, application, page, pivot, new HashSet<String>(Arrays.asList (layersIds)), bringToFront);
		
	}
	
	private void relocate (UIActivity activity, ApplicationSpec application, PageSpec page, String pivot, Set<String> layers, boolean bringToFront) {
		LayerSpec pivotLayer = null;
		
		if (!Lang.isNullOrEmpty (pivot)) {
			if (layers.contains (pivot)) {
				return;
			}
			pivotLayer = page.layer (pivot);
		}
		
		Set<Fragment> 	toMove 	= new HashSet<Fragment> ();
		FragmentManager manager = activity.getSupportFragmentManager ();
		
		// start transaction
		FragmentTransaction transaction = manager.beginTransaction ();

		if (pivotLayer != null) {

			Fragment pivotFragment 	= manager.findFragmentByTag (pivotLayer.id ());
			if (pivotFragment == null) {
				return;
			}
			
			Set<Fragment> 	upper 		= new HashSet<Fragment> ();
			Set<Fragment> 	lower 		= new HashSet<Fragment> ();
			int 			pivotIndex	= manager.getBackStackEntryCount () + 1;

			for (int i = 0; i < manager.getBackStackEntryCount (); i++) {
				
				String lyrId = manager.getBackStackEntryAt (i).getName ();
				if (lyrId == null) {
					continue;
				}
				
				if (lyrId.equals (pivotFragment.getTag ())) {
					pivotIndex = i; 
					continue;
				}

				Fragment f = manager.findFragmentByTag (lyrId);
				if (f == null) {
					continue;
				}
				
				transaction.detach (f);

				if (!layers.contains (lyrId)) {
					if (i > pivotIndex) {
						upper.add (f);
					} else {
						lower.add (f);
					}
					continue;
				}
				
				toMove.add (f);
				
			}
			
			// detach pivot layer
			transaction.detach (pivotFragment);
			
			// add lower layers
			attach (transaction, lower);
			
			if (bringToFront) {
				// add pivot layer
				transaction.attach (pivotFragment);
				// add layers to move
				attach (transaction, toMove, true);
			} else {
				// add layers to move
				attach (transaction, toMove, true);
				// add pivot layer
				transaction.attach (pivotFragment);
			}
			
			// add upper layers
			attach (transaction, upper);
			
		} else {
			Set<Fragment> 	toRetain 	= new HashSet<Fragment> ();
			
			for (int i = 0; i < manager.getBackStackEntryCount (); i++) {
				String lyrId = manager.getBackStackEntryAt (i).getName ();
				if (lyrId == null) {
					continue;
				}
				
				Fragment f = manager.findFragmentByTag (lyrId);
				
				if (layers.contains (lyrId)) {
					toMove.add (f);
				} else {
					toRetain.add (f);
				}
				
				transaction.detach (f);
			}
			
			// add toMove layers
			attach (transaction, bringToFront ? toRetain : toMove, true);

			// add toRetain layers
			attach (transaction, bringToFront ? toMove : toRetain);

		}
		
		// commit transaction
		transaction.commit ();
		
	}
	
	private void attach (FragmentTransaction transaction, Set<Fragment> fragments) {
		attach (transaction, fragments, false);
	}
	
	private void attach (FragmentTransaction transaction, Set<Fragment> fragments, boolean makeVisible) {
		if (fragments.isEmpty ()) {
			return;
		}
		for (Fragment f : fragments) {
			transaction.attach (f);
			if (!makeVisible) {
				continue;
			}
			View v = f.getView ();
			if (v == null) {
				continue;
			}
			if (v.getVisibility () != View.VISIBLE) {
				v.setVisibility (View.VISIBLE);
			}
		}
	}

}
