package ug.co.shop4me.model;

import ug.co.shop4me.view.activities.LoyaltyPoints;

/**
 * Created by kaelyn on 27/12/2016.
 */

public class User {
    public String email;
    public String fullName;
    public String userId;
    public String createdAt;
    public String provider;
    public String favoriteStore;
    public String address;
    public String inviteCode;
    public String openCartId;
    public String phone1;
    public String phone2;
    public String loyaltyPoints;
    public String expressExpiry;
    public String token;
    public String referralCredit;


    public String getReferralCredit() {
        return referralCredit;
    }

    public void setReferralCredit(String referralCredit) {
        this.referralCredit = referralCredit;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getFavoriteStore() {
        return favoriteStore;
    }

    public void setFavoriteStore(String favoriteStore) {
        this.favoriteStore = favoriteStore;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public String getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void setLoyaltyPoints(String loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }

    public String getExpressExpiry() {
        return expressExpiry;
    }

    public void setExpressExpiry(String expressExpiry) {
        this.expressExpiry = expressExpiry;
    }


    public User(String email, String fullName, String userId, String createdAt, String inviteCode, String openCartId) {
        this.email = email;
        this.fullName = fullName;
        this.userId = userId;
        this.createdAt = createdAt;
        this.inviteCode = inviteCode;
        this.openCartId = openCartId;
    }

    public String getOpenCartId() {

        return openCartId;
    }

    public void setOpenCartId(String openCartId) {
        this.openCartId = openCartId;
    }

    public User() {
    }

    public User(String email, String fullName, String userId, String createdAt, String provider, String inviteCode, String address, String phone1,
                String phone2, String openCartId, String expressExpiry, String favoriteStore, String loyaltyPoints, String referralCredit) {
        this.email = email;
        this.fullName = fullName;
        this.userId = userId;
        this.createdAt = createdAt;
        this.provider = provider;
        this.inviteCode = inviteCode;
        this.favoriteStore = favoriteStore;
        this.address = address;
        this.phone1 = phone1;
        this.phone2 = phone2;
        this.expressExpiry =expressExpiry;
        this.loyaltyPoints = loyaltyPoints;
        this.openCartId = openCartId;
        this.referralCredit=referralCredit;
    }


    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }
}
