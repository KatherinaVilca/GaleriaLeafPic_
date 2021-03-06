package org.horaapps.leafpic.imageEditor;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.zomato.photofilters.imageprocessors.Filter;

import org.horaapps.leafpic.R;
import org.horaapps.liz.ThemedActivity;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class FiltersActivity extends ThemedActivity {

    private String filtroActual;
    private ImageView  original, filter1, filter2, filter3, filter4, filter5, filter6, filter7;
    private ImageView show_img;
    private Bitmap bitmapImagen;
    private String imagePath;
    private LinkedList<Filter> listaFiltros;
    private LinkedList<ImageView> views;

    private SharedPreferencesFilters sharedPreferencesFilters;

    static {
        System.loadLibrary("NativeImageProcessor");
    }

    public void init_valores() {

        imagePath = getIntent().getExtras().getString("EXTRA_IMAGE_PATH");

        bitmapImagen = UImage.decodeBitmap(imagePath,null);
        CustomSampleFilters filtros = new CustomSampleFilters();
        listaFiltros = filtros.getListaFiltros();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onCreate(Bundle savedInstanceState) {

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        sharedPreferencesFilters= new SharedPreferencesFilters(this);
        Button button_all_filters;
        Button button_top_filters;
        Button button_apply;

        setContentView(R.layout.activity_filters);
        init_valores();
        button_all_filters = findViewById(R.id.button_all_filters);
        button_apply = findViewById(R.id.button_apply);
        button_top_filters = findViewById(R.id.button_top_filters);

        show_img = findViewById(R.id.show_img);
        original = findViewById(R.id.Original);
        filter1 = findViewById(R.id.filter10);
        filter2 = findViewById(R.id.filter20);
        filter3 = findViewById(R.id.filter30);
        filter4 = findViewById(R.id.filter40);
        filter5 = findViewById(R.id.filter50);
        filter6 = findViewById(R.id.filter60);
        filter7 = findViewById(R.id.filter70);

        show_img.setImageBitmap(bitmapImagen);

        initFilters();

        button_apply.setOnClickListener(view -> {

            Intent data = new Intent();
            CommandEditor comand;

            String filename = String.format("%d.jpg", System.currentTimeMillis());
            File file = Environment.getExternalStorageDirectory();
            File dir = new File(file.getPath() + "/Ediciones");
            File outfile = new File(dir, filename);

            BitmapDrawable d2 = (BitmapDrawable) show_img.getDrawable();
            Bitmap bitmap = d2.getBitmap();
            Bitmap bitmap_a_modificar = bitmap.copy(Bitmap.Config.ARGB_8888, true);


            comand = new ApplyFilter(bitmap_a_modificar, outfile, imagePath);

            sharedPreferencesFilters.aumentarEnUno(filtroActual);

            comand.execute();
            HistorialObserver.getInstance().push(comand);
            data.putExtra("new_file", outfile.getPath());
            setResult(Activity.RESULT_OK, data);
            onBackPressed();
        });


        button_top_filters.setOnClickListener(view -> {

            if(sharedPreferencesFilters.getContador()>0){
                initTopFilters();
            }

        });

        button_all_filters.setOnClickListener(view -> initFilters());
    }

    public void initFilters() {

        views = getImageViews();

        initOriginal();
        initMiniaturaFiltros(views.size());

        for(int i=0; i<views.size() ;i++){

            ImageView imageView = views.get(i);
            DecoradorFilter filtro = (DecoradorFilter) listaFiltros.get(i);

            imageView.setOnClickListener(view -> {

                CommandEditor comandFilters;
                comandFilters = new ShowFilter(show_img, filtro, bitmapImagen);
                comandFilters.execute();
                filtroActual = filtro.getNombre();
            });

        }

        estadoImageView(View.VISIBLE, 0);
    }

    private void initOriginal(){

       original.setImageBitmap(decodificar(imagePath));
       original.setOnClickListener( view -> show_img.setImageURI(Uri.parse(imagePath)));

    }

    private void initMiniaturaFiltros( int limite) {

        CommandEditor com;

        for(int i=0; i<limite; i++){

            DecoradorFilter d= (DecoradorFilter) listaFiltros.get(i);
            com= new ShowFilter(views.get(i) ,d , decodificar(imagePath) );
            com.execute();
        }
    }


    private LinkedList<ImageView> getImageViews() {

        LinkedList<ImageView> views= new LinkedList<>();

        views.add(filter1);
        views.add(filter2);
        views.add(filter3);
        views.add(filter4);
        views.add(filter5);
        views.add(filter6);
        views.add(filter7);
        return views;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initTopFilters() {

        List<String> listaNombresFiltros = KeysFilters.listaNombresFiltros;
        ComparadorValores comparator = new ComparadorValores(sharedPreferencesFilters);
        listaNombresFiltros.sort(comparator);

        int i = 0;
        int f = sharedPreferencesFilters.getContador();

        for ( ; i<f && i<3; i++) {

            DecoradorFilter filtrof = buscarFiltro(listaNombresFiltros.get(i), listaFiltros);
            ImageView imageView = views.get(i);
            CommandEditor cc = new ShowFilter(imageView,filtrof,decodificar(imagePath));
            cc.execute();

            imageView.setOnClickListener(view -> {

                CommandEditor cm;
                cm = new ShowFilter(show_img,filtrof,bitmapImagen);
                cm.execute();
            });
        }

       estadoImageView(View.GONE , i);
    }

    private DecoradorFilter buscarFiltro(String nom, LinkedList<Filter> listaFiltros) {

        boolean encontre = false;
        DecoradorFilter filtroo = null;

        for (int i = 0; !encontre && i < listaFiltros.size() ; i++) {

            filtroo = (DecoradorFilter) listaFiltros.get(i);

            if (filtroo.getNombre().equals(nom)) {
                encontre = true;
            }

        }
            return filtroo;
    }

    private Bitmap decodificar(String imagePath){

        Resize r= new Resize();
        int widthMinutaruta = 248;
        int heightMiniatura = 248;

        return r.decodeSampledBitmapFromResource(imagePath, widthMinutaruta, heightMiniatura);
    }

    private void estadoImageView(int tipo , int inicio){

        for ( ; inicio< views.size(); inicio++){

            views.get(inicio).setVisibility(tipo);
        }
    }

    @Override
    public void onBackPressed(){

        CommandEditor commandVacio;
        commandVacio= new Vacio(null,null);
        HistorialObserver.getInstance().push(commandVacio);
        super.onBackPressed();
    }


}

