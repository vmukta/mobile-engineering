package com.muktatest.dailydeals;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.muktatest.dailydeals.DealsFormatter.TodaysDeal;
import com.muktatest.interfaces.IDealsData;

public class QueryJsonTask extends AsyncTask<String, String, String> {

	private static final String TAG = "DealsListiingFragment";
	private static final String JSON_URL = "http://sheltered-bastion-2512.herokuapp.com/feed.json";
	private 			 IDealsData 	mDealsListener;
	private 			 DealsFormatter mDealsFormatter;
	
	public QueryJsonTask(IDealsData dealsListener) {
		mDealsListener = dealsListener;
		mDealsFormatter = new  DealsFormatter();
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected String doInBackground(String... params) {

		try {
			System.setProperty("http.keepAlive", "false");
		    
			URL url = new URL(JSON_URL);
			HttpURLConnection urlConnection = (HttpURLConnection) url
					.openConnection();
			urlConnection.setRequestProperty("Content-Length", "0");
			urlConnection.setRequestMethod("GET");
			urlConnection.connect();
			int responseCode = urlConnection.getResponseCode();
			switch (responseCode) {
			case 200:
			case 201:
				BufferedReader br = new BufferedReader(
						new InputStreamReader(urlConnection.getInputStream()));
				StringBuilder sb = new StringBuilder();
				String line;
				while ((line = br.readLine()) != null) {
					sb.append(line + "\n");
				}
				br.close();
				return sb.toString();

			}

		} catch (MalformedURLException e) {
			Log.i(TAG, e.getMessage());
		} catch (ProtocolException e) {
			Log.i(TAG, e.getMessage());
		} catch (FileNotFoundException e) {
			Log.i(TAG, e.getMessage());
		} catch (Exception e) {
			Log.i(TAG, e.getMessage());
		}
		return null;
	}

	@Override
	protected void onPostExecute(String result) {

		super.onPostExecute(result);
		
		ArrayList<TodaysDeal> deals = mDealsFormatter.getTodaysDeals(result);
		
		mDealsListener.onDealsFetch(deals);
	}
}

