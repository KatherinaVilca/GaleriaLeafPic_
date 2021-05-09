package org.horaapps.leafpic.imageEditor;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.horaapps.leafpic.activities.EditActivity;
import org.horaapps.leafpic.data.StorageHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class SaveImage extends CommandEditor{

    protected String album_path;
    protected  String path;
    private EditActivity editor;
    public SaveImage(EditActivity editor, String path,String album_path){

        super(null,null);
        this.editor=editor;
        this.album_path=album_path;
        this.path=path;
    }

    public void execute(){

        FileOutputStream outputStream= null;
        String filename= String.format("%d.jpg",System.currentTimeMillis());
        File outfile= new File(album_path,filename);

        try {
            outputStream= new FileOutputStream(outfile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        BitmapFactory.decodeFile(path).compress(Bitmap.CompressFormat.JPEG,100,outputStream);
        StorageHelper.copyFile(editor.getApplicationContext(), outfile, new File(album_path));
    }
}
