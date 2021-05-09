package org.horaapps.leafpic.imageEditor;

import android.support.annotation.Nullable;

import org.horaapps.liz.ThemedActivity;

import java.io.File;

public abstract class CommandEditor extends ThemedActivity {

    protected String previusImagePath;
    protected File file_anterior;

    public CommandEditor(@Nullable String p, @Nullable File file_an){
        previusImagePath=p;
        file_anterior=file_an;
    }

    public String deshacer(){
        if (file_anterior!= null) {
            file_anterior.delete();
        }
        return previusImagePath;
    }

    protected void setPath(String p){
        previusImagePath=p;
    }
    public abstract void execute();
}
