package com.lowermainlandpharmacyservices.lmpsformulary.Model;

public class Drug {
	protected String status;

	public Drug(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String newStatus) {
		this.status = newStatus;
	}
}
