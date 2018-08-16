package com.coldstoragecoins.network;

import android.app.Activity;
import android.util.Log;

import com.coldstoragecoins.models.DefCurrency;
import com.coldstoragecoins.models.UserInfo;
import com.coldstoragecoins.models.Wallet;
import com.coldstoragecoins.utils.Util;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import okhttp3.Headers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This class is used as
 *
 * @author DreamWorksSoftwares
 * @version 1.0
 * @since 21 Jun,2018
 */
public class ApiHelper {
    private static ApiHelper helper;
    private static ApiInterface apiService;
    private ApiInterface apiService1 = ApiClient.getClientCurrency().create(ApiInterface.class);

    public ApiHelper() {
    }

    public static ApiHelper getInstance(Activity mContext) {
        apiService = ApiClient.getClient(mContext).create(ApiInterface.class);
        return new ApiHelper();
    }

    public void userLogin(JsonObject body, final ApiCallback.LoginListener callback) {
        Call<ResponseBody> responseBodyCall = apiService.login(body);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String body = response.body().string();
                        Headers headers = response.headers();
                        JSONObject jsonObject = new JSONObject(body);
                        int code = jsonObject.getInt("err");
                        String message = jsonObject.getString("msg");
                        Log.d("Login Response :", body);
                        if (code == 0) {
                            String auth = headers.get("Authorization");
                            JSONObject jsonObj = jsonObject.getJSONObject("profile");
                            UserInfo userInfo = new Gson().fromJson(jsonObj.toString(), UserInfo.class);
                            List<Wallet> wallets = new ArrayList<>();
                            JSONArray jsonArray = jsonObject.getJSONArray("coins");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                String walletId = jsonObject1.getString("wallet_id");
                                String sku = jsonObject1.getString("sku");
                                String barcode = jsonObject1.getString("barcode");
                                String currency = jsonObject1.getString("currency");
                                String metal = jsonObject1.getString("metal");
                                String img = jsonObject1.getString("img");
                                Wallet wallet = new Wallet();
                                wallet.setImg(img);
                                wallet.setUserId(userInfo.getId() + "");
                                wallet.setIsLocal("false");
                                wallet.setAddress(walletId);
                                wallets.add(wallet);
                            }
                            userInfo.setSid(auth);
                            userInfo.setWallets(wallets);
                            callback.onSuccess(userInfo);
                        } else {
                            Log.d("", "");
                            callback.onFailure(message);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        String body = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(body);
                        String message = jsonObject.getString("msg");
                        callback.onFailure(message);
                        Log.d("", "");
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onFailure(e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                callback.onFailure(t.getMessage());
                Log.d("", "");
            }
        });

    }

    public void userRegister(JsonObject body, final ApiCallback.Listener callback) {
        Call<ResponseBody> responseBodyCall = apiService.signup(body);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String body = response.body().string();
                        JSONObject jsonObject = new JSONObject(body);
                        int code = jsonObject.getInt("err");
                        String message = jsonObject.getString("msg");
                        Log.d("Login Response :", body);
                        if (code == 0) {
                            callback.onSuccess(message);
                        } else {
                            Log.d("", "");
                            callback.onFailure(message);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        String body = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(body);
                        String message = jsonObject.getString("msg");
                        callback.onFailure(message);
                        Log.d("", "");
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onFailure(e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                callback.onFailure(t.getMessage());
                Log.d("", "");
            }
        });

    }

    public void getUserInfo(String sid, final ApiCallback.Listener callback) {
        Call<ResponseBody> responseBodyCall = apiService.userInfo(sid);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String body = response.body().string();
                        JSONObject jsonObject = new JSONObject(body);
                        Headers headers = response.headers();
                        int code = jsonObject.getInt("err");
                        String message = jsonObject.getString("msg");
                        Log.d("Login Response :", body);
                        if (code == 0) {
                            JSONObject jsonObj = jsonObject.getJSONObject("profile");
                            UserInfo userInfo = new Gson().fromJson(jsonObj.toString(), UserInfo.class);
                            callback.onSuccess(null);
                        } else {
                            Log.d("", "");
                            callback.onFailure(message);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        String body = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(body);
                        String message = jsonObject.getString("msg");
                        callback.onFailure(message);
                        Log.d("", "");
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onFailure(e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                callback.onFailure(t.getMessage());
                Log.d("", "");
            }
        });

    }

    public void passwordForgot(JsonObject body, final ApiCallback.Listener callback) {
        Call<ResponseBody> responseBodyCall = apiService.forgot(body);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String body = response.body().string();
                        JSONObject jsonObject = new JSONObject(body);
                        int code = jsonObject.getInt("err");
                        String message = jsonObject.getString("msg");
                        Log.d("Login Response :", body);
                        if (code == 0) {
                            JSONObject jsonObj = jsonObject.getJSONObject("profile");
                            callback.onSuccess("");
                        } else {
                            Log.d("", "");
                            callback.onFailure(message);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        String body = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(body);
                        String message = jsonObject.getString("msg");
                        callback.onFailure(message);
                        Log.d("", "");
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onFailure(e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                callback.onFailure(t.getMessage());
                Log.d("", "");
            }
        });

    }

    public void getCurrencyDetails(final ApiCallback.CurrencyListener callback) {
        Call<ResponseBody> responseBodyCall = apiService1.currency();
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String body = response.body().string();
                        List<DefCurrency> currencies = new ArrayList<>();
                        JSONObject object = new JSONObject(body);
                        Iterator<String> keys = object.keys();
                        while (keys.hasNext()) {
                            String key = (String) keys.next();
                            String value = object.getString(key);
                            DefCurrency defCurrency = new DefCurrency(key, value);
                            currencies.add(defCurrency);
                        }
                        callback.onSuccess(currencies);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        String body = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(body);
                        String message = jsonObject.getString("msg");
                        callback.onFailure(message);
                        Log.d("", "");
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onFailure(e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                callback.onFailure(t.getMessage());
                Log.d("", "");
            }
        });

    }

    public void checkBalance(String sid, JsonObject body, final ApiCallback.WalletListener callback) {
        Call<ResponseBody> responseBodyCall;
        if (sid.equalsIgnoreCase("")) {
            responseBodyCall = apiService.balance(body);
        } else {
            responseBodyCall = apiService.balanceLogin(sid, body);
        }
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        List<Wallet> coinLis = new ArrayList<>();
                        String body = response.body().string();
                        JSONObject object = new JSONObject(body);
                        int err = object.getInt("err");
                        String msg = object.getString("msg");
                        if (err == 0) {
                            JSONArray jsonArray = object.getJSONArray("coins");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object1 = jsonArray.getJSONObject(i);
                                String walletId = object1.getString("wallet_id");
                                double balance = object1.getDouble("balance");
                                double divider = object1.getDouble("divider");
                                String in_fiat = object1.getString("in_fiat");
                                // double bal = Double.parseDouble(balance);
                                // double div = Double.parseDouble(divider);
                                double db = balance / divider;
                                double cc = Util.roundAvoid(db, 8);
                                String text = Double.toString(Math.abs(cc));
                                int integerPlaces = text.indexOf('.');
                                int decimalPlaces = text.length() - integerPlaces - 1;
                                String out = text;
                                for (int j = 0; j < (8 - decimalPlaces); j++) {
                                    out = out + "0";
                                }

                                if (in_fiat.equalsIgnoreCase("") || in_fiat.equalsIgnoreCase("false")) {
                                    in_fiat = "0.00";
                                } else {
                                    in_fiat = Util.roundAvoid(Double.parseDouble(in_fiat), 2) + "";
                                }
                                String cryptocurrency = object1.getString("cryptocurrency");
                                Wallet wallet = new Wallet(walletId, out + "", cryptocurrency, "", in_fiat, "", "", "");
                                coinLis.add(wallet);
                            }
                            callback.onSuccess(coinLis);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        String body = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(body);
                        String message = jsonObject.getString("msg");
                        callback.onFailure(message);
                        Log.d("", "");
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onFailure(e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                callback.onFailure(t.getMessage());
                Log.d("", "");
            }
        });

    }

    public void getWallet(JsonObject body, final ApiCallback.WalletListener callback) {
        Call<ResponseBody> responseBodyCall = apiService.getWallet("", body);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String body = response.body().string();
                        JSONObject jsonObject = new JSONObject(body);
                        int code = jsonObject.getInt("err");
                        String message = jsonObject.getString("msg");
                        Log.d("Login Response :", body);
                        if (code == 0) {
                            JSONArray jsonArray = jsonObject.getJSONArray("wallet_info");
                            List<Wallet> coinLis = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                String obj = object.getString("wallet");
                                Wallet wallet = new Wallet("", "", "", "", "", "", "", "");
                                coinLis.add(wallet);
                            }
                            callback.onSuccess(coinLis);
                        } else {
                            Log.d("", "");
                            callback.onFailure(message);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        String body = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(body);
                        String message = jsonObject.getString("msg");
                        callback.onFailure(message);
                        Log.d("", "");
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onFailure(e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                callback.onFailure(t.getMessage());
                Log.d("", "");
            }
        });

    }

    public void getMyWallets(String sid, JsonObject body, final ApiCallback.WalletListener callback) {
        Call<ResponseBody> responseBodyCall;
        if (sid.equalsIgnoreCase("")) {
            responseBodyCall = apiService.myWallet(body);
        } else {
            responseBodyCall = apiService.myWalletLogin(sid, body);
        }
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        List<Wallet> coinLis = new ArrayList<>();
                        String body = response.body().string();
                        JSONObject object = new JSONObject(body);
                        int err = object.getInt("err");
                        String msg = object.getString("msg");
                        if (err == 0) {
                            JSONArray jsonArray = object.getJSONArray("wallet_info");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object1 = jsonArray.getJSONObject(i);
                                String walletId = object1.getString("wallet_id");
                                String sku = object1.getString("sku");
                                String metal = object1.getString("metal");
                                String slug = object1.getString("slug");
                                String currency = object1.getString("currency");
                                String img = object1.getString("img");
                                Wallet wallet = new Wallet(walletId, "", "", "", "", slug, "", img);
                                coinLis.add(wallet);
                            }
                            callback.onSuccess(coinLis);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        String body = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(body);
                        String message = jsonObject.getString("msg");
                        callback.onFailure(message);
                        Log.d("", "");
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onFailure(e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                callback.onFailure(t.getMessage());
                Log.d("", "");
            }
        });

    }

    public void deleteUser(String sid, final ApiCallback.Listener callback) {
        Call<ResponseBody> responseBodyCall = apiService.delete(sid);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String body = response.body().string();
                        JSONObject object = new JSONObject(body);
                        int err = object.getInt("err");
                        String msg = object.getString("msg");
                        if (err == 0) {
                            callback.onSuccess(msg);
                        } else {
                            callback.onFailure(msg);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        String body = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(body);
                        String message = jsonObject.getString("msg");
                        callback.onFailure(message);
                        Log.d("", "");
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onFailure(e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                callback.onFailure(t.getMessage());
                Log.d("", "");
            }
        });

    }

    public void logoutUser(String sid, final ApiCallback.Listener callback) {
        Call<ResponseBody> responseBodyCall = apiService.logout(sid);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String body = response.body().string();
                        JSONObject object = new JSONObject(body);
                        int err = object.getInt("err");
                        String msg = object.getString("msg");
                        if (err == 0) {
                            callback.onSuccess(msg);
                        } else {
                            callback.onFailure(msg);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onFailure(e.getMessage());
                    }
                } else {
                    try {
                        String body = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(body);
                        String message = jsonObject.getString("msg");
                        callback.onFailure(message);
                        Log.d("", "");
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onFailure(e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                callback.onFailure(t.getMessage());
                Log.d("", "");
            }
        });

    }

    public void updateDetails(String sid, JsonObject body, final ApiCallback.Listener callback) {
        Call<ResponseBody> responseBodyCall = apiService.update(sid, body);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String body = response.body().string();
                        JSONObject jsonObject = new JSONObject(body);
                        int code = jsonObject.getInt("err");
                        String message = jsonObject.getString("msg");
                        String pass_changed = jsonObject.getString("pass_changed");
                        Log.d("Login Response :", body);
                        if (code == 0) {
                            callback.onSuccess(pass_changed);
                        } else {
                            Log.d("", "");
                            callback.onFailure(message);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        String body = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(body);
                        String message = jsonObject.getString("msg");
                        callback.onFailure(message);
                        Log.d("", "");
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onFailure(e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                callback.onFailure(t.getMessage());
                Log.d("", "");
            }
        });

    }

    public void addCoin(String sid, JsonObject body, final ApiCallback.Listener callback) {
        Call<ResponseBody> responseBodyCall = apiService.addCoin(sid, body);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String body = response.body().string();
                        JSONObject jsonObject = new JSONObject(body);
                        int code = jsonObject.getInt("err");
                        Log.d("Login Response :", body);
                        if (code == 0) {
                            JSONObject jsonObj = jsonObject.getJSONObject("result");
                            String wallet = jsonObj.getString("wallet");
                            callback.onSuccess(wallet);
                        } else {
                            Log.d("", "");
                            callback.onFailure("");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        String body = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(body);
                        String message = jsonObject.getString("msg");
                        callback.onFailure(message);
                        Log.d("", "");
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onFailure(e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                callback.onFailure(t.getMessage());
                Log.d("", "");
            }
        });

    }

    public void removeCoins(String sid, JsonObject body, final ApiCallback.Listener callback) {
        Call<ResponseBody> responseBodyCall = apiService.removeCoin(sid, body);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String body = response.body().string();
                        JSONObject jsonObject = new JSONObject(body);
                        int code = jsonObject.getInt("err");
                        Log.d("Login Response :", body);
                        if (code == 0) {
                            JSONObject jsonObj = jsonObject.getJSONObject("result");
                            String wallet = jsonObj.getString("wallet");
                            //  Coin userInfo = new Gson().fromJson(jsonObj.toString(), UserInfo.class);
                            callback.onSuccess(wallet);
                        } else {
                            Log.d("", "");
                            callback.onFailure("");
                        }
                    } catch (Exception e) {
                        callback.onFailure(e.getMessage());
                        e.printStackTrace();
                    }
                } else {
                    try {
                        String body = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(body);
                        String message = jsonObject.getString("msg");
                        callback.onFailure(message);
                        Log.d("", "");
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onFailure(e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                callback.onFailure(t.getMessage());
                Log.d("", "");
            }
        });

    }

}
