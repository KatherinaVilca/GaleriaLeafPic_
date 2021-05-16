package org.horaapps.leafpic.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.ImageView;

import org.horaapps.leafpic.R;
import org.horaapps.leafpic.imageEditor.CommandEditor;
import org.horaapps.leafpic.imageEditor.CutActivity;
import org.horaapps.leafpic.imageEditor.FiltersActivity;
import org.horaapps.leafpic.imageEditor.HistorialObserver;
import org.horaapps.leafpic.imageEditor.ImageSettingsActivity;
import org.horaapps.leafpic.imageEditor.SaveImage;
import org.horaapps.liz.ThemedActivity;

import java.util.Observer;
import java.util.Stack;

public class EditActivity extends ThemedActivity {

    private Toolbar toolbar;
    private ImageView img;
    private String albumPath;
    private String imagenPath;
    private static final int request_code_filters_Activity = 2;
    private Stack<CommandEditor> historial;
    private Observer observer;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        Button buttonFilters;
        Button buttonCrop;
        Button buttonSave;
        Button buttonDeshacer;
        Button buttonSettings;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        init_valores();
        setSupportActionBar(toolbar);

        toolbar = findViewById(R.id.toolbar);
        buttonFilters = findViewById(R.id.button_filters);
        buttonCrop = findViewById(R.id.button_crop);
        buttonSave = findViewById(R.id.button_save);
        buttonDeshacer = findViewById(R.id.button_deshacer);
        buttonSettings=findViewById(R.id.button_settings);
        img = findViewById(R.id.edit_imag);

        img.setImageURI(Uri.parse(imagenPath));
        historial= new Stack<CommandEditor>();
        observer=obtenerObservador();
        HistorialObserver.getInstance().addObserver(observer);

        buttonFilters.setOnClickListener(view -> {

            Intent editionIntent = new Intent(getApplicationContext(), FiltersActivity.class);
            editionIntent.putExtra("EXTRA_ALBUM_PATH",albumPath);
            editionIntent.putExtra("EXTRA_IMAGE_PATH",imagenPath);
            startActivityForResult(editionIntent,request_code_filters_Activity);

        });
        buttonSave.setOnClickListener(view -> {

            new SaveImage(EditActivity.this.getApplicationContext(),imagenPath,albumPath).execute();
            HistorialObserver.getInstance().deleteObserver(observer);
            HistorialObserver.getInstance().destruir(); // solo cuando me voy de la app
            anular();
            onBackPressed();

        });

        buttonCrop.setOnClickListener(view -> {

           Intent cutIntent= new Intent(EditActivity.this.getApplicationContext(), CutActivity.class);
           cutIntent.putExtra("EXTRA_PATH",imagenPath);
           startActivityForResult(cutIntent,request_code_filters_Activity);
        });

        buttonDeshacer.setOnClickListener(view -> undo());

        buttonSettings.setOnClickListener(view -> {

            Intent settingsIntent= new Intent(EditActivity.this.getApplicationContext(), ImageSettingsActivity.class);
            settingsIntent.putExtra("EXTRA_ALBUM_PATH",albumPath);
            settingsIntent.putExtra("EXTRA_IMAGE_PATH",imagenPath);
            startActivityForResult(settingsIntent,request_code_filters_Activity);
        });
    }

    public void init_valores() {
        albumPath= getIntent().getExtras().getString("EXTRA_ALBUM_PATH");
        imagenPath = getIntent().getExtras().getString("EXTRA_IMAGE_PATH");
    }

    private void undo() {
        String new_path;

        if (!historial.isEmpty()) {

            CommandEditor command = HistorialObserver.getInstance().pop();

            if (command != null) {
                new_path= command.deshacer();
                img.setImageURI(Uri.parse(new_path));
                toUpdatePath(new_path);
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null && resultCode == RESULT_OK) {

            String new_path = data.getStringExtra("new_file");
            Bitmap bitmap = BitmapFactory.decodeFile(new_path);
            img.setImageBitmap(bitmap);
            toUpdatePath(new_path);
        }
    }

    private void toUpdatePath(String path){
        imagenPath=path;
    }

   @Override
   public void onBackPressed(){
       HistorialObserver.getInstance().deleteObserver(observer);
       HistorialObserver.getInstance().destruir(); // solo cuando me voy de la app
       anular();
       super.onBackPressed();
   }

    @Override
    public void onResume() {
        super.onResume();
        observer=obtenerObservador();
        HistorialObserver.getInstance().addObserver(observer);
    }

    @Override
    protected void onPause() {
       super.onPause();
       HistorialObserver.getInstance().deleteObserver(observer);
       anular();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        HistorialObserver.getInstance().deleteObserver(observer);
        anular();
    }

    public Observer obtenerObservador(){
        return (observable, o) -> historial=  (Stack<CommandEditor>) o;
    }

    private void anular(){
       historial = null;
       observer = null;
    }
}