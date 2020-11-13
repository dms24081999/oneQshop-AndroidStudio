package com.dominicsilveira.one_q_shop.utils.api;

import com.dominicsilveira.one_q_shop.jsonschema2pojo_classes.Auth.Login;
import com.dominicsilveira.one_q_shop.jsonschema2pojo_classes.Product.CategoriesListDetails;
import com.dominicsilveira.one_q_shop.jsonschema2pojo_classes.Product.ProductListDetails;
import com.dominicsilveira.one_q_shop.jsonschema2pojo_classes.User.User;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
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
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface RestMethods {
    @GET
    Call<ResponseBody> getImageFile(@Url String url); // don't need add 'Content-Type' header, it's useless @Headers({"Content-Type: image/png"})

    @FormUrlEncoded
    @POST("/api/users/create/")
    Call<Login> postRegister(
            @Field("username") String username,
            @Field("first_name") String first_name,
            @Field("last_name") String last_name,
            @Field("email") String email,
            @Field("password") String password
    );


    @FormUrlEncoded
    @POST("/api/users/login/")
    Call<Login> postLogin(
            @Field("username") String username,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("/api/users/reset-password/")
    Call<ResponseBody> postRequestResetPassword(
            @Field("email") String email
    );

    @FormUrlEncoded
    @POST("/api/users/reset-password/confirm/")
    Call<ResponseBody> postResetPassword(
            @Field("token") String token,
            @Field("password") String password
    );


    @GET("/api/users/is-authenticated/")
    Call<User> isAuthenticated(@Header("Authorization") String authHeader);

    @FormUrlEncoded
    @PUT("/api/users/change-password/")
    Call<ResponseBody> changePassword(
            @Header("Authorization") String authHeader,
            @Field("old_password") String old_password,
            @Field("new_password") String new_password
    );

    @FormUrlEncoded
    @PATCH("/api/users/{user_id}/")
    Call<User> updateUserDetails(
            @Path(value = "user_id", encoded = true) Integer user_id,
            @Header("Authorization") String authHeader,
            @Field("first_name") String first_name,
            @Field("last_name") String last_name,
            @Field("email") String email,
            @Field("phone_number") String phone_number
    );

    @Multipart
    @PATCH("/api/users/{user_id}/")
    Call<User> postProfileImage(
            @Path(value = "user_id", encoded = true) Integer user_id,
            @Header("Authorization") String authHeader,
            @Part MultipartBody.Part imageBitmap,
            @Part("picture") RequestBody picture
    );

    @GET("/api/products/product/")
    Call<ProductListDetails> getProductListDetails(@QueryMap Map<String, String> param);
//    @Header("Authorization") String authHeader

    @GET("/api/products/category/")
    Call<CategoriesListDetails> getCategoriesListDetails(@QueryMap Map<String, String> param);
}

