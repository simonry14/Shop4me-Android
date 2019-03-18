/**
 * 
 */
package ug.co.shop4me.model.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * The Class Product used as model for Products.
 *
 * @author Hitesh
 */
public class Product implements Parcelable {


	/** The item short desc. */
	private String description = "";

	/** The item detail. */
	private String longDescription = "";

	/** The mrp. */
	private String mrp;

	/** The discount. */
	private String discount;

	/** The sell mrp. */
	private String salePrice;

	/** The quantity. */
	private String orderQty;

	/** The image url. */
	private String imageUrl = "";

	/** The item name. */
	private String productName = "";

	private String productId = "";

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	/**
	 * @param itemName
	 * @param itemShortDesc
	 * @param itemDetail
	 * @param MRP
	 * @param discount
	 * @param sellMRP
	 * @param orderQty
	 * @param imageURL
	 */
	public Product(String itemName, String itemShortDesc, String itemDetail,
                   String MRP, String discount, String sellMRP, String orderQty,
                   String imageURL, String productId) {
		this.productName = itemName;
		this.description = itemShortDesc;
		this.longDescription = itemDetail;
		this.mrp = MRP;
		this.discount = discount;
		this.salePrice = sellMRP;
		this.orderQty = orderQty;
		this.imageUrl = imageURL;
		this.productId = productId;
	}

	public Product(String salePrice, String orderQty, String imageUrl, String productName) {
		this.salePrice = salePrice;
		this.orderQty = orderQty;
		this.imageUrl = imageUrl;
		this.productName = productName;
	}

	public String getItemName() {
		return productName;
	}

	public void setItemName(String itemName) {
		this.productName = itemName;
	}


	public String getItemShortDesc() {
		return description;
	}

	public void setItemShortDesc(String itemShortDesc) {
		this.description = itemShortDesc;
	}

	public String getItemDetail() {
		return longDescription;
	}

	public void setItemDetail(String itemDetail) {
		this.longDescription = itemDetail;
	}

	public String getMRP() {
		return this.mrp;
	}

	public void setMRP(String MRP) {
		this.mrp = MRP;
	}

	public String getDiscount() {
		return discount + "%";
	}

	public String getDiscountNumeric() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public String getSellMRP() {
		return salePrice;
	}

	public void setSellMRP(String sellMRP) {
		this.salePrice = sellMRP;
	}

	public String getQuantity() {
		return orderQty;
	}

	public void setQuantity(String orderQty) {
		this.orderQty = orderQty;
	}

	public String getImageURL() {
		return imageUrl;
	}

	public void setImageURL(String imageURL) {
		this.imageUrl = imageURL;
	}

	@Override
	public int describeContents() {
		return 0;
	}


	@Override
	public void writeToParcel(Parcel parcel, int i) {
		parcel.writeString(productName);
		parcel.writeString(description);
		parcel.writeString(longDescription);
		parcel.writeString(mrp);
		parcel.writeString(discount);
		parcel.writeString(salePrice);
		parcel.writeString(orderQty);
		parcel.writeString(imageUrl);
		parcel.writeString(productId);
	}

	//constructor used for parcel
	public Product(Parcel parcel){
		//read and set saved values from parcel

		productName = parcel.readString();
		description = parcel.readString();
		longDescription = parcel.readString();
		mrp = parcel.readString();
		discount = parcel.readString();
		salePrice = parcel.readString();
		orderQty = parcel.readString();
		imageUrl = parcel.readString();
		productId = parcel.readString();
	}


	//creator - used when un-parceling our parcle (creating the object)
	public static final Parcelable.Creator<Product> CREATOR = new Parcelable.Creator<Product>(){

		@Override
		public Product createFromParcel(Parcel parcel) {
			return new Product(parcel);
		}

		@Override
		public Product[] newArray(int size) {
			return new Product[0];
		}
	};

    @Override
    public boolean equals (Object object)
    {
		boolean sameSame = false;
        if(object != null && object instanceof Product)
        {
			sameSame = this.productName.equals(((Product) object).productName);
        }
		return sameSame;
    }

	@Override
	public int hashCode() {
		int result = 17;
		result = 31 * result + productName.hashCode();
		return result;
	}
}
