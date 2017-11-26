package fr.benjo.ramenstagram;

import android.app.Application;

import fr.benjo.ramenstagram.api.InstagramApi;
import fr.benjo.ramenstagram.api.InstagramDiskCache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ben on 26/11/2017.
 */

public class RamenstagramApp extends Application {

    private static RamenstagramApp sInstance = null;

    private InstagramApi mInstagramApi;
    private InstagramDiskCache mDiskCache;

    public static RamenstagramApp getInstance() {
        return sInstance;
    }

    public InstagramApi getInstagramApi() {
        return mInstagramApi;
    }

    public InstagramDiskCache getDiskCache() {
        return mDiskCache;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = this;

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.instagram.com")
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mInstagramApi = retrofit.create(InstagramApi.class);
        mDiskCache = new InstagramDiskCache();
    }
}
