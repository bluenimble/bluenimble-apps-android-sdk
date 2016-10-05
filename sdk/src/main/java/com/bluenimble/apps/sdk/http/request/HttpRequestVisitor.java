package com.bluenimble.apps.sdk.http.request;

import java.io.Serializable;
import java.net.HttpURLConnection;

public interface HttpRequestVisitor extends Serializable {

	void visit(HttpRequest request, HttpURLConnection connection) throws HttpRequestWriteException;

}
