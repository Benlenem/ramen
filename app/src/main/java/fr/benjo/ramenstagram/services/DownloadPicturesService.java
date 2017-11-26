package fr.benjo.ramenstagram.services;

import android.app.DownloadManager;
import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.util.List;

import fr.benjo.ramenstagram.R;
import fr.benjo.ramenstagram.RamenstagramApp;
import fr.benjo.ramenstagram.api.InstagramApi;
import fr.benjo.ramenstagram.api.InstagramDiskCache;
import io.reactivex.Observable;

/**
 * Created by ben on 26/11/2017.
 */

public class DownloadPicturesService extends IntentService {

    private InstagramApi mInstagramApi;
    private InstagramDiskCache mCache;
    private NotificationCompat.Builder mBuilder;
    private NotificationManager mNotificationManager;
    private DownloadManager mDownloadManager;
    private int mCurrentDownload = 0;
    private int mDownloadSize = 0;

    public DownloadPicturesService() {
        super("DownloadPicturesService");

        mInstagramApi = RamenstagramApp.getInstance().getInstagramApi();
        mCache = RamenstagramApp.getInstance().getDiskCache();

        mBuilder = new NotificationCompat.Builder(this, "channel");
        mBuilder.setContentTitle("Download in progress")
                .setSmallIcon(R.drawable.ic_cloud_download_white_24dp);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent == null) return;

        Bundle b = intent.getExtras();
        if (b == null) return;

        mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mDownloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

        List<String> urlsToDownload = b.getStringArrayList("urls");
        mDownloadSize = urlsToDownload.size();
        if (mDownloadSize == 0) return;

        Observable.fromIterable(urlsToDownload)
                .flatMap(url -> {
                    if (mCache.imageExistsInCache(url)) {
                        return Observable.empty();
                    } else {
                        return mInstagramApi.downloadPicture(url)
                                .flatMap(body -> mCache.saveImage(body, url))
                                .doOnNext(n -> {
                                    File targetFile = mCache.getImageFile(url);

                                    if (targetFile != null) {
                                        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
                                        final String mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
                                        mDownloadManager.addCompletedDownload(
                                                targetFile.getName(),
                                                targetFile.getName(),
                                                true,
                                                mime,
                                                targetFile.getAbsolutePath(),
                                                0,
                                                false);
                                    }
                                });
                    }
                })
                .doOnNext(n -> this.notifyProgress())
                .doOnError(e -> this.notifyProgressError())
                .doOnComplete(this::notifyProgressEnd)
                .onErrorResumeNext(Observable.empty())
                .subscribe();
    }

    private void notifyProgressEnd() {
        mBuilder.setProgress(0, 0, false);
        mBuilder.setContentTitle("All pictures downloaded");
        mNotificationManager.notify(0, mBuilder.build());
    }

    private void notifyProgressError() {
        mBuilder.setProgress(0, 0, false);
        mBuilder.setContentTitle("Error while downloading pictures");
        mNotificationManager.notify(0, mBuilder.build());
    }

    private void notifyProgress() {
        mCurrentDownload++;
        mBuilder.setProgress(100, (mCurrentDownload * 100) / mDownloadSize, false);
        mNotificationManager.notify(0, mBuilder.build());
    }

}
