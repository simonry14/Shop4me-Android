package ug.co.shop4me.view.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import ug.co.shop4me.CategoryGridItem;
import ug.co.shop4me.view.activities.ProductListActivity;
import ug.co.shop4me.ImageAdapter;
import ug.co.shop4me.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CategoryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CategoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoryFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public CategoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CategoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CategoryFragment newInstance(String param1, String param2) {
        CategoryFragment fragment = new CategoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private Context context;
    private ImageAdapter imageAdapter;

    private List<CategoryGridItem> items = new ArrayList();



    private OnFragmentInteractionListener mListener;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        final GridView gridView = (GridView) view.findViewById(R.id.gridView1);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                switch (position) {
                    case 0:
                        Intent milkIntent = new Intent(getActivity(), ProductListActivity.class);
                        milkIntent.putExtra("categoryId", 89);
                        startActivity(milkIntent);
                        break;
                    case 1:
                        Intent fruitsIntent = new Intent(getActivity(), ProductListActivity.class);
                        fruitsIntent.putExtra("categoryId", 90);
                        startActivity(fruitsIntent);
                        break;
                    case 2:
                        Intent bakeryIntent = new Intent(getActivity(), ProductListActivity.class);
                        bakeryIntent.putExtra("categoryId", 92);
                        startActivity(bakeryIntent);
                        break;
                    case 3:
                        Intent softDrinksIntent = new Intent(getActivity(), ProductListActivity.class);
                        softDrinksIntent.putExtra("categoryId", 93);
                        startActivity(softDrinksIntent);
                        break;
                    case 4:
                        Intent riceIntent = new Intent(getActivity(), ProductListActivity.class);
                        riceIntent.putExtra("categoryId", 97);

                        startActivity(riceIntent);
                        break;
                    case 5:
                        Intent breakfastIntent = new Intent(getActivity(), ProductListActivity.class);
                        breakfastIntent.putExtra("categoryId", 98);
                        startActivity(breakfastIntent);
                        break;
                    case 6:
                        Intent snacksIntent = new Intent(getActivity(), ProductListActivity.class);
                        snacksIntent.putExtra("categoryId", 99);
                        startActivity(snacksIntent);
                        break;
                    case 7:
                        Intent meatIntent = new Intent(getActivity(), ProductListActivity.class);
                        meatIntent.putExtra("categoryId", 84);
                        startActivity(meatIntent);
                        break;
                    case 8:
                        Intent healthIntent = new Intent(getActivity(), ProductListActivity.class);
                        healthIntent.putExtra("categoryId", 94);
                        startActivity(healthIntent);
                        break;
                    case 9:
                        Intent babyIntent = new Intent(getActivity(), ProductListActivity.class);
                        babyIntent.putExtra("categoryId", 95);
                        startActivity(babyIntent);
                        break;
                    case 10:
                        Intent alcoholIntent = new Intent(getActivity(), ProductListActivity.class);
                        alcoholIntent.putExtra("categoryId", 96);
                        startActivity(alcoholIntent);
                        break;
                    case 11:
                        Intent seasoningIntent = new Intent(getActivity(), ProductListActivity.class);
                        seasoningIntent.putExtra("categoryId", 148);
                        startActivity(seasoningIntent);
                        break;

                }
            }
        });

        items =  createItems();
        imageAdapter = new ImageAdapter(items, view.getContext());
        gridView.setAdapter(imageAdapter); // uses the view to get the context instead of getActivity().
        return view;
    }

    public List<CategoryGridItem> createItems()
    {
        CategoryGridItem healthItem = new CategoryGridItem();
        healthItem.setName("Dairy & Eggs");
        healthItem.setAttachedImage(R.drawable.milk);
        items.add(healthItem);

        CategoryGridItem educationItem = new CategoryGridItem();
        educationItem.setName("Fruits & Vegetables");
        educationItem.setAttachedImage(R.drawable.dd);
        items.add(educationItem);

        CategoryGridItem agricItem = new CategoryGridItem();
        agricItem.setName("Bakery");
        agricItem.setAttachedImage(R.drawable.bakery);
        items.add(agricItem);

        CategoryGridItem infraItem = new CategoryGridItem();
        infraItem.setName("Soft Drinks & Juices");
        infraItem.setAttachedImage(R.drawable.softdrinks);
        items.add(infraItem);

        CategoryGridItem riceItem = new CategoryGridItem();
        riceItem.setName("Rice & Pasta");
        riceItem.setAttachedImage(R.drawable.rice);
        items.add(riceItem);

        CategoryGridItem sportsItem = new CategoryGridItem();
        sportsItem.setName("Breakfast Goodies");
        sportsItem.setAttachedImage(R.drawable.cereal);
        items.add(sportsItem);

        CategoryGridItem sportsItem1 = new CategoryGridItem();
        sportsItem1.setName("Snacks & Candies");
        sportsItem1.setAttachedImage(R.drawable.snacks);
        items.add(sportsItem1);

        CategoryGridItem governanceItem = new CategoryGridItem();
        governanceItem.setName("Meats & Fish");
        governanceItem.setAttachedImage(R.drawable.meat);
        items.add(governanceItem);

        CategoryGridItem beautyItem = new CategoryGridItem();
        beautyItem.setName("Health & Beauty");
        beautyItem.setAttachedImage(R.drawable.beauty);
        items.add(beautyItem);

        CategoryGridItem babyItem = new CategoryGridItem();
        babyItem.setName("Baby Items");
        babyItem.setAttachedImage(R.drawable.baby);
        items.add(babyItem);

        CategoryGridItem alcoholItem = new CategoryGridItem();
        alcoholItem.setName("Alcohol");
        alcoholItem.setAttachedImage(R.drawable.beer);
        items.add(alcoholItem);

        CategoryGridItem seasoningItem = new CategoryGridItem();
        seasoningItem.setName("Seasonings & Cooking oil");
        seasoningItem.setAttachedImage(R.drawable.seasoning);
        items.add(seasoningItem);

        return items;

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
