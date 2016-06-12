package com.lowermainlandpharmacyservices.lmpsformulary.Model;

public class Drug {
	protected String status;
    protected String drugClass;

//	public Drug(String status) {
//		this.status = status;
//	}

    public Drug(String status, String drugClass) {
        this.status = status;
        this.drugClass = drugClass;
    }

	public String getStatus() {
		return status;
	}

	public void setStatus(String newStatus) {
		this.status = newStatus;
	}

    public String getDrugClass() {
        return drugClass;
    }

    public void setDrugClass(String drugClass) {
        this.drugClass = drugClass;
    }
}
