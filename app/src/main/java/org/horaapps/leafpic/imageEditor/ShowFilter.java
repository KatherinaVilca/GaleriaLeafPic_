package org.horaapps.leafpic.imageEditor;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.zomato.photofilters.imageprocessors.Filter;


public class ShowFilter extends CommandEditor{

    protected Filter filtro;
    private final Bitmap bitmap;
    private final ImageView img;
    private int width;
    private int height;

    public ShowFilter(ImageView img, Filter filter, Bitmap bitmap){

        super(null,null);
        this.img=img;
        this.filtro = filter;
        this.bitmap = bitmap;
    }

    public void execute() {

        Bitmap result = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        img.setImageBitmap( filtro.processFilter(result ));
    }

}
