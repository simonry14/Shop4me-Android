package ug.co.shop4me;

/**
 * Created by kaelyn on 23/04/2017.
 */

public class CategoryGridItem {

    public CategoryGridItem(String name, int attachedImage){
        name=name;
        attachedImage=attachedImage;
    }
    public CategoryGridItem(){

    }
    private String name;

    private int attachedImage;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAttachedImage() {
        return attachedImage;
    }

    public void setAttachedImage(int attachedImage) {
        this.attachedImage = attachedImage;
    }
}
