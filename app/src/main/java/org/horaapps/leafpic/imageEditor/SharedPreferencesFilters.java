package org.horaapps.leafpic.imageEditor;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesFilters {

    private final SharedPreferences sharedPref;


    public SharedPreferencesFilters(Activity activity){

        sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
    }
    public void setEstadistica(String nombreFiltro, int cantidad){

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(nombreFiltro,cantidad);
        editor.apply();
    }

    public int getEstadistica(String nombreFiltro){

        return sharedPref.getInt(nombreFiltro,0);
    }

    public void aumentarEnUno(String nombreFiltro){
        int cant=getEstadistica(nombreFiltro);
        setEstadistica(nombreFiltro,++cant);
    }
}
