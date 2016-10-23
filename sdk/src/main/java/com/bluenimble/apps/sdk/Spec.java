package com.bluenimble.apps.sdk;

public interface Spec {
	
	String Name 			= "name";
	String Description 		= "description";
	String DefaultLanguage 	= "defaultLanguage";
	String Theme 			= "theme";
	
	String Main 			= "main";
	
	String LogLevel 		= "logLevel";
	
	interface sdkVersion {
	
		String Major = "major";
		String Minor = "minor";
		String Patch = "patch";
	
	}
	
	String Pages		= "pages";
	
	String Events 		= "events";

	interface page {
		
		String Name 	= "name";
		String Theme 	= "theme";
		String Layers 	= "layers";

		interface event {
			String Scope 	= "scope";
			String Action 	= "action";
			String Data 	= "data";
		}

		interface layer {
			
			String Type 		= "type";
			String Name 		= "name";
			String Components 	= "components";
			String Spec 		= "spec";
			
			String Global		= "global";
			String Render		= "render";

			interface component {
				String Id 			= "id";
				String Type 		= "type";
				String Style 		= "style";
				String Break 		= "break";
				String Custom 		= "custom";
				interface binding {
					String Set 		= "set";
					String Get 		= "get";
					String Source	= "source";
					String Property	= "property";
				}
			}
			
		}

	}
	
	interface Action {
		String Scope	= "scope";
		String OnStart 	= "onStart";
		String OnEnd 	= "onEnd";
		interface call {
			String Services 	= "services";
			String OnSuccess 	= "onSuccess";
			String OnError 		= "onError";
			String OnFinish 	= "onFinish";
		}
	}
	
	interface Service {
		String Type		= "type";
		String Visitor	= "visitor";
	}
	
}

