package com.dominicsilveira.one_q_shop.utils.api;

import com.dominicsilveira.one_q_shop.classes.Users;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RestMethods {
    @FormUrlEncoded
    @POST("api/users/create/")
    Call<ResponseBody> postRegister(
            @Field("username") String username,
            @Field("first_name") String first_name,
            @Field("last_name") String last_name,
            @Field("email") String email,
            @Field("password") String password
    );


    @FormUrlEncoded
    @POST("api/token-auth/")
    Call<ResponseBody> postLogin(
            @Field("username") String username,
            @Field("password") String password
    );


    @GET("api/users/is-authenticated/")
    Call<Users> isAuthenticated(@Header("Authorization") String authHeader);

    @FormUrlEncoded
    @PUT("api/users/change-password/")
    Call<ResponseBody> changePassword(
            @Header("Authorization") String authHeader,
            @Field("old_password") String old_password,
            @Field("new_password") String new_password
    );

    @FormUrlEncoded
    @PATCH("api/users/{user_id}/")
    Call<Users> updateUserDetails(
            @Path(value = "user_id", encoded = true) Integer user_id,
            @Header("Authorization") String authHeader,
            @Field("first_name") String first_name,
            @Field("last_name") String last_name,
            @Field("email") String email,
            @Field("phone_number") String phone_number
    );
}

