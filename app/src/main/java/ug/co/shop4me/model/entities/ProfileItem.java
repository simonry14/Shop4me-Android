package ug.co.shop4me.model.entities;

/**
 * Created by kaelyn on 1/01/2017.
 */

public class ProfileItem {
    String title, content;

    public ProfileItem(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;

    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
