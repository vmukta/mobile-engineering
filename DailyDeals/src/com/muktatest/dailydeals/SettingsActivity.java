package com.muktatest.dailydeals;


import java.io.InputStream;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class SettingsActivity extends FragmentActivity {

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}

	@Override
	protected void onCreate(Bundle userData ) {
		
		super.onCreate(userData);
		setContentView(R.layout.settings_main);
		Intent intent  = getIntent();
		TextView usersName = (TextView) findViewById(R.id.users_name);
		if (intent != null && usersName != null)
		{
			usersName.setText(intent.getStringExtra(DealsActivity.USERSNAME));
		}
		
		TextView userName = (TextView) findViewById(R.id.user_name);
		if (intent != null && userName != null)
		{
			String temp = intent.getStringExtra(DealsActivity.USERNAME);
			userName.setText(temp);
		}
		ImageView dealImage = (ImageView) findViewById(R.id.avatar_image);
		if (intent != null && dealImage != null)
		{
			new DownloadImageTask(dealImage).execute(intent.getStringExtra(DealsActivity.AVATAR_LINK));
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
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
	            Log.e("Error", e.getMessage());
	            e.printStackTrace();
	        }
	        return mIcon11;
	    }

	    protected void onPostExecute(Bitmap result) {
	        bmImage.setImageBitmap(result);
	    }
	}
}
