package com.muktatest.interfaces;

public interface IInternetAware {
	/**
	 * Called when Internet has been lost
	 */
	public void onInternetLost();

	/**
	 * Called when Internet has been appeared
	 */
	public void onInternetFound();
}
