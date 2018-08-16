package com.coldstoragecoins.models;

import java.io.Serializable;

/**
 * This class is used as
 *
 * @author DreamWorksSoftwares
 * @version 1.0
 * @since 10 Aug,2018
 */
public class Wallet implements Serializable {
    private String address, balance, currency, isLocal, price, slug, userId, img;

    public Wallet() {
    }

    public Wallet(String address, String balance, String currency, String isLocal, String price, String slug, String userId, String img) {
        this.address = address;
        this.balance = balance;
        this.currency = currency;
        this.isLocal = isLocal;
        this.price = price;
        this.slug = slug;
        this.userId = userId;
        this.img = img;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getIsLocal() {
        return isLocal;
    }

    public void setIsLocal(String isLocal) {
        this.isLocal = isLocal;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

}
