package org.horaapps.leafpic.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
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

import java.io.File;
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
    private Bitmap bitmapOriginal;
    private String new_path;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate(Bundle savedInstanceState) {

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Button buttonFilters;
        Button buttonCrop;
        Button buttonSave;
        Button buttonDeshacer;
        Button buttonSettings;

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
        //historial= new Stack<CommandEditor>();
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

        buttonDeshacer.setOnClickListener(view -> { if (historial != null) undo(); }); // VER. Si voy a una activity secundaria y no hago nada (por mas que antes si haya echo algo), se rompe. Historial is a null reference-

        buttonSettings.setOnClickListener(view -> {

            Intent settingsIntent= new Intent(EditActivity.this.getApplicationContext(), ImageSettingsActivity.class);
            settingsIntent.putExtra("EXTRA_ALBUM_PATH",albumPath);
            settingsIntent.putExtra("EXTRA_IMAGE_PATH",imagenPath);
            startActivityForResult(settingsIntent,request_code_filters_Activity);
        });

        img.setOnTouchListener(new View.OnTouchListener(){

            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction())
                {
                    case MotionEvent.ACTION_DOWN :
                        img.setImageBitmap(bitmapOriginal);
                        break;
                    case MotionEvent.ACTION_UP :
                        img.setImageURI(Uri.parse(imagenPath));
                        break;
                }
                return true;
            }
        });

    }

    public void init_valores() {

        albumPath= getIntent().getExtras().getString("EXTRA_ALBUM_PATH");
        imagenPath = getIntent().getExtras().getString("EXTRA_IMAGE_PATH");

        if(bitmapOriginal == null){
            setOriginalBitmap(imagenPath);
        }
    }

    private void setOriginalBitmap(String imagenPath){

        bitmapOriginal = BitmapFactory.decodeFile(imagenPath);
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

            new_path = data.getStringExtra("new_file");
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

       if(new_path != null){

            File f= new File(new_path);
            f.delete();
       }

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