package com.model;

public class GroupModel {
	String str_giftcard_id, str_country_id, str_giftcard_logo, str_giftcard_description, str_giftcard_title;
	
	public GroupModel() {}

	public GroupModel(String str_giftcard_id, String str_country_id, String str_giftcard_logo, String str_giftcard_description,
			String str_giftcard_title) {
		super();
		this.str_giftcard_id = str_giftcard_id;
		this.str_country_id = str_country_id;
		this.str_giftcard_logo = str_giftcard_logo;
		this.str_giftcard_description = str_giftcard_description;
		this.str_giftcard_title = str_giftcard_title;
	}

	public String getStr_giftcard_id() {
		return str_giftcard_id;
	}

	public void setStr_giftcard_id(String str_giftcard_id) {
		this.str_giftcard_id = str_giftcard_id;
	}

	public String getStr_country_id() {
		return str_country_id;
	}

	public void setStr_country_id(String str_country_id) {
		this.str_country_id = str_country_id;
	}

	public String getStr_giftcard_logo() {
		return str_giftcard_logo;
	}

	public void setStr_giftcard_logo(String str_giftcard_logo) {
		this.str_giftcard_logo = str_giftcard_logo;
	}

	public String getStr_giftcard_description() {
		return str_giftcard_description;
	}

	public void setStr_giftcard_description(String str_giftcard_description) {
		this.str_giftcard_description = str_giftcard_description;
	}

	public String getStr_giftcard_title() {
		return str_giftcard_title;
	}

	public void setStr_giftcard_title(String str_giftcard_title) {
		this.str_giftcard_title = str_giftcard_title;
	}
}
