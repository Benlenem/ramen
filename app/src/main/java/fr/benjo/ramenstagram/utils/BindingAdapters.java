package fr.benjo.ramenstagram.utils;

import android.databinding.BindingAdapter;
import android.databinding.BindingConversion;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Downloader;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import fr.benjo.ramenstagram.RamenstagramApp;
import fr.benjo.ramenstagram.api.InstagramDiskCache;
import fr.benjo.ramenstagram.utils.mvvm.Command1;

/**
 * Created by benjamin on 04/05/2017.
 */

public class BindingAdapters {

    @BindingAdapter(value = {"bind:items", "bind:itemLayoutId", "bind:itemBinding"})
    public static <T> void setAdapter(final RecyclerView rv,
                                      final List<T> items,
                                      final int itemLayoutId,
                                      final ItemBinding itemBinding) {
        if (items != null && rv.getAdapter() == null && itemBinding != null) {
            SimpleBindingAdapter a = new SimpleBindingAdapter<>(itemBinding, items, itemLayoutId);
            rv.setAdapter(a);
        }
    }


    @BindingAdapter(value = {"bind:layoutMode"})
    public static <T> void setLayoutManager(final RecyclerView rv,
                                            final String layoutMode) {
        rv.setLayoutManager("list".equals(layoutMode) ? new LinearLayoutManager(rv.getContext()) : new GridLayoutManager(rv.getContext(), 2));
    }

    @BindingAdapter(value = {"bind:imageUrl", "bind:placeHolder", "bind:primaryColorCommand"}, requireAll = false)
    public static void loadImage(ImageView view, String imageUrl, Drawable placeHolder, Command1<Integer> primaryColorCommand) {

        InstagramDiskCache diskCache = RamenstagramApp.getInstance().getDiskCache();

        Picasso.Builder b = new Picasso.Builder(view.getContext());
        if (diskCache.imageExistsInCache(imageUrl)) {
            b.downloader(new CacheDowloader());
        }

        b.build()
                .load(imageUrl)
                .placeholder(placeHolder)
                .transform(PaletteTransformation.instance())
                .into(view, new Callback.EmptyCallback() {
                    @Override
                    public void onSuccess() {
                        if (primaryColorCommand == null) return;

                        Bitmap bitmap = ((BitmapDrawable) view.getDrawable()).getBitmap();
                        Palette palette = PaletteTransformation.getPalette(bitmap);
                        primaryColorCommand.execute(palette.getDominantColor(Color.WHITE));
                    }
                });
    }

    //http://jakewharton.com/coercing-picasso-to-play-with-palette/
    @BindingConversion
    public static ColorDrawable convertColorToDrawable(int color) {
        return color != 0 ? new ColorDrawable(color) : null;
    }

    public static class PaletteTransformation implements Transformation {
        private static final PaletteTransformation INSTANCE = new PaletteTransformation();
        private static final Map<Bitmap, Palette> CACHE = new WeakHashMap<>();

        private PaletteTransformation() {
        }

        public static PaletteTransformation instance() {
            return INSTANCE;
        }

        public static Palette getPalette(Bitmap bitmap) {
            return CACHE.get(bitmap);
        }

        @Override
        public Bitmap transform(Bitmap source) {
            Palette palette = Palette.from(source).generate();
            CACHE.put(source, palette);
            return source;
        }

        @Override
        public String key() {
            return "";
        }
    }

    public static class CacheDowloader implements Downloader {

        @Override
        public Response load(Uri uri, int networkPolicy) throws IOException {
            InstagramDiskCache cache = RamenstagramApp.getInstance().getDiskCache();
            File f = cache.getImageFile(uri.toString());
            return new Response(new FileInputStream(f), true, -1);
        }

        @Override
        public void shutdown() {
        }
    }

}
