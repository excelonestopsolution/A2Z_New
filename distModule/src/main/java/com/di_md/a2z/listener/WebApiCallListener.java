package com.di_md.a2z.listener;

import org.json.JSONObject;

public interface WebApiCallListener {
    void onSuccessResponse(JSONObject jsonObject);
    void onFailure(String message);
}

