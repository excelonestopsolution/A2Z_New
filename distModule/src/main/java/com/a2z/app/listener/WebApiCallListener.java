package com.a2z.app.listener;

import org.json.JSONObject;

public interface WebApiCallListener {
    void onSuccessResponse(JSONObject jsonObject);
    void onFailure(String message);
}

