package com.bluenimble.apps.sdk.json;

public interface JsonEmitter {
	
	void onStartObject(JsonObject o, boolean root);
	void onEndObject(JsonObject o, boolean root);
	
	void onStartArray(JsonArray array);
	void onEndArray(JsonArray array);

	void onStartProperty(JsonObject p, String name, boolean isLast);
	void onEndProperty(JsonObject p, String name, boolean isLast);

	void onStartArrayValue(JsonArray array, Object value, boolean isLast);
	void onEndArrayValue(JsonArray array, Object value, boolean isLast);

	void onValue(JsonEntity p, String name, Object value);
	
	JsonEmitter
		write(String text);
	
}
