package ug.co.shop4me.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import ug.co.shop4me.view.activities.MainActivity;
import ug.co.shop4me.R;
import ug.co.shop4me.model.entities.Product;
import ug.co.shop4me.util.ColorGenerator;
import ug.co.shop4me.view.activities.ProductDetailsActivity;
import ug.co.shop4me.view.activities.ProductListActivity;
import ug.co.shop4me.view.activities.SearchActivity;
import ug.co.shop4me.view.customview.TextDrawable;
import ug.co.shop4me.view.customview.TextDrawable.IBuilder;

import java.util.ArrayList;
import java.util.Collections;

public class ProductListAdapter extends
		RecyclerView.Adapter<ProductListAdapter.VersionViewHolder> implements
		ItemTouchHelperAdapter, Filterable {

	private ColorGenerator mColorGenerator = ColorGenerator.MATERIAL;

	private IBuilder mDrawableBuilder;

	private TextDrawable drawable;

	private String ImageUrl;

	private ArrayList<Product> productList = new ArrayList<Product>();
	private ArrayList<Product> filteredProductList = new ArrayList<Product>();
	private OnItemClickListener clickListener;

	private Context context;

	private ProductFilter productFilter;


	public ProductListAdapter(Context context, ArrayList<Product> prodList) {
		this.context = context;
		this.productList = prodList;
		this.filteredProductList = prodList;


	}

	@Override
	public VersionViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
		View view = LayoutInflater.from(viewGroup.getContext()).inflate(
				R.layout.item_product_list, viewGroup, false);
		VersionViewHolder viewHolder = new VersionViewHolder(view);
		return viewHolder;
	}

	@Override
	public void onBindViewHolder(final VersionViewHolder holder,
			final int position) {

		holder.itemName.setText(filteredProductList.get(position).getItemName());

		holder.itemDesc.setText(filteredProductList.get(position).getItemShortDesc());

		Long sellCostString = Long.valueOf(filteredProductList.get(position).getSellMRP().split("[.]")[0]);



		String costString = MainActivity.formatter.format(sellCostString)+"/=" ;//+ buyMRP;

		holder.itemCost.setText(costString);

		if(MainActivity.shoppingCartList.contains(filteredProductList.get(position))){
			int d = MainActivity.shoppingCartList.indexOf(filteredProductList.get(position));
			holder.quantity.setText(MainActivity.shoppingCartList.get(d).getQuantity()) ;// from cart
		}


		mDrawableBuilder = TextDrawable.builder().beginConfig().withBorder(4).endConfig().roundRect(10);

		drawable = mDrawableBuilder.build(String.valueOf(filteredProductList.get(position).getItemName().charAt(0)), mColorGenerator.getColor(filteredProductList.get(position).getItemName()));

		ImageUrl = filteredProductList.get(position).getImageURL();

		Glide.with(context).load(ImageUrl).placeholder(drawable).error(drawable).animate(R.anim.fade_in).centerCrop().into(holder.imagView);

		holder.addItem.findViewById(R.id.add_item).setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						
						//current object
						Product tempObj = filteredProductList.get(position);

						//if current object is lready in shopping list
                       // if (GlobaDataHolder.getGlobaDataHolder().getShoppingList().contains(tempObj)) {
						if (MainActivity.shoppingCartList.contains(tempObj)) {



                                //get position of current item in shopping list
                               // int indexOfTempInShopingList = GlobaDataHolder.getGlobaDataHolder().getShoppingList().indexOf(tempObj);
                                int indexOfTempInShopingList = MainActivity.shoppingCartList.indexOf(tempObj);


							tempObj = MainActivity.shoppingCartList.get(indexOfTempInShopingList); // use this as the object since it has a quantity. original one has empty quantity


                                // increase quantity of current item in shopping list
                                if (Integer.parseInt(tempObj.getQuantity()) == 0) {
                                    //ProductListActivity.updateItemCount(true);
									try{ProductListActivity.updateAlertIcon(true);}catch (NullPointerException e){SearchActivity.updateAlertIcon(true);}
                                }

                                // update quanity in shopping list
                               // GlobaDataHolder.getGlobaDataHolder().getShoppingList().get(indexOfTempInShopingList).setQuantity(String.valueOf(Integer.valueOf(tempObj.getQuantity()) + 1));

                                MainActivity.shoppingCartList.get(indexOfTempInShopingList).setQuantity
                                        (String.valueOf(Integer.valueOf(MainActivity.shoppingCartList.get(indexOfTempInShopingList).getQuantity()) + 1));



                                // update current item quanitity
                                holder.quantity.setText( MainActivity.shoppingCartList.get(indexOfTempInShopingList).getQuantity());

							//Save cart
							MainActivity.saveCart();
							try{ProductListActivity.updateAlertIcon(true);}catch (NullPointerException e){SearchActivity.updateAlertIcon(true);}

                            } else {

                                //ProductListActivity.updateItemCount(true);


                                tempObj.setQuantity(String.valueOf(1));

                                holder.quantity.setText(tempObj.getQuantity());

                                //GlobaDataHolder.getGlobaDataHolder().getShoppingList().add(tempObj);
                                MainActivity.shoppingCartList.add(tempObj);

                                //MainActivity.updateCheckOutAmount(BigDecimal.valueOf(Long.valueOf(productList.get(position).getSellMRP())), true);
MainActivity.saveCart();
							try{ProductListActivity.updateAlertIcon(true);}catch (NullPointerException e){SearchActivity.updateAlertIcon(true);}
                            }
                        }

					//	Utils.vibrate(get);


				});

		holder.removeItem.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Product tempObj = (filteredProductList).get(position);
                //if (GlobaDataHolder.getGlobaDataHolder().getShoppingList().contains(tempObj)) { //if its in cart/ shoppinglist

				if (MainActivity.shoppingCartList.contains(tempObj)) { //if its in cart/ shoppinglist

				int f =	MainActivity.shoppingCartList.indexOf(tempObj); // get index of temp ob in cart
					 tempObj = MainActivity.shoppingCartList.get(f); // use this as the object since it has a quantity. original one has empty quantity

					//int indexOfTempInShopingList = GlobaDataHolder.getGlobaDataHolder().getShoppingList().indexOf(tempObj); //get its index
                    int indexOfTempInShopingList = MainActivity.shoppingCartList.indexOf(tempObj); //get its index

					if (Integer.valueOf(tempObj.getQuantity()) != 0) { //if its quantity in the list aint zero

						//GlobaDataHolder.getGlobaDataHolder().getShoppingList().get(indexOfTempInShopingList).setQuantity(String.valueOf(Integer.valueOf(tempObj.getQuantity()) - 1));
                        MainActivity.shoppingCartList.get(indexOfTempInShopingList).setQuantity(String.valueOf(Integer.valueOf(tempObj.getQuantity()) - 1));

						//MainActivity.updateCheckOutAmount(BigDecimal.valueOf(Long.valueOf(productList.get(position).getSellMRP().split("[.]")[0])), false);
//update the number in view
						holder.quantity.setText(MainActivity.shoppingCartList.get(indexOfTempInShopingList).getQuantity());

						//Utils.vibrate(getContext());
						String thisss = MainActivity.shoppingCartList.get(indexOfTempInShopingList).getQuantity();


						if(MainActivity.shoppingCartList.get(indexOfTempInShopingList).getQuantity().equals("0")){//if quantity is zero
							//remove this item from the shopping cart list
							MainActivity.shoppingCartList.remove(indexOfTempInShopingList);
							try{ProductListActivity.updateAlertIcon(false);}catch (NullPointerException e){SearchActivity.updateAlertIcon(false);}
						}

						MainActivity.saveCart();
					}

				} else {

				}

			}

		});

        holder.itemView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Product pd = filteredProductList.get(position);
                Intent pIntent = new Intent(context, ProductDetailsActivity.class);
                pIntent.setAction("FROM PD LIST ADAPTER");
                pIntent.putExtra("pName", pd.getItemName());
                pIntent.putExtra("pDesc", pd.getItemShortDesc());
                pIntent.putExtra("pPrice", pd.getSellMRP());
                pIntent.putExtra("pImgURL", pd.getImageURL());
				pIntent.putExtra("pId", pd.getProductId());
				pIntent.putExtra("pQuantity", holder.quantity.getText());
				pIntent.putExtra("product", pd);
				pIntent.putExtra("position", position);

                context.startActivity(pIntent);
            }
        });

	}
	



	@Override
	public int getItemCount() {
		//return productList == null ? 0 : productList.size();
		return filteredProductList == null ? 0 : filteredProductList.size();
	}

	@Override
	public Filter getFilter() {
		if (productFilter == null) {
			productFilter = new ProductFilter();
		}

		return productFilter;
	}

	class VersionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		TextView itemName, itemDesc, itemCost, availability, quantity, addItem, removeItem;
		ImageView imagView;

		public VersionViewHolder(View itemView) {
			super(itemView);

			itemName = (TextView) itemView.findViewById(R.id.item_name);

			itemDesc = (TextView) itemView.findViewById(R.id.item_short_desc);

			itemCost = (TextView) itemView.findViewById(R.id.item_price);

			availability = (TextView) itemView.findViewById(R.id.iteam_avilable);

			quantity = (TextView) itemView.findViewById(R.id.iteam_amount);

			itemName.setSelected(true);

			imagView = ((ImageView) itemView.findViewById(R.id.product_thumb));

			addItem = ((TextView) itemView.findViewById(R.id.add_item));

			removeItem = ((TextView) itemView.findViewById(R.id.remove_item));

			itemView.setOnClickListener(this);


		}

		@Override
		public void onClick(View v) {
			clickListener.onItemClick(v, getPosition());
		}
	}

	public interface OnItemClickListener {
		public void onItemClick(View view, int position);
	}

	public void SetOnItemClickListener(
			final OnItemClickListener itemClickListener) {
		this.clickListener = itemClickListener;
	}

	@Override
	public void onItemDismiss(int position) {
		filteredProductList.remove(position);
		notifyItemRemoved(position);
	}

	@Override
	public void onItemMove(int fromPosition, int toPosition) {
		if (fromPosition < toPosition) {
			for (int i = fromPosition; i < toPosition; i++) {
				Collections.swap(filteredProductList, i, i + 1);
			}
		} else {
			for (int i = fromPosition; i > toPosition; i--) {
				Collections.swap(filteredProductList, i, i - 1);
			}
		}
		notifyItemMoved(fromPosition, toPosition);
	}

	/**
	 * Custom filter for friend list
	 * Filter content in friend list according to the search text
	 */
	private class ProductFilter extends Filter {

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			FilterResults filterResults = new FilterResults();
			if (constraint != null && constraint.length() > 0) {
				ArrayList<Product> tempList = new ArrayList<Product>();

				// search content in friend list
				for (Product p : productList) {
					if (p.getItemName().toLowerCase().contains(constraint.toString().toLowerCase())) {
						tempList.add(p);
					}
				}

				filterResults.count = tempList.size();
				filterResults.values = tempList;
			} else {
				filterResults.count = productList.size();
				filterResults.values = productList;
			}

			return filterResults;
		}

		@Override
		protected void publishResults(CharSequence constraint, FilterResults filterResults) {
			filteredProductList = (ArrayList<Product>) filterResults.values;
			notifyDataSetChanged();
		}

	}


}
