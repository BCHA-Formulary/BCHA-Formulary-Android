package com.lowermainlandpharmacyservices.lmpsformulary.Model.Refactored;

/**
 * Created by Kelvin on 6/5/2016.
 */
public enum NameType {
    GENERIC,
    BRAND;

    public static NameType getNameType(String nametype) {
        if (nametype.equals(GENERIC.name()))
            return GENERIC;
        return BRAND;
    }
}


