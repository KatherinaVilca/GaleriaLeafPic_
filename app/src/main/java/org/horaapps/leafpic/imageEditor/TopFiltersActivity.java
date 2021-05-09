package org.horaapps.leafpic.imageEditor;

/**
public class TopFiltersActivity  extends ThemedActivity {

    private String filter1,filter2,filter3;
    private ImageView filter1,filter2,filter3;
    private ImageView show_Image;
    private CommandEditor comand;
    private Bitmap bitmap_actual;

    static {
        System.loadLibrary("NativeImageProcessor");
    }


    private void init_valores(){
        filter1= getIntent().getExtras().getString("FILTER1");
        filter2 = getIntent().getExtras().getString("FILTER2");
        filter3 = BitmapFactory.decodeFile(image_path);
    }

}
*/