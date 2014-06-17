package com.muktatest.interfaces;

import java.util.ArrayList;

import com.muktatest.dailydeals.DealsFormatter.TodaysDeal;

public interface IDealsData {

	public void onDealsFetch(ArrayList<TodaysDeal> result);
}
