package org.horaapps.leafpic.imageEditor;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.zomato.photofilters.imageprocessors.ImageProcessor;

import org.horaapps.leafpic.R;
import org.horaapps.liz.ThemedActivity;

public class ImageSettingsActivity extends ThemedActivity{
    private SeekBar seekBar_Brillo, seekBar_Contraste,seekBar_Saturacion;
    //private TextView
    private ImageView show_Image;
    private String image_path,album_path;
    private CommandEditor comand;
    private Bitmap bitmap_act;
    private  Bitmap bitmap_a_modificar;
    private DatosImagen datos;

    static {
        System.loadLibrary("NativeImageProcessor");
    }

    private void init_valores(){
        album_path= getIntent().getExtras().getString("EXTRA_ALBUM_PATH");
        image_path = getIntent().getExtras().getString("EXTRA_IMAGE_PATH");
        bitmap_act = BitmapFactory.decodeFile(image_path);
    }
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_setting);
        init_valores();
        seekBar_Brillo= findViewById(R.id.SeekBar_brillo);
        seekBar_Contraste= findViewById(R.id.SeekBar_contraste);
        seekBar_Saturacion= findViewById(R.id.SeekBar_saturacion);
        show_Image= findViewById(R.id.imageView);
        show_Image.setImageBitmap(bitmap_act);

        bitmap_a_modificar= bitmap_act.copy(Bitmap.Config.ARGB_8888,true);
        datos= new DatosImagen(0,0,0,bitmap_a_modificar);

        System.out.println("Cuantas veces se ejecutara esto? ");
        seekBar_Brillo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                datos.setBrillo(i);
                Bitmap in= ImageProcessor.doBrightness(datos.getBrillo(),datos.getImagen());
                show_Image.setImageBitmap(in);
                datos.setImagen(in);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
            }
        });

        seekBar_Contraste.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                datos.setContraste(i);
                Bitmap in= ImageProcessor.doContrast(datos.getContraste(),datos.getImagen());
                show_Image.setImageBitmap(in);
                datos.setImagen(in);
                //Bitmap im=datos.getImagen();
               // show_Image.setImageBitmap( ImageProcessor.doContrast(datos.getContraste(),im) );

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
              //  bitmap_act= bitmap_a_modificar.copy(Bitmap.Config.ARGB_8888,true);
                //bitmap_act=bitmap_a_modificar;
            }
        });

        seekBar_Saturacion.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                System.out.println(i);
                datos.setSaturacion(i);
                Bitmap in= ImageProcessor.doSaturation(datos.getImagen(),datos.getSaturacion());
                show_Image.setImageBitmap(in);
                datos.setImagen(in);

               // Bitmap im=datos.getImagen();
               // show_Image.setImageBitmap( ImageProcessor.doSaturation(im,datos.getSaturacion()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }

}
