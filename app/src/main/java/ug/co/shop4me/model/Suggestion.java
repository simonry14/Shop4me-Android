package ug.co.shop4me.model;

/**
 * Created by kaelyn on 11/05/2017.
 */

public class Suggestion {
    public String productName;
    public String fullName;

    public Suggestion(){

    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Suggestion(String productName, String fullName) {
        this.productName = productName;
        this.fullName = fullName;
    }
}
