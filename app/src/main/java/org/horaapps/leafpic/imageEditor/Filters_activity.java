package org.horaapps.leafpic.imageEditor;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.zomato.photofilters.SampleFilters;

import org.horaapps.leafpic.R;
import org.horaapps.liz.ThemedActivity;

import java.io.File;

public class Filters_activity extends ThemedActivity {

    private Button button_all_filters;
    private Button button_top_filters;
    private Button button_apply;
    private Button button_back;
    private TextView title;

    private String filtro_actual;


    private ImageView show_img, filter1, filter2, filter3, filter4, filter5;
    private String album_path;

    private Bitmap bitmap_actual;
    private String image_path;
    private CommandEditor comand;
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

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_edit);
        init_valores();
        button_all_filters = findViewById(R.id.button_all_filters);
        button_apply = findViewById(R.id.button_apply);
        button_top_filters = findViewById(R.id.button_top_filters);
        button_back = findViewById(R.id.button_back);

        title = findViewById(R.id.title1);
        show_img = findViewById(R.id.show_img);

        show_img.setImageBitmap(bitmap_actual);

        filter1 = findViewById(R.id.filter10);
        filter2 = findViewById(R.id.filter20);
        filter3 = findViewById(R.id.filter30);
        filter4 = findViewById(R.id.filter40);
        filter5 = findViewById(R.id.filter50);

        initFilters();

        button_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String filename = String.format("%d.jpg", System.currentTimeMillis());
                File outfile = new File(album_path, filename);
                comand = new ApplyFilter(show_img, outfile, image_path);

//                sharedPreferencesFilters.aumentarEnUno(filtro_actual);

                Intent data = new Intent();
                comand.execute();
                HistorialObserver.getInstance().push(comand);
                data.putExtra("new_file", outfile.getPath());
                setResult(Activity.RESULT_OK, data);

                onBackPressed();
            }
        });

        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        button_top_filters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //initTopFilters();
            }
        });

        button_all_filters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initFilters();
            }
        });
    }

    public void initFilters() {
        filter1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comand = new ShowFilter(show_img, SampleFilters.getNightWhisperFilter(), bitmap_actual);
                comand.execute();
                filtro_actual = KeysFilters.NIGHTWHISPERFILTER;
            }
        });

        filter2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comand = new ShowFilter(show_img, SampleFilters.getBlueMessFilter(), bitmap_actual);
                comand.execute();
                filtro_actual = KeysFilters.BLUEMESSFILTER;
            }
        });

        filter3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                comand = new ShowFilter(show_img, SampleFilters.getLimeStutterFilter(), bitmap_actual);
                comand.execute();
                filtro_actual = KeysFilters.LIMESTUTTERFILTER;
            }
        });

        filter4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                comand = new ShowFilter(show_img, SampleFilters.getStarLitFilter(), bitmap_actual);
                comand.execute();
                filtro_actual = KeysFilters.STARTLITFILTER;
            }
        });

        filter5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                comand = new ShowFilter(show_img, SampleFilters.getAweStruckVibeFilter(), bitmap_actual);
                comand.execute();
                filtro_actual = KeysFilters.AWESTRUCKVIBEFILTER;
            }
        });

    }

/**
    @RequiresApi(api = Build.VERSION_CODES.N)
    private () {
        List<String> lista = KeysFilters.listaFiltros;
        int mayor = 0;
        ComparadorValores comparator = new ComparadorValores(sharedPreferencesFilters);
        lista.sort(comparator);

        for (int i = 0; i < 3; i++) {
            lista.get(i);
        }
    }

    private void initTopFilters(){

        For(String h: lista){
            h // nombre del filtro.
            Filtro filtro = getFiltro(h)

                    generar un image
        }
    }
    */
}
