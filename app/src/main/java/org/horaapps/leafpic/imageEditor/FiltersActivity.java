package org.horaapps.leafpic.imageEditor;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
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

    private String filtro_actual;
    private ImageView show_img, filter1, filter2, filter3, filter4, filter5, filter6, original;
    private String album_path;

    private Bitmap bitmap_actual;
    private String image_path;
    private CommandEditor comand;
    private CommandEditor comandFilters;

    private SharedPreferencesFilters sharedPreferencesFilters;

    static {
        System.loadLibrary("NativeImageProcessor");
    }

    public void init_valores() {
        album_path = getIntent().getExtras().getString("EXTRA_ALBUM_PATH");
        image_path = getIntent().getExtras().getString("EXTRA_IMAGE_PATH");
        bitmap_actual = BitmapFactory.decodeFile(image_path);

    }

    public void onCreate(Bundle savedInstanceState) {

        Button button_all_filters;
        Button button_top_filters;
        Button button_apply;
        Button button_back;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);
        init_valores();
        button_all_filters = findViewById(R.id.button_all_filters);
        button_apply = findViewById(R.id.button_apply);
        button_top_filters = findViewById(R.id.button_top_filters);
        button_back = findViewById(R.id.button_back);

        show_img = findViewById(R.id.show_img);
        original = findViewById(R.id.Original);
        filter1 = findViewById(R.id.filter10);
        filter2 = findViewById(R.id.filter20);
        filter3 = findViewById(R.id.filter30);
        filter4 = findViewById(R.id.filter40);
        filter5 = findViewById(R.id.filter50);
        filter6 = findViewById(R.id.filter60);
        sharedPreferencesFilters= new SharedPreferencesFilters(this);
        show_img.setImageBitmap(bitmap_actual);
        initFilters();


        button_apply.setOnClickListener(view -> {

            String filename = String.format("%d.jpg", System.currentTimeMillis());
            File outfile = new File(album_path, filename);
            comand = new ApplyFilter(show_img, outfile, image_path);

            sharedPreferencesFilters.aumentarEnUno(filtro_actual);

            Intent data = new Intent();
            comand.execute();
            HistorialObserver.getInstance().push(comand);
            data.putExtra("new_file", outfile.getPath());
            setResult(Activity.RESULT_OK, data);

            onBackPressed();
        });

        button_back.setOnClickListener(view -> onBackPressed());

        button_top_filters.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {

                if(sharedPreferencesFilters.getContador()>0){
                    initTopFilters();
                }
            }
        });

        button_all_filters.setOnClickListener(view -> initFilters());
    }

    public void initFilters() {

        LinkedList<ImageView> views= getImageViews();
        LinkedList<Filter> filtros= getListaFiltros();

        initOriginal();
        initMiniaturaFiltros(views,filtros);

        for(int i=0; i<views.size() ;i++){

             ImageView op= views.get(i);
             DecoradorFilter fil=(DecoradorFilter) filtros.get(i);

            op.setOnClickListener(view -> {

                comandFilters = new ShowFilter(show_img, fil, bitmap_actual);
                comandFilters.execute();
                filtro_actual = fil.getNombre();

            });

        }
    }

    private void initOriginal(){

        original.setImageBitmap(bitmap_actual);
        original.setOnClickListener( view -> show_img.setImageBitmap(bitmap_actual) );
    }

    private void initMiniaturaFiltros(LinkedList<ImageView> listaViews, LinkedList<Filter> listaFiltros) {

        CommandEditor com;

        for(int i=0; i<listaViews.size(); i++){
            DecoradorFilter d= (DecoradorFilter) listaFiltros.get(i);

            com= new ShowFilter(listaViews.get(i) ,d ,bitmap_actual);
            com.execute();
        }
    }

    private LinkedList<Filter> getListaFiltros(){

        LinkedList<Filter> listaFiltros = new LinkedList<Filter> () ;
        listaFiltros.add(CustomSampleFilters.getBlueMessFilter());
        listaFiltros.add(CustomSampleFilters.getAweStruckVibeFilter());
        listaFiltros.add(CustomSampleFilters.getLimeStutterFilter());
        listaFiltros.add(CustomSampleFilters.getNightWhisperFilter());
        listaFiltros.add(CustomSampleFilters.getStarLitFilter());
        listaFiltros.add(CustomSampleFilters.getBlackAndWhite(bitmap_actual));
        return listaFiltros;
    }

    private LinkedList<ImageView> getImageViews() {

        LinkedList<ImageView> views= new LinkedList<ImageView>();

        views.add(filter1);
        views.add(filter2);
        views.add(filter3);
        views.add(filter4);
        views.add(filter5);
        views.add(filter6);
        return views;
    }
    
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initTopFilters() {

        List<String> lista = KeysFilters.listaFiltros;
        LinkedList<ImageView> views = getImageViews();
        ComparadorValores comparator = new ComparadorValores(sharedPreferencesFilters);
        lista.sort(comparator);

        int i;

        for ( i=0; i < 3; i++) {

            DecoradorFilter filtro= buscarFiltro(lista.get(i));
            ImageView imageView = views.get(i);


            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    comand = new ShowFilter(imageView, filtro, bitmap_actual);
                    comand.execute();
                }
            });

        }
    }

    private DecoradorFilter buscarFiltro(String nom) {

        LinkedList<Filter> lista = getListaFiltros();
        boolean encontre = false;
        DecoradorFilter filtro = null;

        for (int i = 0; i < lista.size() || !encontre; i++) {

            filtro = (DecoradorFilter) lista.get(i);
            if (filtro.getNombre().equals(nom)) {
                encontre = true;
            }

        }
            return filtro;
    }
}

