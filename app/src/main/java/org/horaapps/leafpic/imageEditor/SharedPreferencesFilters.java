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

        SharedPreferences.Editor editor = sharedPref.edit();
        int valor = sharedPref.getInt(nombreFiltro,0);
        editor.putInt(nombreFiltro, valor+1);
        int con = sharedPref.getInt("Contador",0);
        editor.putInt("Contador",con+1);
        editor.apply();
    }

    public int getContador(){
     return  getEstadistica("Contador");
    }
}
