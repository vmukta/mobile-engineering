package com.muktatest.dailydeals;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.muktatest.dailydeals.DealsFormatter.TodaysDeal;
import com.muktatest.interfaces.IDealsData;
import com.muktatest.interfaces.IInternetAware;
import com.muktatest.receivers.ConnectionStatusReceiver;

public class DealsActivity extends FragmentActivity implements IInternetAware {

	public static final String USERSNAME = "UsersName";
	public static final String USERNAME = "UserName";
	public static final String AVATAR_LINK = "Link";
	private static final String INTERNET_LOST = "No connection found! Please connect and try again.";

	/**
	 * Class to store user information to be shown in Settings
	 * 
	 */
	public class UserInfo {
		private String mUsersName;
		private String mUserName;
		private String mAvatarLink;

		public UserInfo(String usersName, String userName, String avatarURL) {
			mUsersName = usersName;
			mUserName = userName;
			mAvatarLink = avatarURL;
		}

	};

	private UserInfo mUserInfo;
	private IntentFilter mConnectionFilter;
	private ConnectionStatusReceiver mReceiver;
	private MenuItem mSettingMenuItem;

	/**
	 * Set user information
	 * 
	 * @param usersName
	 *            - name of the user
	 * @param userName
	 *            - user name
	 * @param avatarURL
	 *            - URL to download avatar for the user
	 */
	public void setUserInfo(String usersName, String userName, String avatarURL) {
		mUserInfo = new UserInfo(usersName, userName, avatarURL);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mUserInfo = null;
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new DealsListingFragment()).commit();
		}
		mReceiver = new ConnectionStatusReceiver(this);
		mConnectionFilter = new IntentFilter();
		mConnectionFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
		mConnectionFilter.addAction("android.intent.action.SERVICE_STATE");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);

		mSettingMenuItem = menu.getItem(0);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Intent settingsIntent = new Intent(this, SettingsActivity.class);
			if (mUserInfo != null) {
				settingsIntent.putExtra(USERSNAME, mUserInfo.mUsersName);
				settingsIntent.putExtra(USERNAME, mUserInfo.mUserName);
				settingsIntent.putExtra(AVATAR_LINK, mUserInfo.mAvatarLink);
			}
			startActivity(settingsIntent);
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(mReceiver);
	}

	@Override
	protected void onResume() {

		super.onResume();
		registerReceiver(mReceiver, mConnectionFilter);
	}

	/**
	 * Fragment to show the listing from JSON
	 */
	public static class DealsListingFragment extends Fragment implements
			IDealsData {

		private QueryJsonTask mQueryJsonTask;
		private ProgressBar mProgressBar;
		private DealsRowAdapter mDealsAdapter;
		ArrayList<TodaysDeal> mDealsListing;
		private ListView mDealsListView;
		private DealsActivity mParentActivity;

		public DealsListingFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);

			mParentActivity = (DealsActivity) getActivity();
			mProgressBar = (ProgressBar) rootView
					.findViewById(R.id.progressBar);
			mDealsListView = (ListView) rootView.findViewById(R.id.dealslist);
			mDealsListView
					.setOnItemClickListener(new ListView.OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {

							if (mDealsAdapter != null) {
								mDealsAdapter.onRowClick(position);
							}

						}

					});

			return rootView;
		}

		@Override
		public void onPause() {
			// TODO Auto-generated method stub
			super.onPause();
		}

		@Override
		public void onResume() {

			super.onResume();

			ConnectivityManager connectivityManager = (ConnectivityManager) getActivity()
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo activeNetInfo = connectivityManager
					.getActiveNetworkInfo();

			if (activeNetInfo != null && activeNetInfo.isConnected()) {

				mProgressBar.setVisibility(View.VISIBLE);
				mQueryJsonTask = new QueryJsonTask(this);
				mQueryJsonTask.execute();
			}
		}

		@Override
		public void onDealsFetch(ArrayList<TodaysDeal> result) {
			// stop progress bar and show the data
			mProgressBar.setVisibility(View.GONE);

			// store the user information from the result
			if (result != null) {
				TodaysDeal user = result.get(0);
				if (user != null) {
					mParentActivity.setUserInfo(user.getNameOfUser(),
							user.getUserName(), user.getAvatarLink());
				}
				mDealsAdapter = new DealsRowAdapter(getActivity(), result);
				mDealsListView.setAdapter(mDealsAdapter);
				mDealsListView.setVisibility(View.VISIBLE);
			}

		}

	}

	@Override
	public void onInternetLost() {
		Toast message = Toast.makeText(this, INTERNET_LOST, Toast.LENGTH_LONG);
		message.show();

		if (mSettingMenuItem != null) {
			mSettingMenuItem.setEnabled(false);
		}
	}

	@Override
	public void onInternetFound() {
		if (mSettingMenuItem != null) {
			mSettingMenuItem.setEnabled(true);
		}
	}

}
