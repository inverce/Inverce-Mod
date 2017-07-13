package com.inverce.mod.integrations.okhttp;

import com.inverce.mod.processing.Processor;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class OkHttpCallProcessor<Result> implements Processor<Call, Response> {
    private OnResponse<Result> onResponse;
    private OnFailure<Result> onFailure;

    public OkHttpCallProcessor() {
        onResponse = (call, response) -> {
            if (!response.isSuccessful()) {
                ResponseBody errorBody = response.body();
                throw new IOException("Server returned error code: " + response.code() + " " + (errorBody != null ? errorBody.string() : "{empty body}"));
            }
        };
    }

    public OkHttpCallProcessor(OnFailure<Result> onFailure) {
        this();
        this.onFailure = onFailure;
    }

    public OkHttpCallProcessor(OnResponse<Result> onResponse) {
        this.onResponse = onResponse;
    }

    public OkHttpCallProcessor(OnResponse<Result> onResponse, OnFailure<Result> onFailure) {
        this.onResponse = onResponse;
        this.onFailure = onFailure;
    }

    @Override
    public Response processJob(Call call) throws Exception {
        try {
            Response response = call.execute();
            if (onResponse != null) {
                onResponse.onResponse(call, response);
            }
            return response;
        } catch (InterruptedException ex) {
            throw ex;
        } catch (Exception ex) {
            if (onFailure != null) {
                onFailure.onFailure(call, ex);
            }
            throw ex;
        }
    }

    public static interface OnFailure<Result> {
        void onFailure(Call call, Throwable t);
    }

    public static interface OnResponse<Result> {
        void onResponse(Call call, Response response) throws Exception;
    }
}
