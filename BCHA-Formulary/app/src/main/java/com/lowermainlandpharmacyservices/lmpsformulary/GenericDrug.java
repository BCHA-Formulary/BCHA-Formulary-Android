package com.lowermainlandpharmacyservices.lmpsformulary;

import java.util.ArrayList;

public class GenericDrug extends Drug {
	public String genericName;
	public ArrayList<String> brandNames;

	public GenericDrug(String genericName, String brandName, String status) {
		super(status); // superconstructor Drug

		this.genericName = genericName;

		brandNames = new ArrayList<String>();
		brandNames.add(brandName);
	}

	public boolean containsBrandName(String brand) {
		for (String brandName : brandNames) {
			if (brandName.equalsIgnoreCase(brand))
				return true;
		}
		return false;
	}

	public String getGenericName() {
		return genericName;
	}

	public void setGenericName(String generic) {
		this.genericName = generic;
	}

	public void addBrandName(String brand) {
		if (brand.equals("")) {
		} else
			brandNames.add(brand);
	}

	public ArrayList<String> getBrandNames() {
		return brandNames;
	}

}
