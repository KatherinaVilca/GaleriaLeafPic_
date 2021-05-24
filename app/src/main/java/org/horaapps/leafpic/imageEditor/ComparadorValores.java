package org.horaapps.leafpic.imageEditor;

import java.util.Comparator;

public class ComparadorValores implements Comparator<String> {

    private final SharedPreferencesFilters s;

    public ComparadorValores(SharedPreferencesFilters sharedPreferencesFilters){

        s=sharedPreferencesFilters;
    }
    @Override
    public int compare(String valor1, String valor2) {

        int estadistica1= s.getEstadistica( valor1);
        int estadistica2= s.getEstadistica( valor2);

        return Integer.compare(estadistica2, estadistica1);
    }
}
