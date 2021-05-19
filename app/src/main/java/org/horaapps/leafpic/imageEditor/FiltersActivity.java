package org.horaapps.leafpic.imageEditor;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
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
    private ImageView show_img, original, filter1, filter2, filter3, filter4, filter5, filter6, filter7;
    private String album_path;

    private Bitmap bitmapImagen;
    private String image_path;
    private CommandEditor comand;
    private CommandEditor comandFilters;
    private LinkedList<Filter> listaFiltros;
    private Bitmap redimencionImagen;

    private SharedPreferencesFilters sharedPreferencesFilters;

    static {
        System.loadLibrary("NativeImageProcessor");
    }

    public void init_valores() {

        album_path = getIntent().getExtras().getString("EXTRA_ALBUM_PATH");
        image_path = getIntent().getExtras().getString("EXTRA_IMAGE_PATH");

    }

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

        show_img.setImageURI(Uri.parse(image_path));

        initFilters();

        button_apply.setOnClickListener(view -> {

            String filename = String.format("%d.jpg", System.currentTimeMillis());
            File outfile = new File(album_path, filename);

            BitmapDrawable d2 = (BitmapDrawable) show_img.getDrawable();
            Bitmap bitmap = d2.getBitmap();
            Bitmap bitmap_a_modificar= bitmap.copy(Bitmap.Config.ARGB_8888,true);
            comand = new ApplyFilter(bitmap_a_modificar, outfile, image_path);

            sharedPreferencesFilters.aumentarEnUno(filtro_actual);

            Intent data = new Intent();
            comand.execute();
            HistorialObserver.getInstance().push(comand);
            data.putExtra("new_file", outfile.getPath());
            setResult(Activity.RESULT_OK, data);

            onBackPressed();

        });


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

        CustomSampleFilters filtros = new CustomSampleFilters();
        LinkedList<ImageView> views = getImageViews();
        listaFiltros = filtros.getListaFiltros();

        initOriginal();


        int width = original.getDrawable().getIntrinsicWidth();
        int height = original.getDrawable().getIntrinsicHeight();

        //Bitmap resizedBitmap = Bitmap.createScaledBitmap( decodificar(image_path)  , width,height, false);
        Bitmap resizedBitmap = decodificar(image_path,width,height);
        initMiniaturaFiltros(views,listaFiltros, resizedBitmap,views.size());

        for(int i=0; i<views.size() ;i++){

             ImageView imageView = views.get(i);
             DecoradorFilter filtro = (DecoradorFilter) listaFiltros.get(i);

            imageView.setOnClickListener(view -> {

                comandFilters = new ShowFilter(show_img, filtro, decodificar(image_path, show_img.getWidth(), show_img.getHeight()));
                comandFilters.execute();
                filtro_actual = filtro.getNombre();
            });

        }

        for( int g=0; g<views.size(); g++){
            views.get(g).setVisibility(View.VISIBLE);
        }
    }

    private void initOriginal(){

        original.setImageURI(Uri.parse(image_path));
        original.setOnClickListener( view -> show_img.setImageURI(Uri.parse(image_path) ));
    }

    private void initMiniaturaFiltros(LinkedList<ImageView> listaViews, LinkedList<Filter> listaFiltros, Bitmap resizedBitmap, int limite) {

        CommandEditor com;

        for(int i=0; i<limite; i++){

            DecoradorFilter d= (DecoradorFilter) listaFiltros.get(i);
            com= new ShowFilter(listaViews.get(i) ,d ,resizedBitmap);
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
        LinkedList<Filter> listaFiltros= new CustomSampleFilters().getListaFiltros();
        LinkedList<ImageView> views = getImageViews();
        ComparadorValores comparator = new ComparadorValores(sharedPreferencesFilters);
        listaNombresFiltros.sort(comparator);


        int i=0;
        int f = sharedPreferencesFilters.getContador();


        for ( ; i<f && i<3; i++) {

            DecoradorFilter filtrof = buscarFiltro(listaNombresFiltros.get(i), listaFiltros);
            ImageView imageView = views.get(i);
            CommandEditor c= new ShowFilter(imageView,filtrof,decodificar(image_path, imageView.getWidth(), imageView.getHeight()));
            c.execute();

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                   System.out.println(filtrof.getNombre());
                    comand = new ShowFilter(show_img, filtrof, decodificar(image_path,show_img.getWidth(), show_img.getHeight()));
                    comand.execute();
                }
            });
        }
        int k= i;
        for(; k< views.size(); k++){
            views.get(k).setVisibility(View.GONE);
        }
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

    private Bitmap decodificar(String image_path, int w,int h){
        Resize r= new Resize();
        return r.decodeSampledBitmapFromResource(image_path, w, h);
        /**
        BitmapFactory.Options options = new BitmapFactory.Options();
        return BitmapFactory.decodeFile(image_path ,options);
         */
    }

    @Override
    public void onBackPressed(){

        comand= new Vacio(null,null);
        HistorialObserver.getInstance().push(comand);
        super.onBackPressed();
    }
}

