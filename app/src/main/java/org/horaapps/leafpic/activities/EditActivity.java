package org.horaapps.leafpic.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;

import org.horaapps.leafpic.R;
import org.horaapps.leafpic.activities.base.SharedMediaActivity;
import org.horaapps.leafpic.adapters.MediaPagerAdapter;
import org.horaapps.leafpic.animations.DepthPageTransformer;
import org.horaapps.leafpic.fragments.BaseMediaFragment;
import org.horaapps.leafpic.imageEditor.CommandEditor;
import org.horaapps.leafpic.imageEditor.CutActivity;
import org.horaapps.leafpic.imageEditor.Filters_activity;
import org.horaapps.leafpic.imageEditor.HistorialObserver;
import org.horaapps.leafpic.imageEditor.ImageSettingsActivity;
import org.horaapps.leafpic.imageEditor.SaveImage;
import org.horaapps.leafpic.util.AnimationUtils;
import org.horaapps.leafpic.util.Measure;
import org.horaapps.leafpic.views.HackyViewPager;

import java.util.Observable;
import java.util.Observer;
import java.util.Stack;

import butterknife.BindView;

//import java.io.File;

public class EditActivity extends SharedMediaActivity implements BaseMediaFragment.MediaTapListener {

    private Toolbar toolbar;
    private ImageView img;

    private Button button_filters;
    private Button button_crop;
    private Button button_save;
    private  Button button_desahacer;
    private Button button_settings;
    private int position;

    private static final String TAG = EditActivity.class.getSimpleName();
    private String album_path;
    private String imagen_path;

    private EditActivity editor = this;
    private boolean fullScreenMode, customUri = false;
    private Bitmap b;
    private static final int request_code_filters_Activity = 2;

    private HistorialObserver historial_observer;
    private Stack<CommandEditor> historial;
    private CommandEditor command;
    private Observer observer;

    private RecyclerView rvEdicion;
    @BindView(R.id.photos_pager)
    HackyViewPager mViewPager;
    private MediaPagerAdapter adapter;

    static {
        System.loadLibrary("NativeImageProcessor");
    }

    public void init_valores() {
        album_path= getIntent().getExtras().getString("EXTRA_ALBUM_PATH");
        imagen_path = getIntent().getExtras().getString("EXTRA_IMAGE_PATH");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        init_valores();
        setSupportActionBar(toolbar);
        toolbar = findViewById(R.id.toolbar);
        button_filters = findViewById(R.id.button_filters);
        button_crop = findViewById(R.id.button_crop);
        button_save = findViewById(R.id.button_save);
        button_desahacer = findViewById(R.id.button_deshacer);
        button_settings=findViewById(R.id.button_settings);

        img = findViewById(R.id.edit_imag);
        img.setImageURI(Uri.parse(imagen_path));

        historial_observer= HistorialObserver.getInstance();
        observer=obtenerObservador();
        historial_observer.addObserver(observer);



        button_filters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent editionIntent = new Intent(getApplicationContext(), Filters_activity.class);
                editionIntent.putExtra("EXTRA_ALBUM_PATH",album_path);
                editionIntent.putExtra("EXTRA_IMAGE_PATH",imagen_path);
                startActivityForResult(editionIntent,request_code_filters_Activity);

            }
        });
        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                command=new SaveImage(editor,imagen_path,album_path);
                onBackPressed();
            }
        });

        button_crop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               Intent cutIntent= new Intent(editor.getApplicationContext(), CutActivity.class);
               cutIntent.putExtra("EXTRA_PATH",imagen_path);
               startActivityForResult(cutIntent,request_code_filters_Activity);
            }
        });

        button_desahacer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                undo();
            }
        });

        button_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent settingsIntent= new Intent(editor.getApplicationContext(), ImageSettingsActivity.class);
                settingsIntent.putExtra("EXTRA_ALBUM_PATH",album_path);
                settingsIntent.putExtra("EXTRA_IMAGE_PATH",imagen_path);
                startActivityForResult(settingsIntent,request_code_filters_Activity);
            }
        });
    }

    private void initUi() {

        setSupportActionBar(toolbar);
        toolbar.bringToFront();
        toolbar.setNavigationIcon(getToolbarIcon(GoogleMaterial.Icon.gmd_arrow_back));
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        setupSystemUI();

        getWindow().getDecorView().setOnSystemUiVisibilityChangeListener
                (visibility -> {
                    if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) showSystemUI();
                    else hideSystemUI();
                });

        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(position);

        /// useImageMenu = isCurrentMediaImage(); NO SE PORQE OBTIENE LA IMGAEN

        mViewPager.setPageTransformer(true, AnimationUtils.getPageTransformer(new DepthPageTransformer()));


        if (((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay().getRotation() == Surface.ROTATION_90) {
            Configuration configuration = new Configuration();
            configuration.orientation = Configuration.ORIENTATION_LANDSCAPE;
            onConfigurationChanged(configuration);
        }

    }

    private void hideSystemUI() {
        runOnUiThread(new Runnable() {
            public void run() {
                toolbar.animate().translationY(-toolbar.getHeight()).setInterpolator(new AccelerateInterpolator())
                        .setDuration(200).start();

                getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
                    @Override
                    public void onSystemUiVisibilityChange(int visibility) {
                        Log.wtf(TAG, "ui changed: " + visibility);
                    }
                });
                getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                                | View.SYSTEM_UI_FLAG_IMMERSIVE);

                //fullScreenMode = true;
                //changeBackGroundColor();
            }
        });
    }

    private void setupSystemUI() {
        toolbar.animate().translationY(Measure.getStatusBarHeight(getResources())).setInterpolator(new DecelerateInterpolator())
                .setDuration(0).start();
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    private void showSystemUI() {
        runOnUiThread(new Runnable() {
            public void run() {
                toolbar.animate().translationY(Measure.getStatusBarHeight(getResources())).setInterpolator(new DecelerateInterpolator())
                        .setDuration(240).start();

                getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                // fullScreenMode = false;
                //changeBackGroundColor();
            }
        });
    }

    private void undo() {
        String new_path;

        if (!historial.isEmpty()) {

            CommandEditor command = historial.pop();

            if (command != null) {
                new_path= command.deshacer();
                System.out.println("El path de la imagen anterior"+ new_path);
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
        imagen_path=path;
    }

    @Override
    public void onViewTapped() {
        {
            toggleSystemUI();
        }
    }

    public void toggleSystemUI() {
        if (fullScreenMode) showSystemUI();
        else hideSystemUI();
    }
    /**
    public static void startActivity(@NonNull Context context,
                                     @Nullable Parcelable album,
                                     @Nullable Serializable media,
                                     int position) {

        Intent intent = new Intent(context, SingleMediaActivity.class);
        intent.putExtra(EXTRA_ARGS_ALBUM, album);
        intent.putExtra(EXTRA_ARGS_MEDIA, media);
        context.startActivity(intent);
    }
    */

    @Override
    public void onResume() {
        super.onResume();
        observer=obtenerObservador();
        historial_observer.addObserver(observer);
    }

    @Override
    protected void onPause() {
        super.onPause();
        historial_observer.deleteObserver(observer);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        historial_observer.deleteObserver(observer);
    }

    public Observer obtenerObservador(){
        return new Observer() {
            @Override
            public void update(Observable observable, Object o) {
                historial=  (Stack<CommandEditor>) o;
            }
        };
    }
}