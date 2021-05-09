package org.horaapps.leafpic.imageEditor;

import java.util.Comparator;

public class ComparadorValores implements Comparator {

    private SharedPreferencesFilters s;

    public ComparadorValores(SharedPreferencesFilters sharedPreferencesFilters){

        s=sharedPreferencesFilters;
    }
    @Override
    public int compare(Object valor1, Object valor2) {

        int estadistica1= s.getEstadistica((String) valor1);
        int estadistica2= s.getEstadistica((String) valor2);

        if (estadistica1 > estadistica2){
            return 1;
        }
        if(estadistica1<estadistica2) {
            return -1;
        }
        return 0;
    }
}
