package ug.co.shop4me.model.entities;

/**
 * Created by kaelyn on 11/05/2017.
 */

public class Store {
    public String storeName;
    public String storeDescription;
    public  String storeImage;

    public Store(String storeName, String storeDescription, String storeImage) {
        this.storeName = storeName;
        this.storeDescription = storeDescription;
        this.storeImage = storeImage;
    }

    public Store() {
    }

    public String getStoreImage() {
        return storeImage;
    }

    public void setStoreImage(String storeImage) {
        this.storeImage = storeImage;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        storeName = storeName;
    }

    public String getStoreDescription() {
        return storeDescription;
    }

    public void setStoreDescription(String storeDescription) {
        this.storeDescription = storeDescription;
    }
}
