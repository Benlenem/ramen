package fr.benjo.ramenstagram.views;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import fr.benjo.ramenstagram.R;
import fr.benjo.ramenstagram.databinding.ActivityFullscreenPictureBinding;
import fr.benjo.ramenstagram.viewmodels.FullscreenImageViewModel;

/**
 * Created by ben on 26/11/2017.
 */

public class FullscreenImageActivity extends AppCompatActivity {

    public static final String CAPTION = "CAPTION";
    public static final String IMAGE_URL = "IMAGE_URL";
    public static final String PLACEHOLDER_COLOR = "PLACEHOLDER_COLOR";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String caption = getIntent().getStringExtra(CAPTION);
        String imageUrl = getIntent().getStringExtra(IMAGE_URL);
        int placeholderColor = getIntent().getIntExtra(PLACEHOLDER_COLOR, 0);

        FullscreenImageViewModel model = new FullscreenImageViewModel(
                caption,
                imageUrl,
                placeholderColor == -1 ? null : placeholderColor);

        ActivityFullscreenPictureBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_fullscreen_picture);
        binding.setVm(model);
    }
}
