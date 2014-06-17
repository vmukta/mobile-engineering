package com.muktatest.dailydeals;

import java.util.ArrayList;

import android.content.Intent;
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

import com.muktatest.dailydeals.DealsFormatter.TodaysDeal;
import com.muktatest.interfaces.IDealsData;

public class DealsActivity extends FragmentActivity{
	
	public static final String USERSNAME = "UsersName";
	public static final String USERNAME = "UserName";
	public static final String AVATAR_LINK = "Link";
	
	/**
	 * Class to store user information to be shown in Settings
	 * 
	 */
	public class UserInfo 
	{
		private String mUsersName;
		private String mUserName;
		private String mAvatarLink;

		public UserInfo(String usersName, String userName , String avatarURL)
		{
			mUsersName = usersName;
			mUserName = userName;
			mAvatarLink = avatarURL ; 
		}
	
	};
	
	private UserInfo mUserInfo; 
	
	/**
	 * Set user information
	 * @param usersName - name of the user 
	 * @param userName - user name 
	 * @param avatarURL - URL to download avatar for the user 
	 */
	public void setUserInfo(String usersName, String userName,String avatarURL)
	{
		mUserInfo = new UserInfo(usersName, userName, avatarURL);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mUserInfo = null ;
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new DealsListingFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Intent settingsIntent = new Intent(this,SettingsActivity.class);
			if (mUserInfo != null )
			{
				settingsIntent.putExtra(USERSNAME, mUserInfo.mUsersName);
				settingsIntent.putExtra(USERNAME, mUserInfo.mUserName);
				settingsIntent.putExtra(AVATAR_LINK, mUserInfo.mAvatarLink);
			}
			startActivity(settingsIntent);
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Fragment to show the listing from JSON
	 */
	public static class DealsListingFragment extends Fragment implements IDealsData {

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
			mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
			mDealsListView = (ListView) rootView.findViewById(R.id.dealslist);
			mDealsListView.setOnItemClickListener(new ListView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
				
					if (mDealsAdapter!=null)
					{
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
			
			mProgressBar.setVisibility(View.VISIBLE);
			mQueryJsonTask = new QueryJsonTask(this);
			mQueryJsonTask.execute();
		}

		@Override
		public void onDealsFetch(ArrayList<TodaysDeal> result) {
			//stop progress bar and show the data 
			mProgressBar.setVisibility(View.GONE);
			
			// store the user information from the result 
			if ( result != null)
			{
				TodaysDeal user = result.get(0);
				if ( user !=null )
				{
					mParentActivity.setUserInfo(user.getNameOfUser(), user.getUserName(), user.getAvatarLink());
				}
				mDealsAdapter = new DealsRowAdapter(getActivity(), result);
				mDealsListView.setAdapter(mDealsAdapter);
				mDealsListView.setVisibility(View.VISIBLE);
			}
			
		}

	}
}
