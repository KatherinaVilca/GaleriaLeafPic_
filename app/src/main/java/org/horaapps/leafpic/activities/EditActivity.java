package org.horaapps.leafpic.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.ImageView;

import org.horaapps.leafpic.R;
import org.horaapps.leafpic.imageEditor.CommandEditor;
import org.horaapps.leafpic.imageEditor.CutActivity;
import org.horaapps.leafpic.imageEditor.FiltersActivity;
import org.horaapps.leafpic.imageEditor.HistorialObserver;
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
    private String newPath;
    private String pathOriginal;

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

        init_valores();
        setSupportActionBar(toolbar);

        toolbar = findViewById(R.id.toolbar);
        buttonFilters = findViewById(R.id.button_filters);
        buttonCrop = findViewById(R.id.button_crop);
        buttonSave = findViewById(R.id.button_save);
        buttonDeshacer = findViewById(R.id.button_deshacer);

        img = findViewById(R.id.edit_imag);

        img.setImageURI(Uri.parse(imagenPath));

        carpetaTemporales();
        observer=obtenerObservador();
        HistorialObserver.getInstance().addObserver(observer);

        buttonFilters.setOnClickListener(view -> {

            BitmapDrawable d2 = (BitmapDrawable) img.getDrawable();
            Bitmap bitmap = d2.getBitmap();
            System.out.println("en edit: "+bitmap.getWidth()+" "+bitmap.getHeight());

            Intent editionIntent = new Intent(getApplicationContext(), FiltersActivity.class);
            editionIntent.putExtra("EXTRA_IMAGE_PATH",imagenPath);

            startActivityForResult(editionIntent,request_code_filters_Activity);

        });
        buttonSave.setOnClickListener(view -> {

            new SaveImage(EditActivity.this.getApplicationContext(),imagenPath,albumPath).execute();
            HistorialObserver.getInstance().deleteObserver(observer);
            HistorialObserver.getInstance().destruir();
            anular();
            onBackPressed();

        });

        buttonCrop.setOnClickListener(view -> {

           Intent cutIntent= new Intent(EditActivity.this.getApplicationContext(), CutActivity.class);
           cutIntent.putExtra("EXTRA_PATH",imagenPath);
           startActivityForResult(cutIntent,request_code_filters_Activity);

        });

        buttonDeshacer.setOnClickListener(view -> undo() );

        img.setOnTouchListener((v, event) -> {

            switch(event.getAction())
            {
                case MotionEvent.ACTION_DOWN :
                    img.setImageURI(Uri.parse(pathOriginal));
                    break;
                case MotionEvent.ACTION_UP :
                    img.setImageURI(Uri.parse(imagenPath));
                    break;
            }
            return true;
        });

    }

    public void init_valores() {

        albumPath= getIntent().getExtras().getString("EXTRA_ALBUM_PATH");
        imagenPath = getIntent().getExtras().getString("EXTRA_IMAGE_PATH");
        pathOriginal = imagenPath;
    }


    private void undo() {

        String path;

        if ( historial == null || historial.isEmpty() ) {

            return;
        }

        CommandEditor command = HistorialObserver.getInstance().pop();

            while( !historial.isEmpty() &&!command.getCommandValido() ) {
                command= HistorialObserver.getInstance().pop();
            }

            if (command == null || !command.getCommandValido() ) {

                return;
            }

            path = command.deshacer();
            img.setImageURI(Uri.parse(path));
                toUpdatePath(path);
    }



    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (data != null && resultCode == RESULT_OK) {

            newPath = data.getStringExtra("new_file");
            Bitmap bitmap = BitmapFactory.decodeFile(newPath);
            img.setImageBitmap(bitmap);
            toUpdatePath(newPath);

        }
    }

    private void toUpdatePath(String path){
        imagenPath=path;
    }

   @Override
   public void onBackPressed(){

       super.onBackPressed();
       HistorialObserver.getInstance().deleteObserver(observer);
       HistorialObserver.getInstance().destruir();
       anular();

       if(newPath != null){

            File f= new File(newPath);
            f.delete();
       }


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

        if(newPath != null){

            File f= new File(newPath);
            f.delete();
        }
    }

    public Observer obtenerObservador(){

        return (observable, o) -> historial=  (Stack<CommandEditor>) o;
    }

    private void anular(){
       historial = null;
       observer = null;
    }

    private void carpetaTemporales() {

        File file = Environment.getExternalStorageDirectory();
        File dir = new File(file.getPath() + "/Ediciones");

        if (dir.exists()) {
            return;
        }

         dir.mkdir();
    }




}