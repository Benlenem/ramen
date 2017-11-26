package fr.benjo.ramenstagram.viewmodels;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

/**
 * Created by ben on 26/11/2017.
 */

public class FullscreenImageViewModel {

    public ObservableField<String> caption;
    public ObservableField<String> pictureUrl;
    public ObservableField<Integer> placeHolderColor;
    public ObservableBoolean isLoadedFromCache;

    public FullscreenImageViewModel(String caption, String pictureUrl, Integer placeHolderColor) {
        this.caption = new ObservableField<>(caption);
        this.pictureUrl = new ObservableField<>(pictureUrl);
        this.placeHolderColor = new ObservableField<>(placeHolderColor);
    }
}
