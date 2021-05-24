package org.horaapps.leafpic.imageEditor;

import com.zomato.photofilters.SampleFilters;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.imageprocessors.subfilters.ContrastSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.SaturationSubFilter;

import java.util.LinkedList;

public class CustomSampleFilters {

    protected LinkedList<Filter> listaFiltros = new LinkedList<>();

    public static Filter getStarLitFilter() {

        Filter filtro = SampleFilters.getStarLitFilter();
        DecoradorFilter filtroDecorado = new DecoradorFilter(KeysFilters.STARTLITFILTER);
        filtroDecorado.addSubFilters(filtro.getSubFilters());
        return filtroDecorado;

    }

    public static Filter getBlueMessFilter() {

        Filter filtro = SampleFilters.getBlueMessFilter();
        DecoradorFilter filtroDecorado = new DecoradorFilter(KeysFilters.BLUEMESSFILTER);
        filtroDecorado.addSubFilters(filtro.getSubFilters());
        return filtroDecorado;

    }

    public static Filter getAweStruckVibeFilter() {

        Filter filtro = SampleFilters.getAweStruckVibeFilter();
        DecoradorFilter filtroDecorado = new DecoradorFilter(KeysFilters.AWESTRUCKVIBEFILTER);
        filtroDecorado.addSubFilters(filtro.getSubFilters());
        return filtroDecorado;

    }

    public static Filter getLimeStutterFilter() {

        Filter filtro = SampleFilters.getLimeStutterFilter();
        DecoradorFilter filtroDecorado = new DecoradorFilter(KeysFilters.LIMESTUTTERFILTER);
        filtroDecorado.addSubFilters(filtro.getSubFilters());
        return filtroDecorado;

    }

    public static Filter getNightWhisperFilter() {

        Filter filtro = SampleFilters.getNightWhisperFilter();
        DecoradorFilter filtroDecorado = new DecoradorFilter(KeysFilters.NIGHTWHISPERFILTER);
        filtroDecorado.addSubFilters(filtro.getSubFilters());
        return filtroDecorado;

    }

    public static Filter getBlackAndWhite(){

        Filter filtro = SampleFilters.getStarLitFilter();
        DecoradorFilter filtroDecorado = new DecoradorFilter(KeysFilters.BLACKANDWHITEFILTER);
        filtro.addSubFilter( new ContrastSubFilter(1f));
        filtro.addSubFilter( new SaturationSubFilter(-2));
        filtroDecorado.addSubFilters(filtro.getSubFilters());
        return filtroDecorado;

    }

    public static Filter getMoreWhiteThanBlack(){

        Filter filtro = SampleFilters.getLimeStutterFilter();
        DecoradorFilter filtroDecorado = new DecoradorFilter(KeysFilters.MOREWHITETHANBLACK);
        filtro.addSubFilter( new ContrastSubFilter(2f));
        filtro.addSubFilter( new SaturationSubFilter(-4));
        filtroDecorado.addSubFilters(filtro.getSubFilters());
        return filtroDecorado;

    }

    protected void addFilter(){

        listaFiltros.add(getBlueMessFilter());
        listaFiltros.add(getAweStruckVibeFilter());
        listaFiltros.add(getLimeStutterFilter());
        listaFiltros.add(getNightWhisperFilter());
        listaFiltros.add(getStarLitFilter());
        listaFiltros.add(getBlackAndWhite());
        listaFiltros.add(getMoreWhiteThanBlack());

    }
    public LinkedList<Filter> getListaFiltros(){

        if(listaFiltros.isEmpty()){
            addFilter();
        }
        return listaFiltros;

    }

}
