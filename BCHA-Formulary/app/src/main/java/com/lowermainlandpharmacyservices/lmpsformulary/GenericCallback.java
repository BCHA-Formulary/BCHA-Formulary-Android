package com.lowermainlandpharmacyservices.lmpsformulary;

/**
 * Created by Kelvin on 2017-09-09.
 */

public interface GenericCallback <T, E> {
    void onSuccess(T object);
    void onFailure(E object);
}
