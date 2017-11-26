package fr.benjo.ramenstagram.api;

import fr.benjo.ramenstagram.models.TagDetail;
import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Url;

/**
 * Created by ben on 26/11/2017.
 */

public interface InstagramApi {
    @GET("/explore/tags/{tag}/?__a=1")
    Observable<TagDetail> getTagDetail(@Path("tag") String tag);

    @GET
    Observable<ResponseBody> downloadPicture(@Url String url);
}
