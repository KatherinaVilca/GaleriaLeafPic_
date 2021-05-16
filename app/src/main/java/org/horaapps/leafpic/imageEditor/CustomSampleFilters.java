package org.horaapps.leafpic.imageEditor;

import android.graphics.Bitmap;

import com.zomato.photofilters.SampleFilters;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.imageprocessors.subfilters.SaturationSubFilter;

public class CustomSampleFilters {

    public static Filter getStarLitFilter() {

        Filter filtro = SampleFilters.getStarLitFilter();
        DecoradorFilter filtroDecorado= new DecoradorFilter(KeysFilters.STARTLITFILTER);
        filtroDecorado.addSubFilters(filtro.getSubFilters());
        return filtroDecorado;

    }

    public static Filter getBlueMessFilter() {

        Filter filtro = SampleFilters.getBlueMessFilter();
        DecoradorFilter filtroDecorado= new DecoradorFilter(KeysFilters.BLUEMESSFILTER);
        filtroDecorado.addSubFilters(filtro.getSubFilters());
        return filtroDecorado;

    }

    public static Filter getAweStruckVibeFilter() {

        Filter filtro = SampleFilters.getAweStruckVibeFilter();
        DecoradorFilter filtroDecorado= new DecoradorFilter(KeysFilters.AWESTRUCKVIBEFILTER);
        filtroDecorado.addSubFilters(filtro.getSubFilters());
        return filtroDecorado;

    }

    public static Filter getLimeStutterFilter() {

        Filter filtro = SampleFilters.getLimeStutterFilter();
        DecoradorFilter filtroDecorado= new DecoradorFilter(KeysFilters.LIMESTUTTERFILTER);
        filtroDecorado.addSubFilters(filtro.getSubFilters());
        return filtroDecorado;

    }

    public static Filter getNightWhisperFilter() {

        Filter filtro = SampleFilters.getNightWhisperFilter();
        DecoradorFilter filtroDecorado= new DecoradorFilter(KeysFilters.NIGHTWHISPERFILTER);
        filtroDecorado.addSubFilters(filtro.getSubFilters());
        return filtroDecorado;

    }

    public static Filter getBlackAndWhite(Bitmap bitmap){
        Filter filtro = new Filter();
        filtro.addSubFilter( new SaturationSubFilter(-10));
        DecoradorFilter filtroDecorado = new DecoradorFilter(KeysFilters.BLACKANDWHITEFILTER);
        filtroDecorado.addSubFilters(filtro.getSubFilters());
        return filtroDecorado;
    }

}
