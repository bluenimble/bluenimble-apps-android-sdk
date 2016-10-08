package com.bluenimble.apps.sdk.ui.components.impls.list;

import com.bluenimble.apps.sdk.Lang;
import com.bluenimble.apps.sdk.application.UIActivity;
import com.bluenimble.apps.sdk.controller.DataHolder;
import com.bluenimble.apps.sdk.controller.impls.DefaultDataHolder;
import com.bluenimble.apps.sdk.json.JsonArray;
import com.bluenimble.apps.sdk.json.JsonObject;
import com.bluenimble.apps.sdk.spec.ApplicationSpec;
import com.bluenimble.apps.sdk.spec.ComponentSpec;
import com.bluenimble.apps.sdk.spec.LayerSpec;
import com.bluenimble.apps.sdk.spec.PageSpec;
import com.bluenimble.apps.sdk.ui.components.impls.dropdown.DropDownFactory.Custom;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public class DefaultListAdapter extends RecyclerView.Adapter<DefaultListAdapter.ViewHolder> {

	protected ApplicationSpec 	application;
	protected LayerSpec 		layer;
	protected PageSpec 			page;
	protected JsonArray 		records;
	protected DataHolder 		one;
	protected Context 			context;
	
	public DefaultListAdapter (Context context, ApplicationSpec application, ComponentSpec component, JsonArray records) {
		this.context 		= context;
		this.records		= records;
		this.one 			= new DefaultDataHolder ();
		
		this.application 	= application;
		this.page 			= application.renderer ().current ();
		
		String template 	= (String) component.get (Custom.Template);
		String layerId		= template;
		
		int indexOfDot		= template.indexOf (Lang.DOT);
		if (indexOfDot > 0) {
			page 			= application.page (template.substring (0, indexOfDot));
			layerId			= template.substring (indexOfDot + 1);
		} 
		if (page == null) {
			return;
		}
		
		this.layer 			= page.layer (layerId);
	}
	
	@Override
	public int getItemCount () {
		if (records == null) {
			return 0;
		}
		return records.count ();
	}
	
	public int getItemViewType (int position) {
		JsonObject record = (JsonObject) records.get (position);
		if (record.containsKey ("header")) {
			return 0;
		}
		return 1;
	}

	@Override
	public long getItemId (int position) {
		return position;
	}

	@Override
	public ViewHolder onCreateViewHolder (ViewGroup group, int itemViewType) {
		// Normally we should do: if (viewType == 0) { view = Inflator.inflate (headerView);} else { // inflate child view }
		return new ViewHolder (application.renderer ().render (application, layer, null /* DataHolder in here */, group, (UIActivity)context));
	}



	@Override
	public void onBindViewHolder (ViewHolder viewHolder, int position) {
		JsonObject o = (JsonObject) records.get (position);
		//viewHolder.title.setText (o.getString ("title"));
	}

	public class ViewHolder extends RecyclerView.ViewHolder {
        //public TextView title, genre;
 
        public ViewHolder (View view) {
            super (view);
            //title 	= (TextView) view.findViewById (R.id.title);
            //genre 	= (TextView) view.findViewById (R.id.genre);
        }
    }
}