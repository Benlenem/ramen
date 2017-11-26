package fr.benjo.ramenstagram.utils;

import android.util.SparseArray;

/**
 * Created by benjamin on 04/05/2017.
 */

public interface ItemBinding {
    Integer getBindingItemVariable();

    SparseArray<Object> getAdditionalVariables();
}
