package com.you9.gamesdk.request;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.tsy.sdk.myokhttp.MyOkHttp;
import com.tsy.sdk.myokhttp.response.JsonResponseHandler;
import com.tsy.sdk.myokhttp.response.RawResponseHandler;
import com.you9.gamesdk.bean.JyResponse;
import com.you9.gamesdk.listener.JyBaseRequestListener;
import com.you9.gamesdk.util.JyConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class JyBaseRequest {

    /**
     * 发送post请求
     *
     */

    protected  void get(Context context, HashMap<String, String> params, String requestUrl, final String portName, int returnDataFormat, final JyBaseRequestListener listener){
        Log.d("eeeee", "getRequestParams=" + params.toString());
        if (returnDataFormat == JyConstants.RETURN_DATA_FORMAT_JSON){
            final JyResponse resp = new JyResponse();
            new MyOkHttp().get().url(requestUrl + portName).params(params).tag(context).enqueue(new JsonResponseHandler() {

                @Override
                public void onSuccess(int statusCode, JSONArray response) {
                    super.onSuccess(statusCode, response);
                    Log.d("eeeee", "getJSONArrayresponse=" + response);
                }

                @Override
                public void onSuccess(int statusCode, JSONObject response) {
                    super.onSuccess(statusCode, response);
                    Log.d("eeeee", "getJSONObjectresponse=" + response);
                }

                @Override
                public void onFailure(int statusCode, String error_msg) {

                }
            });
        }
    }

    protected void post(Context context, HashMap<String, String> params, String requestUrl, final String portName, int returnDataFormat, final JyBaseRequestListener listener) {
        Log.d("eeeee", "postRequestParams=" + params.toString());


        if (returnDataFormat == JyConstants.RETURN_DATA_FORMAT_RAW){
            final JyResponse resp = new JyResponse();
            new MyOkHttp().post().url(requestUrl + portName).params(params).tag(context).enqueue(new RawResponseHandler() {
                @Override
                public void onSuccess(int statusCode, String response) {
                    Log.d("eeeee", "response=" + response);
                    resp.setDesc(response);
                    listener.onBaseRequestSuccess(resp);

                }

                @Override
                public void onFailure(int statusCode, String error_msg) {
                    Log.d("eeeee", "error_msg=" + error_msg);
//                    resp.setDesc(error_msg);
                    listener.onBaseRequestFailed(error_msg);

                }
            });
        }

        if (returnDataFormat == JyConstants.RETURN_DATA_FORMAT_JSON){
            new MyOkHttp().post().url(requestUrl + portName).params(params).tag(context).enqueue(new JsonResponseHandler() {

                @Override
                public void onSuccess(int statusCode, JSONObject response) {
                    super.onSuccess(statusCode, response);
                    Log.d("eeeee", "responseJSONObject=" + response);

                    JyResponse resp = null;

                    if (!portName.equals("getPayType") && !portName.equals("queryOrder") && !portName.equals("queryOneOrder")){
                        resp = JSON.parseObject(response.toString(), JyResponse.class);
                    }else{
                        com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(response.toString());
                        resp = new JyResponse();
                        resp.setState(jsonObject.getInteger("ret") + "");
                        if (portName.equals("getPayType")){
                            resp.setDesc(jsonObject.getString("records"));
                        }else if (portName.equals("queryOrder")){
                            resp.setDesc(jsonObject.getString("orders"));
                        }

                    }

                    listener.onBaseRequestSuccess(resp);

                }

                @Override
                public void onSuccess(int statusCode, JSONArray response) {
                    Log.d("eeeee", "responseJSONArray=" + response);
                    super.onSuccess(statusCode, response);

                    JyResponse resp = new JyResponse();
                    try {
                        resp.setState(response.getInt(0) + "");
                        resp.setDesc(response.getString(1));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    listener.onBaseRequestSuccess(resp);


                }

                @Override
                public void onFailure(int statusCode, String error_msg) {
                    Log.d("eeeee", "error_msg=" + error_msg);
                    JyResponse resp = new JyResponse();
//                    resp.setDesc(error_msg);
                    listener.onBaseRequestFailed(error_msg);


                }
            });

        }


    }



}
