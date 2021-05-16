package org.horaapps.leafpic.imageEditor;

import com.zomato.photofilters.imageprocessors.Filter;

public class DecoradorFilter extends Filter {

    private final String nombre;

    public DecoradorFilter(Filter filter, String nombre) {

        super(filter);
        this.nombre = nombre;
    }

    public DecoradorFilter(String nombre) {

        this.nombre = nombre;
    }

    public String getNombre() {

        return nombre;
    }

}
