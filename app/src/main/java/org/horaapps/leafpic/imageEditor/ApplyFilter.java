package org.horaapps.leafpic.imageEditor;

import android.graphics.Bitmap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class ApplyFilter extends CommandEditor {

    private File outfile;
    private Bitmap bitmap;

    public ApplyFilter(Bitmap bitmap, File outfile, String path) {

        super(path, outfile);
        this.outfile = outfile;
        this.bitmap = bitmap;
 }

    public void execute() {

        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(outfile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

    }
}
