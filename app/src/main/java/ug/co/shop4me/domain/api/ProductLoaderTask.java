package ug.co.shop4me.domain.api;

import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;

import ug.co.shop4me.view.activities.MainActivity;
import ug.co.shop4me.domain.mock.FakeWebServer;
import ug.co.shop4me.model.GlobaDataHolder;

import ug.co.shop4me.view.adapter.ProductOverViewPagerAdapter;


import java.util.Set;

/**
 * The Class ImageLoaderTask.
 */
public class ProductLoaderTask extends AsyncTask<String, Void, Void> {

	private Context context;
	private ViewPager viewPager;
	private TabLayout tabs;
	private RecyclerView recyclerView;

	public ProductLoaderTask(RecyclerView listView, Context context,
                             ViewPager viewpager, TabLayout tabs) {

		this.viewPager = viewpager;
		this.tabs = tabs;
		this.context = context;

	}

	@Override
	protected void onPreExecute() {

		super.onPreExecute();


	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);


		
		setUpUi();

	}

	@Override
	protected Void doInBackground(String... params) {

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		FakeWebServer.getFakeWebServer().getAllElectronics();

		return null;
	}

	private void setUpUi() {

		setupViewPager();

		// bitmap = BitmapFactory
		// .decodeResource(getResources(), R.drawable.header);
		//
		// Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
		// @SuppressWarnings("ResourceType")
		// @Override
		// public void onGenerated(Palette palette) {
		//
		// int vibrantColor = palette.getVibrantColor(R.color.primary_500);
		// int vibrantDarkColor = palette
		// .getDarkVibrantColor(R.color.primary_700);
		// collapsingToolbarLayout.setContentScrimColor(vibrantColor);
		// collapsingToolbarLayout
		// .setStatusBarScrimColor(vibrantDarkColor);
		// }
		// });

		// tabLayout
		// .setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
		// @Override
		// public void onTabSelected(TabLayout.Tab tab) {
		//
		// viewPager.setCurrentItem(tab.getPosition());
		//
		// switch (tab.getPosition()) {
		// case 0:
		//
		// header.setImageResource(R.drawable.header);
		//
		// bitmap = BitmapFactory.decodeResource(
		// getResources(), R.drawable.header);
		//
		// Palette.from(bitmap).generate(
		// new Palette.PaletteAsyncListener() {
		// @SuppressWarnings("ResourceType")
		// @Override
		// public void onGenerated(Palette palette) {
		//
		// int vibrantColor = palette
		// .getVibrantColor(R.color.primary_500);
		// int vibrantDarkColor = palette
		// .getDarkVibrantColor(R.color.primary_700);
		// collapsingToolbarLayout
		// .setContentScrimColor(vibrantColor);
		// collapsingToolbarLayout
		// .setStatusBarScrimColor(vibrantDarkColor);
		// }
		// });
		// break;
		// case 1:
		//
		// header.setImageResource(R.drawable.header_1);
		//
		// bitmap = BitmapFactory.decodeResource(
		// getResources(), R.drawable.header_1);
		//
		// Palette.from(bitmap).generate(
		// new Palette.PaletteAsyncListener() {
		// @SuppressWarnings("ResourceType")
		// @Override
		// public void onGenerated(Palette palette) {
		//
		// int vibrantColor = palette
		// .getVibrantColor(R.color.primary_500);
		// int vibrantDarkColor = palette
		// .getDarkVibrantColor(R.color.primary_700);
		// collapsingToolbarLayout
		// .setContentScrimColor(vibrantColor);
		// collapsingToolbarLayout
		// .setStatusBarScrimColor(vibrantDarkColor);
		// }
		// });
		//
		// break;
		// case 2:
		//
		// header.setImageResource(R.drawable.header2);
		//
		// Bitmap bitmap = BitmapFactory.decodeResource(
		// getResources(), R.drawable.header2);
		//
		// Palette.from(bitmap).generate(
		// new Palette.PaletteAsyncListener() {
		// @SuppressWarnings("ResourceType")
		// @Override
		// public void onGenerated(Palette palette) {
		//
		// int vibrantColor = palette
		// .getVibrantColor(R.color.primary_500);
		// int vibrantDarkColor = palette
		// .getDarkVibrantColor(R.color.primary_700);
		// collapsingToolbarLayout
		// .setContentScrimColor(vibrantColor);
		// collapsingToolbarLayout
		// .setStatusBarScrimColor(vibrantDarkColor);
		// }
		// });
		//
		// break;
		// }
		// }
		//
		// @Override
		// public void onTabUnselected(TabLayout.Tab tab) {
		//
		// }
		//
		// @Override
		// public void onTabReselected(TabLayout.Tab tab) {
		//
		// }
		// });

	}

	private void setupViewPager() {

		ProductOverViewPagerAdapter adapter = new ProductOverViewPagerAdapter(
				new MainActivity().getSupportFragmentManager());

		Set<String> keys = GlobaDataHolder.getGlobaDataHolder().getProductMap()
				.keySet();

		for (String string : keys) {

			//adapter.addFrag(new ProductListFragment(string), string);

		}

		viewPager.setAdapter(adapter);

//		viewPager.setPageTransformer(true,
//				new );

		tabs.setupWithViewPager(viewPager);

	}

}