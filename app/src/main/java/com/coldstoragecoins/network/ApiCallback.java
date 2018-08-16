package com.coldstoragecoins.network;


import com.coldstoragecoins.models.DefCurrency;
import com.coldstoragecoins.models.UserInfo;
import com.coldstoragecoins.models.Wallet;

import java.util.List;

/**
 * This class is used as
 *
 * @author DreamWorksSoftwares
 * @version 1.0
 * @since 17 Jul,2018
 */
public class ApiCallback {
    public interface Listener {
        void onSuccess(String result);

        void onFailure(String error);
    }

    public interface CurrencyListener {
        void onSuccess(List<DefCurrency> result);

        void onFailure(String error);
    }

    public interface WalletListener {
        void onSuccess(List<Wallet> result);

        void onFailure(String error);
    }

    public interface LoginListener {
        void onSuccess(UserInfo result);

        void onFailure(String error);
    }

    /*public interface AccountListener {
        void onSuccess(AccountBean account);

        void onFailure(String error);
    }

    public interface PaymentDetailsListener {
        void onSuccess(PaymentDetails details);

        void onFailure(String error);
    }

    public interface RegionsListener {
        void onSuccess(List<RegionBean> regionBeanList);

        void onFailure(String error);
    }

    public interface PaymentListener {
        void onSuccess(List<PaymentBean> regionBeanList);

        void onFailure(String error);
    }
*/
}
