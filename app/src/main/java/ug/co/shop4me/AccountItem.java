package ug.co.shop4me;

/**
 * Created by kaelyn on 1/05/2017.
 */

public class AccountItem {
    String name;
    int image;

    public AccountItem(String a, int img) {
        this.name = a;
        this.image = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
