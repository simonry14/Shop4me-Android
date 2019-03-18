package ug.co.shop4me.model.entities;

/**
 * Created by kaelyn on 11/05/2017.
 */

public class Contact {
    public String contactName;
    public String contactDescription;
    public  int contactImage;

    public Contact(String contactName, String contactDescription, int contactImage) {
        this.contactName = contactName;
        this.contactDescription = contactDescription;
        this.contactImage = contactImage;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactDescription() {
        return contactDescription;
    }

    public void setContactDescription(String contactDescription) {
        this.contactDescription = contactDescription;
    }

    public int getContactImage() {
        return contactImage;
    }

    public void setContactImage(int contactImage) {
        this.contactImage = contactImage;
    }
}
