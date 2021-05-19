package org.horaapps.leafpic.imageEditor;

import android.support.annotation.Nullable;
import java.io.File;

public class Vacio extends CommandEditor {

    public Vacio(@Nullable String n, @Nullable File f ){
        super(n,f);
    }

    @Override
    public boolean getCommandValido(){
        return false;
    }
    protected void execute() {

    }
}
