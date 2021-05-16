package org.horaapps.leafpic.imageEditor;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import com.yalantis.ucrop.UCrop;

import org.horaapps.liz.ColorPalette;
import org.horaapps.liz.ThemedActivity;

import java.io.File;

public class CutActivity extends ThemedActivity{

    private String imagePath;

    public void onCreate(Bundle savedInstanceState) {

        CommandEditor comand;
        super.onCreate(savedInstanceState);
        imagePath = getIntent().getExtras().getString("EXTRA_PATH");

        execute();
        comand= new Cut(imagePath);
        comand.execute();
        HistorialObserver.getInstance().push(comand);
    }

    public boolean execute() {

        Uri mDestinationUri = Uri.fromFile(new File(getCacheDir(), "croppedImage.png"));
        Uri uri = Uri.fromFile(new File(imagePath));
        UCrop uCrop = UCrop.of(uri, mDestinationUri);
        uCrop.withOptions(getUcropOptions());
        uCrop.start(CutActivity.this);
        return true;
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (data != null && resultCode == RESULT_OK) {
            switch (requestCode) {
                case UCrop.REQUEST_CROP:

                    Uri imageUri = UCrop.getOutput(data);
                    Intent dataReturn= new Intent();
                    dataReturn.putExtra("new_file",imageUri.getPath());
                    setResult(Activity.RESULT_OK, dataReturn);
                    break;

                default:
                    super.onActivityResult(requestCode, resultCode, data);
                    break;
            }
        }
        onBackPressed();
    }

     @SuppressWarnings("ResourceAsColor")
     private UCrop.Options getUcropOptions() {

         UCrop.Options options = new UCrop.Options();

         options.setCompressionFormat(Bitmap.CompressFormat.PNG);
         options.setCompressionQuality(90);
         options.setActiveWidgetColor(getAccentColor());
         options.setToolbarColor(getPrimaryColor());
         options.setStatusBarColor(isTranslucentStatusBar() ? ColorPalette.getObscuredColor(getPrimaryColor()) : getPrimaryColor());
         options.setCropFrameColor(getAccentColor());
         options.setFreeStyleCropEnabled(true);

         return options;
     }


}
