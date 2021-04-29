package com.dominicsilveira.oneqshoprestapi.rest_api;

import com.dominicsilveira.oneqshoprestapi.pojo_classes.Auth.Login;
import com.dominicsilveira.oneqshoprestapi.pojo_classes.Cart.CartDetails;
import com.dominicsilveira.oneqshoprestapi.pojo_classes.Cart.CartListDetails;
import com.dominicsilveira.oneqshoprestapi.pojo_classes.Invoice.InvoiceDetails;
import com.dominicsilveira.oneqshoprestapi.pojo_classes.Invoice.InvoiceListDetails;
import com.dominicsilveira.oneqshoprestapi.pojo_classes.Product.CategoriesListDetails;
import com.dominicsilveira.oneqshoprestapi.pojo_classes.Product.ProductBarCodes;
import com.dominicsilveira.oneqshoprestapi.pojo_classes.Product.ProductDetails;
import com.dominicsilveira.oneqshoprestapi.pojo_classes.Product.ProductListDetails;
import com.dominicsilveira.oneqshoprestapi.pojo_classes.Product.ProductRecommendations;
import com.dominicsilveira.oneqshoprestapi.pojo_classes.User.User;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
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

public interface RestApiMethods {
    String getImageFileRequest = "getImageFileRequest";
    @GET
    Call<ResponseBody> getImageFile(@Url String url); // no need add 'Content-Type' header, it's useless @Headers({"Content-Type: image/png"})

    String postRegisterRequest = "postRegisterRequest";
    @FormUrlEncoded
    @POST("/api/users/create/")
    Call<Login> postRegister(
            @Field("username") String username,
            @Field("first_name") String first_name,
            @Field("last_name") String last_name,
            @Field("email") String email,
            @Field("password") String password
    );

    String postLoginRequest = "postLoginRequest";
    @FormUrlEncoded
    @POST("/api/users/login/")
    Call<Login> postLogin(
            @Field("username") String username,
            @Field("password") String password
    );

    String postRequestResetPasswordRequest = "postRequestResetPasswordRequest";
    @FormUrlEncoded
    @POST("/api/users/reset-password/")
    Call<ResponseBody> postRequestResetPassword(
            @Field("email") String email
    );

    String postResetPasswordRequest = "postResetPasswordRequest";
    @FormUrlEncoded
    @POST("/api/users/reset-password/confirm/")
    Call<ResponseBody> postResetPassword(
            @Field("token") String token,
            @Field("password") String password
    );

    String isAuthenticatedRequest = "isAuthenticatedRequest";
    @GET("/api/users/is-authenticated/")
    Call<User> isAuthenticated(@Header("Authorization") String authHeader);

    String changePasswordRequest = "changePasswordRequest";
    @FormUrlEncoded
    @PUT("/api/users/change-password/")
    Call<ResponseBody> changePassword(
            @Header("Authorization") String authHeader,
            @Field("old_password") String old_password,
            @Field("new_password") String new_password
    );

    String updateUserDetailsRequest = "updateUserDetailsRequest";
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

    String postProfileImageRequest = "postProfileImageRequest";
    @Multipart
    @PATCH("/api/users/{user_id}/")
    Call<User> postProfileImage(
            @Path(value = "user_id", encoded = true) Integer user_id,
            @Header("Authorization") String authHeader,
            @Part MultipartBody.Part imageBitmap,
            @Part("picture") RequestBody picture
    );

    String getProductListDetailsRequest = "getProductListDetailsRequest";
    @GET("/api/products/product/")
    Call<ProductListDetails> getProductListDetails(
            @Header("Authorization") String authHeader,
            @QueryMap Map<String,
                    String> param
    );


    String getProductRecommendationListDetailsRequest = "getProductRecommendationListDetailsRequest";
    @GET("/api/products/recommend/visual/{product_id}/")
    Call<ProductRecommendations> getProductRecommendationListDetails(
            @Path(value = "product_id", encoded = true) Integer product_id,
            @QueryMap Map<String, String> param
    );

    String getProductDetailsRequest = "getProductDetailsRequest";
    @GET("/api/products/product/{product_id}/")
    Call<ProductDetails> getProductDetails(
            @Header("Authorization") String authHeader,
            @Path(value = "product_id", encoded = true) Integer product_id,
            @QueryMap Map<String, String> param
    );

    String getProductBarCodesRequest = "getProductBarCodesRequest";
    @GET("/api/products/barcodes/")
    Call<ProductBarCodes> getProductBarCodes(
            @QueryMap Map<String,
                    String> param
    );

    String getCategoriesListDetailsRequest = "getCategoriesListDetailsRequest";
    @GET("/api/products/category/")
    Call<CategoriesListDetails> getCategoriesListDetails(
            @QueryMap Map<String,
                    String> param
    );

    String getCartListDetailsRequest = "getCartListDetailsRequest";
    @GET("/api/products/cart/")
    Call<CartListDetails> getCartListDetails(
            @Header("Authorization") String authHeader,
            @QueryMap Map<String, String> param
    );

    String updateCartDetailsRequest = "updateCartDetailsRequest";
    @FormUrlEncoded
    @POST("/api/products/cart/")
    Call<CartDetails> updateCartDetails(
            @Header("Authorization") String authHeader,
            @Field("user_id") Integer user_id,
            @Field("product_id") Integer product_id,
            @Field("count") Integer count
    );

    String deleteCartDetailsRequest = "deleteCartDetailsRequest";
    @DELETE("/api/products/cart/{cart_id}/")
    Call<CartDetails> deleteCartDetails(
            @Header("Authorization") String authHeader,
            @Path(value = "cart_id", encoded = true) Integer cart_id
    );

    String postInvoiceDetailsRequest = "postInvoiceDetailsRequest";
    @Multipart
    @POST("/api/products/invoice/")
    Call<InvoiceListDetails> postInvoiceDetails(
            @Part ("user_id") Integer user_id,
            @Header("Authorization") String authHeader,
            @Part MultipartBody.Part uploadFile,
            @Part("pdf_file") RequestBody pdf_file
    );

    String getInvoiceListDetailsRequest = "getInvoiceListDetailsRequest";
    @GET("/api/products/invoice/")
    Call<InvoiceListDetails> getInvoiceListDetails(
            @Header("Authorization") String authHeader
    );

    String getInvoiceDetailsRequest = "getInvoiceDetailsRequest";
    @FormUrlEncoded
    @POST("/api/products/invoice/{id}")
    Call<InvoiceDetails> getInvoiceDetails(
            @Field ("id") Integer id,
            @Header("Authorization") String authHeader
    );
}
