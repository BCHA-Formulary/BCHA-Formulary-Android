package com.lowermainlandpharmacyservices.lmpsformulary;

import java.util.ArrayList;

public class BrandFormularyDrug extends BrandDrug {

	public ArrayList<String> strengths;

	public BrandFormularyDrug(String genericName, String brandName,
			String strength) {
		super(genericName, brandName, "Formulary");
		strengths = new ArrayList<String>();
		strengths.add(strength);
	}

	public ArrayList<String> getStrengths() {
		return strengths;
	}

	public void setStrengths(ArrayList<String> strengths) {
		this.strengths = strengths;
	}

	public void addStrength(String strength) {
		strengths.add(strength);
	}

}
