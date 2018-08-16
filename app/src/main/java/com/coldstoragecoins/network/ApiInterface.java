package com.coldstoragecoins.network;

import com.google.gson.JsonObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * This Interface is used as fetch Api
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 30Aug 2017
 */
public interface ApiInterface {
    @Headers({
            "Content-Type:application/json"
    })

    @POST("login.php")
    Call<ResponseBody> login(@Body JsonObject body);

    @POST("register.php")
    Call<ResponseBody> signup(@Body JsonObject body);

    @POST("userinfo.php")
    Call<ResponseBody> userInfo(@Query("sid") String sid);

    @POST("passwordreset.php")
    Call<ResponseBody> forgot(@Body JsonObject body);

    @POST("passwordreset.php")
    Call<ResponseBody> reset(@Query("sid") String sid, @Body JsonObject body);

    @POST("updateuser.php")
    Call<ResponseBody> update(@Query("sid") String sid, @Body JsonObject body);

    @GET("currencies.json?show_alternative")
    Call<ResponseBody> currency();

    @POST("removeuser.php")
    Call<ResponseBody> delete(@Query("sid") String sid);

    @POST("coin_list.php")
    Call<ResponseBody> getWallet(@Query("sid") String sid, @Body JsonObject body);

    @POST("add_coin.php")
    Call<ResponseBody> addCoin(@Query("sid") String sid, @Body JsonObject body);

    @POST("remove_coin.php")
    Call<ResponseBody> removeCoin(@Query("sid") String sid, @Body JsonObject body);

    @POST("logout.php")
    Call<ResponseBody> logout(@Query("sid") String sid);

    @POST("balance.php")
    Call<ResponseBody> balance(@Body JsonObject body);

    @POST("balance.php")
    Call<ResponseBody> balanceLogin(@Query("sid") String sid, @Body JsonObject body);

    @POST("coininfo.php")
    Call<ResponseBody> myWallet(@Body JsonObject body);

    @POST("coininfo.php")
    Call<ResponseBody> myWalletLogin(@Query("sid") String sid, @Body JsonObject body);


    //@GET("coins_list.php")
    ///Call<ResponseBody> currency(@Query("sid") String sid);

}
