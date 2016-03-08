package com.lowermainlandpharmacyservices.lmpsformulary;

public class GenericExcludedDrug extends GenericDrug {
	public StringBuilder criteria;

	public GenericExcludedDrug(String genericName, String brandName,
			String criteria) {
		super(genericName, brandName, "Excluded");
		this.criteria = new StringBuilder(criteria);
	}

	public String getCriteria() {
		return criteria.toString();
	}

	public void additionalCriteria(String extraCriteria) {
		StringBuilder extraAddition = new StringBuilder();
		String punctutations = ".,':;<>/=()-";

		// ignore numbers
		if (Character.isDigit(extraCriteria.charAt(0))) {
			extraAddition.append(extraCriteria.charAt(0));
			extraAddition.append(".  ");
		}
		// add bullet
		else if (!(extraCriteria.contains(":") || extraCriteria.contains("OR")
				&& extraCriteria.length() <= 3)) {
			extraAddition.append("    -   ");
		}

		// find the first letter
		int j = 0;
		boolean foundFirstLetter = false;
		for (int i = 0; i < extraCriteria.length(); i++) {
			if (!(foundFirstLetter)) {
				if (!(Character.isAlphabetic(extraCriteria.charAt(i)))) {
					j++;
				} else
					foundFirstLetter = true;
			}
		}

		// append all from first letter till end
		char charToAdd;
		for (int k = j; k < extraCriteria.length(); k++) {
			charToAdd = extraCriteria.charAt(k);
			if (Character.isLetterOrDigit(charToAdd)
					|| Character.isWhitespace(charToAdd)
					|| Character.isSpaceChar(charToAdd)
					|| punctutations.contains(String.valueOf(charToAdd)))
				extraAddition.append(extraCriteria.charAt(k));
			else
				extraAddition.append("'");
		}
		this.criteria.insert(criteria.length(),
				("\n\n" + extraAddition.toString()));
	}
}
