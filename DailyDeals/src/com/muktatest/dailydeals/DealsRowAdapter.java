package com.muktatest.dailydeals;

import java.io.InputStream;
import java.util.ArrayList;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.muktatest.dailydeals.DealsFormatter.TodaysDeal;

public class DealsRowAdapter extends ArrayAdapter<TodaysDeal> {

	private final ArrayList<TodaysDeal> values;
	private final Context mContext;
	private static final String TAG = "DealsRowAdapter";

	public DealsRowAdapter(Context context, ArrayList<TodaysDeal> deals) {
		super(context, R.layout.deals_row, deals);

		this.mContext = context;
		this.values = deals;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.deals_row, null, true);

		TextView title = (TextView) rowView.findViewById(R.id.deals_title);
		TextView description = (TextView) rowView
				.findViewById(R.id.deals_description);
		ImageView dealImage = (ImageView) rowView
				.findViewById(R.id.dealsicon_image);
		dealImage.setScaleType(ScaleType.CENTER_INSIDE);
		final String dealLink = values.get(position).getLink();

		dealImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				gotoLink(dealLink);
			}
		});

		// Set title and description
		TodaysDeal deal = values.get(position);
		if (deal != null)
		{
			title.setText(deal.getTitle());
			description.setText(deal.getDescription());
			new DownloadImageTask(dealImage).execute(deal.getImageSource());
		}
		return rowView;

	}

	public void onRowClick(int position) {
		String dealsLink = values.get(position).getLink();
		if (dealsLink != null && !dealsLink.isEmpty())
		{
			gotoLink(dealsLink);
		}
	}

	private void gotoLink(String link) {
		try {
			Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
			mContext.startActivity(myIntent);
		} catch (ActivityNotFoundException e) {

			Log.e(TAG , "Error parsing data in getTodaysDeals() " + e.getMessage());
		}
	}

	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		ImageView bmImage;

		public DownloadImageTask(ImageView bmImage) {
			this.bmImage = bmImage;
		}

		protected Bitmap doInBackground(String... urls) {
			String urldisplay = urls[0];
			Bitmap mIcon11 = null;
			try {
				InputStream in = new java.net.URL(urldisplay).openStream();
				mIcon11 = BitmapFactory.decodeStream(in);
			} catch (Exception e) {
				Log.e(TAG , "Error parsing data in getTodaysDeals() " + e.getMessage());
			}
			return mIcon11;
		}

		protected void onPostExecute(Bitmap result) {
			bmImage.setImageBitmap(result);
			
		}
	}
}
