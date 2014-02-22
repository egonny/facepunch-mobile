package com.egonny.facepunch.model.menu;

public class ActionItem extends MenuListItem {

	private Action mAction;

	public ActionItem(String title, int iconId, Action action) {
		super(title, iconId);
		mAction = action;
	}

	public Action getAction() {
		return mAction;
	}

	public enum Action {
		LOG_IN,
		PM,
		POPULAR,
		SUBSCRIBED,
		PROFILE
	}
}
