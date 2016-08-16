package com.model;

public class ConnectModel {
	String connectId, connectName, connectLink, connectLogo;

	public ConnectModel() {}

	public ConnectModel(String connectId, String connectName, String connectLink, String connectLogo) {
		super();
		this.connectId	 = connectId;
		this.connectName = connectName;
		this.connectLink = connectLink;
		this.connectLogo = connectLogo;
	}

	public String getConnectId() {
		return connectId;
	}

	public void setConnectId(String connectId) {
		this.connectId = connectId;
	}

	public String getConnectName() {
		return connectName;
	}

	public void setConnectName(String connectName) {
		this.connectName = connectName;
	}

	public String getConnectLink() {
		return connectLink;
	}

	public void setConnectLink(String connectLink) {
		this.connectLink = connectLink;
	}

	public String getConnectLogo() {
		return connectLogo;
	}

	public void setConnectLogo(String connectLogo) {
		this.connectLogo = connectLogo;
	}

}
