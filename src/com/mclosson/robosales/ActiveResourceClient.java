package com.mclosson.robosales;

import org.json.JSONObject;

public class ActiveResourceClient extends RestClient {

	public JSONObject index(String id) {
		return get(id);
	}

	public JSONObject show(String id) {
		return get(id);
	}

	public JSONObject create(String id) {
		return post(id);
	}

	public JSONObject update(String id) {
		return put(id);
	}

	public JSONObject destroy(String id) {
		return delete(id);
	}

}
