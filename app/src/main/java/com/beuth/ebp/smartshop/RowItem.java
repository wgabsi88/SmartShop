package com.beuth.ebp.smartshop;

public class RowItem {

	private String title;
	private String icon;

	public RowItem(String title, String icon) {
		this.title = title;
		this.icon = icon;

	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

}
