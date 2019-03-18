package ug.co.shop4me.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import ug.co.shop4me.view.activities.MainActivity;
import ug.co.shop4me.R;
import ug.co.shop4me.model.entities.OrderItem;
import ug.co.shop4me.view.activities.InstapayActivity2;
import ug.co.shop4me.view.activities.OrderDetailsActivity;

/**
 * Created by kaelyn on 11/05/2017.
 */

public class OrderItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<OrderItem> orderList;

    public OrderItemAdapter(Context mContext, List<OrderItem> storeList){
        this.orderList=storeList;
        this.mContext=mContext;

    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_items, parent, false);
        return new OrderItemAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final OrderItem pd =orderList.get(position);
        ((OrderItemAdapter.ViewHolder) holder).orderIdTextView.setText("Order Id: "+pd.getOrderId());
        ((OrderItemAdapter.ViewHolder) holder).orderDateTextView.setText(pd.getOrderDate());
        ((OrderItemAdapter.ViewHolder) holder).orderStatusTextView.setText(pd.getOrderStatus());
        ((OrderItemAdapter.ViewHolder) holder).orderTotalTextView.setText(MainActivity.formatter.format(Long.valueOf(pd.getOrderTotal()))+ "/=");
      //  MainActivity.formatter.format(Long.valueOf(obj.optString("total").split("[.]")[0] ))+ "/="

        //Hide button if niiga has paid or if its cod or if status aint recieved.
        if (pd.getOrderPaymentMethod().equals("Cash on Delivery")||pd.getOrderPaymentMethod().contains("Pay in Supermarket")||!pd.getOrderStatus().equals("Received")){
            ((ViewHolder) holder).payOrder.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent (mContext, OrderDetailsActivity.class);
                in.putExtra("id",pd.getOrderId());
                in.putExtra("date",pd.getOrderDate());
                in.putExtra("status",pd.getOrderStatus());
                in.putExtra("total",pd.getOrderTotal());
                in.putExtra("payment",pd.getOrderPaymentMethod());
                in.putExtra("address",pd.getOrderShippingAddress());
                in.putExtra("time",pd.getOrderTime());
                mContext.startActivity(in);
            }
        });

        ((ViewHolder) holder).payOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(mContext, InstapayActivity2.class);
                in.putExtra("address", pd.getOrderShippingAddress());
                in.putExtra("time", pd.getOrderTime());
                in.putExtra("payment", pd.getOrderPaymentMethod());
                in.putExtra("total", pd.getOrderTotal().toString());
                in.putExtra("id", pd.getOrderId());
                mContext.startActivity(in);

            }
        });

    }

    @Override
    public int getItemCount() {
       return orderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView orderIdTextView;
        public TextView orderDateTextView;
        public TextView orderStatusTextView;
        public TextView orderTotalTextView;
        public Button payOrder;


        public ViewHolder(View v) {
            super(v);
            orderIdTextView = (TextView) v.findViewById(R.id.orderIdTextView);
            orderDateTextView = (TextView) v.findViewById(R.id.orderDateTextView);
            orderStatusTextView = (TextView) v.findViewById(R.id.orderStatusTextView);
            orderTotalTextView = (TextView) v.findViewById(R.id.orderTotalTextView);
            payOrder = (Button) v.findViewById(R.id.payOrder);
        }
    }
}
