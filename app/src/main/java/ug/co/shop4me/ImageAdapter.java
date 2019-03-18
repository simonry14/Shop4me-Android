package ug.co.shop4me;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import ug.co.shop4me.view.SquareImageView;

import java.util.List;


/**
 * Created by kaelyn on 17/07/2015.
 */public class ImageAdapter extends BaseAdapter {
    private Context mContext;

    private List<CategoryGridItem> items ;

    public ImageAdapter(List<CategoryGridItem> items, Context context) {
        this.items = items;
        mContext = context;
    }



    public int getCount() {
        //return mThumbIds.length;
        return items.size();
    }

    public Object getItem(int position) {
        //return null;
        return (CategoryGridItem)items.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView imageView;


        CategoryGridItem item = items.get(position);
        GridItemPlaceHolder holder = null;

        if (convertView == null) {
            // if it's not recycled, initialize some attributes
          //  imageView = new ImageView(mContext);
           // imageView.setLayoutParams(new GridView.LayoutParams(400,400));
           // imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
           // imageView.setPadding(0,0,0,0);

            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.item_category_gridview, parent, false);

            holder = new GridItemPlaceHolder();
            holder.atachedImage = (SquareImageView) convertView.findViewById(R.id.item_manifesto_gridview_image);
            holder.title = (TextView)convertView.findViewById(R.id.item_manifesto_gridview_title);

            convertView.setTag(holder);
            holder = (GridItemPlaceHolder)convertView.getTag();
            holder.title.setText(item.getName());
            holder.atachedImage.setImageResource(item.getAttachedImage());


        } else {
            holder = (GridItemPlaceHolder)convertView.getTag();
            //imageView = (ImageView) convertView;
            //now attaching the corresponding components...
            holder.title.setText(item.getName());
           // holder.atachedImage =
            // holder.atachedImage = item.getAttachedImage();
           // if(item.getAttachedImage() != -1)
            holder.atachedImage.setImageResource(item.getAttachedImage());
              //      Picasso.with(mContext).load(item.getAttachedImage()).into(holder.atachedImage);
            //Picasso.with(mContext).load(item.getAttachedImage()).placeholder(R.drawable.img_placeholder_360_480).error(R.drawable.img_missing_360_480).into(holder.atachedImage);

        }

       // imageView.setImageResource(mThumbIds[position]);
       //
       // return imageView;
        return convertView;
    }

    // references to our images
    //private Integer[] mThumbIds = {
        //    R.drawable.education, R.drawable.agriculture,
        //    R.drawable.health, R.drawable.roads,
         //   R.drawable.sports, R.drawable.governance,

    //};


    private static class GridItemPlaceHolder {
        SquareImageView atachedImage;
        TextView title;
    }
}
