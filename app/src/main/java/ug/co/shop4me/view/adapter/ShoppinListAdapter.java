/*
 * Copyright (C) 2015 Paul Burke
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ug.co.shop4me.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import ug.co.shop4me.view.activities.MainActivity;
import ug.co.shop4me.R;
import ug.co.shop4me.model.entities.Product;
import ug.co.shop4me.util.ColorGenerator;
import ug.co.shop4me.view.customview.ItemTouchHelperViewHolder;
import ug.co.shop4me.view.customview.TextDrawable;
import ug.co.shop4me.view.customview.TextDrawable.IBuilder;

/**
 * Simple RecyclerView.Adapter that implements {@link ItemTouchHelperAdapter} to
 * respond to move and dismiss events from a
 * {@link android.support.v7.widget.helper.ItemTouchHelper}.
 *
 * @author Paul Burke (ipaulpro)
 */
public class ShoppinListAdapter extends
		RecyclerView.Adapter<ShoppinListAdapter.ItemViewHolder>  {


	private ColorGenerator mColorGenerator = ColorGenerator.MATERIAL;

	private IBuilder mDrawableBuilder;

	private TextDrawable drawable;
	private String ImageUrl;
	private List<Product> productList = new ArrayList<Product>();
	private Context context;

	public ShoppinListAdapter(Context context) {
		this.context = context;
		productList = MainActivity.shoppingCartList;
	}


	@Override
	public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_list, parent, false);
		ItemViewHolder itemViewHolder = new ItemViewHolder(view);
		return itemViewHolder;
	}

	@Override
	public void onBindViewHolder(final ItemViewHolder holder, final int position) {
		holder.itemName.setText(productList.get(position).getItemName());
		final Long sellCostString = Long.valueOf(productList.get(position).getSellMRP().split("[.]")[0]);
		String qn = productList.get(position).getQuantity();
		int quantity = Integer.valueOf(qn);
		final Long total = sellCostString * quantity;
holder.itemCost.setText(MainActivity.formatter.format(total)+"/=");
		mDrawableBuilder = TextDrawable.builder().beginConfig().withBorder(4)
				.endConfig().roundRect(10);
		drawable = mDrawableBuilder.build(String.valueOf(productList
				.get(position).getItemName().charAt(0)), mColorGenerator
				.getColor(productList.get(position).getItemName()));
		ImageUrl = productList.get(position).getImageURL();
		holder.quanitity.setText(productList.get(position).getQuantity());
		Glide.with(context).load(ImageUrl).into(holder.imagView);
	}


	@Override
	public int getItemCount() {
		return productList.size();
	}

	/**
	 * Simple example of a view holder that implements
	 * {@link ItemTouchHelperViewHolder} and has a "handle" view that initiates
	 * a drag event when touched.
	 */
	public static class ItemViewHolder extends RecyclerView.ViewHolder
	 {

		TextView itemName, itemCost, quanitity;
		ImageView imagView;

		public ItemViewHolder(View itemView) {
			super(itemView);
			// handleView = (ImageView) itemView.findViewById(R.id.handle);
			itemName = (TextView) itemView.findViewById(R.id.item_name);
			itemCost = (TextView) itemView.findViewById(R.id.item_price);
			quanitity = (TextView) itemView.findViewById(R.id.iteam_amount);
			itemName.setSelected(true);
			imagView = ((ImageView) itemView.findViewById(R.id.product_thumb2));
		}
	}
}
