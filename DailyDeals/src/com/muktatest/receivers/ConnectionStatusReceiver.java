package com.muktatest.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.muktatest.interfaces.IInternetAware;

public class ConnectionStatusReceiver extends BroadcastReceiver {

	private IInternetAware mListener;

	public ConnectionStatusReceiver(IInternetAware listener) {
		mListener = listener;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();

		if (activeNetInfo != null && activeNetInfo.isConnected()) {
			mListener.onInternetFound();
		} else {
			mListener.onInternetLost();
		}
	}
}