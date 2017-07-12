package com.inverce.mod.integrations.okhttp;

import com.inverce.mod.integrations.processing.Processor;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class RetrofitCallProcessor<Result> implements Processor<Call<Result>, Response<Result>> {
    private OnResponse<Result> onResponse;
    private OnFailure<Result> onFailure;

    public RetrofitCallProcessor() {
        onResponse = (call, response) -> {
            if (!response.isSuccessful()) {
                ResponseBody errorBody = response.errorBody();
                throw new IOException("Server returned error code: " + response.code() + " " + (errorBody != null ? errorBody.string() : "{empty body}"));
            }
        };
    }

    public RetrofitCallProcessor(OnFailure<Result> onFailure) {
        this();
        this.onFailure = onFailure;
    }

    public RetrofitCallProcessor(OnResponse<Result> onResponse) {
        this.onResponse = onResponse;
    }

    public RetrofitCallProcessor(OnResponse<Result> onResponse, OnFailure<Result> onFailure) {
        this.onResponse = onResponse;
        this.onFailure = onFailure;
    }

    @Override
    public Response<Result> processJob(Call<Result> call) throws Exception {
        try {
            Response<Result> response = call.execute();
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
        void onFailure(Call<Result> call, Throwable t);
    }

    public static interface OnResponse<Result> {
        void onResponse(Call<Result> call, Response<Result> response) throws Exception;
    }
}

