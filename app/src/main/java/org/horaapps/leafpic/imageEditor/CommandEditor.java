package org.horaapps.leafpic.imageEditor;

import android.support.annotation.Nullable;

import org.horaapps.liz.ThemedActivity;

import java.io.File;

public abstract class CommandEditor extends ThemedActivity {

    protected String previusImagePath;
    protected File fileAnterior;

    public CommandEditor(@Nullable String p, @Nullable File file_an){
        previusImagePath=p;
        fileAnterior=file_an;
    }

    public String deshacer(){

        if (fileAnterior!= null) {

            fileAnterior.delete();
        }
        return previusImagePath;
    }

    protected void setPath(String p){

        previusImagePath=p;
    }

    public boolean getCommandValido(){
        return true;
    }

    protected abstract void execute();
}
