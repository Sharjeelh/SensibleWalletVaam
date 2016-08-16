package com.model;

public class CountryListModel {
	String str_country_id, str_country_status, str_country_price_sign, str_country_name, str_country_logo;

	public CountryListModel() {}

	public CountryListModel(String str_country_id, String str_country_status, 
			String str_country_price_sign, String str_country_name, String str_country_logo) {
		super();
		this.str_country_id = str_country_id;
		this.str_country_status = str_country_status;
		this.str_country_price_sign = str_country_price_sign;
		this.str_country_name = str_country_name;
		this.str_country_logo = str_country_logo;
	}

	public String getStr_country_id() {
		return str_country_id;
	}

	public void setStr_country_id(String str_country_id) {
		this.str_country_id = str_country_id;
	}

	public String getStr_country_status() {
		return str_country_status;
	}

	public void setStr_country_status(String str_country_status) {
		this.str_country_status = str_country_status;
	}

	public String getStr_country_price_sign() {
		return str_country_price_sign;
	}

	public void setStr_country_price_sign(String str_country_price_sign) {
		this.str_country_price_sign = str_country_price_sign;
	}

	public String getStr_country_name() {
		return str_country_name;
	}

	public void setStr_country_name(String str_country_name) {
		this.str_country_name = str_country_name;
	}

	public String getStr_country_logo() {
		return str_country_logo;
	}

	public void setStr_country_logo(String str_country_logo) {
		this.str_country_logo = str_country_logo;
	}
}
