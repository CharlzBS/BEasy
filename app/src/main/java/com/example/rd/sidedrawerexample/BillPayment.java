package com.example.rd.sidedrawerexample;

import java.io.Serializable;

/**
 * Created by rd on 11-02-2017.
 */
public class BillPayment implements Serializable {

    private int sno;
    private String date;
    private String supplierName;
    private String buyerName;
    private String billNumber;
    private int amount;
    private String photoUrl;

    public BillPayment() {
    }


    public BillPayment(int sno, String date,String supplierName, String buyerName, String billNumber, int amount){
        this.sno = sno;
        this.date = date;
        this.supplierName = supplierName;
        this.buyerName = buyerName;
        this.billNumber = billNumber;
        this.amount = amount;


    }

    public BillPayment(int sno, String date,String supplierName, String buyerName, String billNumber, int amount,String photoUrl){
        this.sno = sno;
        this.date = date;
        this.supplierName = supplierName;
        this.buyerName = buyerName;
        this.billNumber = billNumber;
        this.amount = amount;
        this.photoUrl = photoUrl;

    }



    public int getSno() {
        return sno;
    }

    public void setSno(int sno) {
        this.sno = sno;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName){
        this.supplierName = supplierName;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getBillNumber() {
        return billNumber;
    }
    public void setBillNumber(String billNumber){
        this.billNumber = billNumber;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
