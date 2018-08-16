package com.coldstoragecoins.network;

import android.app.Activity;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * This class is contains base urls
 *
 * @author Virendra
 * @version 1.0
 * @since 01JAN 2018
 */
public class ApiClient {
    public static final String BASE_URL = "https://app.coldstoragecoins.com/app/";
    public static final String CURRENCY_URL = "https://openexchangerates.org/api/";
    private static Retrofit retrofit = null, retrofit1 = null;

    public static Retrofit getClient(Activity mContext) {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL).client(SelfSigningClientBuilder.createClient(mContext))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }

    static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .readTimeout(190, TimeUnit.SECONDS)
            .writeTimeout(190, TimeUnit.SECONDS)
            .connectTimeout(190, TimeUnit.SECONDS)
            .build();

    public static Retrofit getClientCurrency() {
        if (retrofit1 == null) {
            retrofit1 = new Retrofit.Builder()
                    .baseUrl(CURRENCY_URL).client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit1;
    }
}
