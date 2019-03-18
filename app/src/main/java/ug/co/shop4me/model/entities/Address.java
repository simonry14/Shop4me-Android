package ug.co.shop4me.model.entities;

/**
 * Created by kaelyn on 13/05/2017.
 */

public class Address {
    public String addressName;

    public Address(String addressName) {
        this.addressName = addressName;
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }
}
