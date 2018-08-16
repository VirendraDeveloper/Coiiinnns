package com.coldstoragecoins.models;

import java.util.List;

/**
 * This class is used as
 *
 * @author DreamWorksSoftwares
 * @version 1.0
 * @since 06 Aug,2018
 */
public class UserInfo {

    /**
     * id : 216
     * email : testuser@gmail.com
     * default_currency : USD
     * created_at : 2018-08-03 16:45:23
     * updated_at : 2018-08-03 16:45:23
     * firstname : unimplemented
     * lastname : unimplemented
     */

    private int id;
    private String email;
    private String sid;
    private String default_currency;
    private String created_at;
    private String updated_at;
    private String firstname;
    private String lastname;
    private List<Wallet> wallets;

    public List<Wallet> getWallets() {
        return wallets;
    }

    public void setWallets(List<Wallet> wallets) {
        this.wallets = wallets;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDefault_currency() {
        return default_currency;
    }

    public void setDefault_currency(String default_currency) {
        this.default_currency = default_currency;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
}
