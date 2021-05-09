package org.horaapps.leafpic.settings;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.widget.ImageView;

import com.zomato.photofilters.SampleFilters;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.imageprocessors.subfilters.BrightnessSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.ContrastSubFilter;

import org.horaapps.leafpic.R;
import org.horaapps.leafpic.data.Album;

import butterknife.BindView;


public class PhotoEdition // extends BaseMediaFragment
 {

    @BindView(R.id.media_view)
    ImageView previewView;


    private String path;
    private Album alb;


    static {
        System.loadLibrary("NativeImageProcessor");
    }

    /**
    @NonNull
    public static PhotoEdition newInstance(@NonNull Media media) {

        //return BaseMediaFragment.newInstance(new PhotoEdition(), media);
    }
    */

    public PhotoEdition(){}

    public Bitmap añadirEfecto(Uri uri, ImageView img) {

        Filter myFilter = new Filter();

        myFilter.addSubFilter(new BrightnessSubFilter(-100));
        myFilter.addSubFilter(new ContrastSubFilter(1.1f));
        BitmapDrawable d = (BitmapDrawable) img.getDrawable();
        Bitmap bitmap = d.getBitmap();
        Bitmap imagen = bitmap.copy(Bitmap.Config.ARGB_8888, true);

        Bitmap result = myFilter.processFilter(imagen);
        return result;
    }

    public Bitmap añadirEfecto2(Uri uri, ImageView img) {

        Filter filter2 = SampleFilters.getBlueMessFilter();

        BitmapDrawable d2 = (BitmapDrawable) img.getDrawable();
        Bitmap bitmap = d2.getBitmap();
        Bitmap imagen = bitmap.copy(Bitmap.Config.ARGB_8888, true);

        Bitmap result = filter2.processFilter(imagen);
        return result;
    }

    public Bitmap añadirEfecto3(Uri uri, ImageView img) {

        Filter filter2 = SampleFilters.getLimeStutterFilter();

        BitmapDrawable d2 = (BitmapDrawable) img.getDrawable();
        Bitmap bitmap = d2.getBitmap();
        Bitmap imagen = bitmap.copy(Bitmap.Config.ARGB_8888, true);

        Bitmap result = filter2.processFilter(imagen);
        return result;
    }

    public void setAlbumImg(Album album) {
        alb = album;
    }




}
