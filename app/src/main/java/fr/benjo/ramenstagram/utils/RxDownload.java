package fr.benjo.ramenstagram.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

/**
 * Created by ben on 26/11/2017.
 */

public class RxDownload {

    public static Observable<Long> writeResponseBodyToOutputStream(final ResponseBody body, final OutputStream outputStream) {
        return Observable.create(emitter -> {
            try {
                InputStream inputStream = null;

                try {
                    byte[] fileReader = new byte[4096];
//                        long fileSize = body.contentLength();
                    long fileSizeDownloaded = 0;
                    inputStream = body.byteStream();

                    while (true) {
                        int read = inputStream.read(fileReader);

                        if (read == -1) {
                            break;
                        }

                        outputStream.write(fileReader, 0, read);
                        fileSizeDownloaded += read;
                        //Log.v("", "file download: " + fileSizeDownloaded + " of " + fileSize);
                    }

                    emitter.onNext(fileSizeDownloaded);

                    outputStream.flush();
                } finally {
                    if (inputStream != null) {
                        inputStream.close();
                    }

                    if (outputStream != null) {
                        outputStream.close();
                    }
                }
            } catch (IOException e) {
                emitter.onError(e);
            }

            emitter.onComplete();
        });
    }
}
