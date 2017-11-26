package fr.benjo.ramenstagram.views;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

import fr.benjo.ramenstagram.R;
import fr.benjo.ramenstagram.RamenstagramApp;
import fr.benjo.ramenstagram.databinding.ActivityRamenListBinding;
import fr.benjo.ramenstagram.models.Node;
import fr.benjo.ramenstagram.services.DownloadPicturesService;
import fr.benjo.ramenstagram.utils.mvvm.Command1;
import fr.benjo.ramenstagram.viewmodels.InstagramNodeViewModel;
import fr.benjo.ramenstagram.viewmodels.InstagramNodesViewModel;

public class RamenListActivity extends AppCompatActivity {

    public static final String RAMEN_TAG = "ramen";

    private InstagramNodesViewModel mViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new InstagramNodesViewModel(RamenstagramApp.getInstance().getInstagramApi(), RAMEN_TAG, new Command1<InstagramNodeViewModel>() {
            @Override
            public void execute(InstagramNodeViewModel vm) {
                Node node = vm.getNode();

                Intent i = new Intent(RamenListActivity.this, FullscreenImageActivity.class);
                i.putExtra(FullscreenImageActivity.CAPTION, node.getCaption());
                i.putExtra(FullscreenImageActivity.IMAGE_URL, node.getDisplaySrc());
                i.putExtra(FullscreenImageActivity.PLACEHOLDER_COLOR, vm.getPrimaryColor());
                startActivity(i);
            }
        });

        ActivityRamenListBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_ramen_list);
        binding.setVm(mViewModel);

        mViewModel.canDownload.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                invalidateOptionsMenu();
            }
        });

        mViewModel.layoutMode.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                invalidateOptionsMenu();
            }
        });

        mViewModel.errorMessage.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                Toast.makeText(RamenListActivity.this, mViewModel.errorMessage.get(), Toast.LENGTH_LONG).show();
            }
        });

        askForPermission();
    }

    public void askForPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {


            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    42);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar, menu);

        menu.getItem(0).setIcon(isListLayout() ? R.drawable.ic_grid_on_white_24dp : R.drawable.ic_view_list_white_24dp);
        menu.getItem(1).setVisible(mViewModel != null && mViewModel.canDownload.get());
        return true;
    }

    private boolean isListLayout() {
        return InstagramNodesViewModel.LAYOUT_MODE_LIST.equals(mViewModel.layoutMode.get());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.download_all:
                startDownloadService();
                return true;
            case R.id.toggle_layout:
                mViewModel.onToggleMode();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void startDownloadService() {
        Intent serviceIntent = new Intent(this, DownloadPicturesService.class);

        ArrayList<String> urlsToDownload = new ArrayList<>();

        for (InstagramNodeViewModel vm : mViewModel.ramenList) {
            urlsToDownload.add(vm.getNode().getDisplaySrc());
        }

        serviceIntent.putExtra("urls", urlsToDownload);

        startService(serviceIntent);
    }
}
