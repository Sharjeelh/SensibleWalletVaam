package com.model;

public class GroupItemModel {
	String str_offer_id, str_offers_redeem_amount, str_offer_price, strOfferImage;

	public GroupItemModel() {}

	public GroupItemModel(String str_offer_id, String str_offers_redeem_amount, String str_offer_price,
			String strOfferImage) {
		super();
		this.strOfferImage = strOfferImage;
		this.str_offer_id = str_offer_id;
		this.str_offers_redeem_amount = str_offers_redeem_amount;
		this.str_offer_price = str_offer_price;
	}

	public String getStrOfferImage() {
		return strOfferImage;
	}

	public void setStrOfferImage(String strOfferImage) {
		this.strOfferImage = strOfferImage;
	}

	public String getStr_offer_id() {
		return str_offer_id;
	}

	public void setStr_offer_id(String str_offer_id) {
		this.str_offer_id = str_offer_id;
	}

	public String getStr_offers_redeem_amount() {
		return str_offers_redeem_amount;
	}

	public void setStr_offers_redeem_amount(String str_offers_redeem_amount) {
		this.str_offers_redeem_amount = str_offers_redeem_amount;
	}

	public String getStr_offer_price() {
		return str_offer_price;
	}

	public void setStr_offer_price(String str_offer_price) {
		this.str_offer_price = str_offer_price;
	}
}
