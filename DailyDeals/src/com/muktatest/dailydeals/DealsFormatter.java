package com.muktatest.dailydeals;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class DealsFormatter {

	private static final String TAG = "DealsFormatter";
	private static final String JSON_TAG_TITLE = "attrib";
	private static final String JSON_TAG_DESC = "desc";
	private static final String JSON_TAG_LINK = "href";
	private static final String JSON_TAG_IMG_SRC = "src";
	private static final String JSON_TAG_USER = "user";
	private static final String JSON_TAG_USERS_NAME = "name";
	private static final String JSON_TAG_AVATAR = "avatar";
	private static final String JSON_TAG_AVATAR_WIDTH = "width";
	private static final String JSON_TAG_AVATAR_HEIGHT = "height";
	private static final String JSON_TAG_USERNAME = "username";

	public class TodaysDeal {
		private String mTitle;
		private String mDesc;
		private String mLink;
		private String mImageSource;
		private String mNameOfUser;
		private String mUserName;
		private String mUserAvatarLInk;
		private String mUserAvatarWidth;
		private String mUserAvatarHeight;

		public String getTitle() {
			return mTitle;
		}

		public String getDescription() {
			return mDesc;
		}

		public String getLink() {
			return mLink;
		}

		public String getImageSource() {
			return mImageSource;
		}

		public String getNameOfUser() {
			return mNameOfUser;
		}

		public String getUserName() {
			return mUserName;
		}

		public String getAvatarLink() {
			return mUserAvatarLInk;
		}

		public String getAvatarWidth() {
			return mUserAvatarWidth;
		}

		public String getAvatarHeight() {
			return mUserAvatarHeight;
		}

		public TodaysDeal(String title, String description, String link,
				String imageSrc, String nameOfUser, String userName,
				String avatarLink, String avatarWidth, String avatarHeight) {
			mTitle = title;
			mDesc = description;
			mLink = link;
			mImageSource = imageSrc;
			mNameOfUser = nameOfUser;
			mUserName = userName;
			mUserAvatarLInk = avatarLink;
			mUserAvatarWidth = avatarWidth;
			mUserAvatarHeight = avatarHeight;

		}
	}

	/**
	 * Function to parse the JSON string and return TodaysDeal list
	 * @param strJSON - JSON string to parse 
	 * @return
	 */
	public ArrayList<TodaysDeal> getTodaysDeals(String strJSON) {
		ArrayList<TodaysDeal> deals = new ArrayList<TodaysDeal>();

		try {
			JSONArray jsonArray = new JSONArray(strJSON);

			if (jsonArray != null) {

				for (int i = 0; i < jsonArray.length(); i++) {

					JSONObject jsonObj = jsonArray.getJSONObject(i);

					JSONObject user = jsonObj.getJSONObject(JSON_TAG_USER);

					JSONObject avatar = user.getJSONObject(JSON_TAG_AVATAR);

					TodaysDeal deal = new TodaysDeal(
							jsonObj.getString(JSON_TAG_TITLE),
							jsonObj.getString(JSON_TAG_DESC),
							jsonObj.getString(JSON_TAG_LINK),
							jsonObj.getString(JSON_TAG_IMG_SRC),
							user.getString(JSON_TAG_USERS_NAME),
							user.getString(JSON_TAG_USERNAME),
							avatar.getString(JSON_TAG_IMG_SRC),
							avatar.getString(JSON_TAG_AVATAR_WIDTH),
							avatar.getString(JSON_TAG_AVATAR_HEIGHT));
					deals.add(deal);
				}
			}

		} catch (JSONException e) {

			Log.e(TAG, "Error parsing data in getTodaysDeals() " + e.getMessage());
		}
		return deals;
	}
}
