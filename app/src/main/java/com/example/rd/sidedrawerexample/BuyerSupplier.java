package com.example.rd.sidedrawerexample;

import java.io.Serializable;

/**
 * Created by rd on 26-04-2017.
 */
public class BuyerSupplier implements Serializable {

    private String buyerName;

    private String buyerLocation;

    private String buyerContact;

    private String buyerWebsite;

    private String buyerMail;

    private String products;

    public BuyerSupplier() {}

    public BuyerSupplier(String buyerName,String buyerLocation, String buyerContact, String buyerWebsite, String buyerMail,String products){
        this.buyerName = buyerName;
        this.buyerLocation = buyerLocation;
        this.buyerContact = buyerContact;
        this.buyerWebsite = buyerWebsite;
        this.buyerMail = buyerMail;
        this.products = products;

    }

    public String getBuyerContact() {
        return buyerContact;
    }

    public void setBuyerContact(String buyerContact) {
        this.buyerContact = buyerContact;
    }

    public String getBuyerLocation() {
        return buyerLocation;
    }

    public void setBuyerLocation(String buyerLocation) {
        this.buyerLocation = buyerLocation;
    }

    public String getBuyerMail() {
        return buyerMail;
    }

    public void setBuyerMail(String buyerMail) {
        this.buyerMail = buyerMail;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getBuyerWebsite() {
        return buyerWebsite;
    }

    public void setBuyerWebsite(String buyerWebsite) {
        this.buyerWebsite = buyerWebsite;
    }

    public String getProducts() {
        return products;
    }

    public void setProducts(String products) {
        this.products = products;
    }

}
