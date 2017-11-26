package fr.benjo.ramenstagram.api;

import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import fr.benjo.ramenstagram.utils.RxDownload;
import io.reactivex.Observable;
import okhttp3.ResponseBody;

/**
 * Created by ben on 26/11/2017.
 */

public class InstagramDiskCache {

    public boolean imageExistsInCache(String url) {
        File f = getFileForPicture(url);
        return f.exists();
    }

    public Observable<Long> saveImage(ResponseBody body, String url) {
        File pictureFile = getFileForPicture(url);
        try {
            return RxDownload.writeResponseBodyToOutputStream(body, new FileOutputStream(pictureFile));
        } catch (FileNotFoundException e) {
            return Observable.error(e);
        }
    }

    @Nullable
    public File getImageFile(String url) {
        return imageExistsInCache(url) ? getFileForPicture(url) : null;
    }

    @NonNull
    private File getFileForPicture(String url) {
        File downloadsFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        return new File(downloadsFolder, keyForUrl(url) + ".png");
    }

    private String keyForUrl(String url) {
        return String.valueOf(url.hashCode());
    }
}
