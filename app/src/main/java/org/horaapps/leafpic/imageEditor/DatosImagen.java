package org.horaapps.leafpic.imageEditor;

import android.graphics.Bitmap;

public class DatosImagen {

    private int brilloActual;
    private int contrasteActual;
    private int saturacionActual;
    private Bitmap im;
    //private Bitmap imagenOriginal;

    public DatosImagen(int brillo, int contraste, int saturacion, Bitmap ac){
        brilloActual=brillo;
        contrasteActual=contraste;
        saturacionActual=saturacion;
        im=ac;
    }

    public void setImagen(Bitmap v){
        im=v;
    }

    public void setBrillo(int brillo){   // 80 y ahora quiero 20    20 es mayor a 80 no. Entonces brilloactusal es 20-80= -40
/**
        if( brillo> brilloActual || brilloActual==brillo)
            brilloActual=brillo;
        else brilloActual = brillo-brilloActual;
*/
        brilloActual=brillo;
    }

    public void setContraste(int contraste){
        contrasteActual=contraste/01;
    }

    public void setSaturacion(int saturacion){
        if(saturacion>10) {
            saturacionActual = saturacion / 10;
        }
        else saturacionActual =saturacion;
    }

    public Bitmap getImagen(){
        return im.copy(Bitmap.Config.ARGB_8888,true);
    }

    public int getBrillo(){
        return brilloActual;
    }

    public int getContraste(){
        return contrasteActual;
    }

    public int getSaturacion(){
        return saturacionActual;
    }



}
