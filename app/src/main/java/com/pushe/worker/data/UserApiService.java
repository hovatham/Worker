package com.pushe.worker.data;

import com.pushe.worker.data.model.LoggedInUser;
import com.pushe.worker.operations.model.Operation;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * REST API to Retrofit to get a user account
 */

public interface UserApiService {
    @Headers("Accept: application/json")
    @GET("user")
    Call<LoggedInUser> getUser(@Query("id") String id);

    @Headers("Accept: application/json")
    @GET("operation")
    Call<Operation> getOperation(@Query("barcode") String barcode);
}
