package com.model;

public class EarnCreditModel {
	String offerName, offerDetail, offerCredit, tag;
	int image;
	boolean isBadgeVisible;

	public EarnCreditModel() {}

	public EarnCreditModel(String offerName, String offerDetail, String offerCredit, int image, boolean isBadgeVisible) {
		super();
		this.image		= image;
		this.offerName 	= offerName;
		this.offerDetail = offerDetail;
		this.offerCredit = offerCredit;
		this.isBadgeVisible = isBadgeVisible;
	}

	public int getImage() {
		return image;
	}

	public void setImage(int image) {
		this.image = image;
	}

	public String getOfferName() {
		return offerName;
	}

	public void setOfferName(String offerName) {
		this.offerName = offerName;
	}

	public String getOfferDetail() {
		return offerDetail;
	}

	public void setOfferDetail(String offerDetail) {
		this.offerDetail = offerDetail;
	}

	public String getOfferCredit() {
		return offerCredit;
	}

	public void setOfferCredit(String offerCredit) {
		this.offerCredit = offerCredit;
	}

	public void setTag(String tag) { this.tag = tag; }

	public String getTag() { return tag; }

	public boolean isBadgeVisible() { return isBadgeVisible; }
}
