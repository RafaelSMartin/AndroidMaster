package com.rafaels.edittexttuneado;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

import java.util.Vector;

/**
 * Created by Rafael S Martin on 08/02/2018.
 */

public class EditTextTuneado extends android.support.v7.widget.AppCompatEditText {

    private Paint pincel;

    private Paint pincel2 = new Paint();
    private Path path = new Path();
    private Vector<String> resaltar = new Vector<String>();

    private boolean dibujarRayas;
    private int posicionNumeros;


    public EditTextTuneado(Context context, AttributeSet attrs) {
        super(context, attrs);
        float densidad = getResources().getDisplayMetrics().density;
        pincel = new Paint();
        pincel.setColor(Color.BLACK);
        pincel.setTextAlign(Paint.Align.RIGHT);
        pincel.setTextSize(12*densidad);

        pincel2.setColor(Color.YELLOW);
        pincel2.setStyle(Paint.Style.FILL);
        resaltar.add("Android");
        resaltar.add("curso");

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.EditTextTuneado, 0,0);
        //Usamos try/finally para asegurarnos en caso de error q se libera memoria
        try {
            dibujarRayas = a.getBoolean(R.styleable.EditTextTuneado_dibujarRayas, true);
            posicionNumeros = a.getInteger(R.styleable.EditTextTuneado_posicionNumeros,0);

            switch (posicionNumeros){
                case 0:
                    pincel.setTextAlign(Paint.Align.RIGHT);
                    break;
                case 1:
                    pincel.setTextAlign(Paint.Align.LEFT);
                    break;
            }
            int colorNumeros = a.getColor(R.styleable.EditTextTuneado_colorNumeros, Color.BLACK);
            pincel.setColor(colorNumeros);
            float tamanyoNumeros = a.getDimension(R.styleable.EditTextTuneado_tamanyoNumeros,
                    12*densidad);
            pincel.setTextSize(tamanyoNumeros);
        }finally {
            //Libera memoria de TypedArray q es aconsejable
            a.recycle();
        }


    }

    @Override
    protected void onDraw(Canvas canvas){
        final Layout layout = getLayout(); //distribucion de lineas
        final String texto = getText().toString(); //Texto mostrado
        for (String palabra : resaltar){
            int pos = 0;
            do{
                // localiza la pos de la sig palabra o nos devuelve -1 si no encuentra mas
                pos = texto.indexOf(palabra, pos);
                if(pos != -1){
                    // Permite obtener un area que cogeria los caracteres comprendidos
                    // entre dos posiciones y se mete en path
                    layout.getSelectionPath(pos, pos+palabra.length(), path);
                    pos++;
                    //Coloreamos el area path como dice pincel2, de amarillo
                    canvas.drawPath(path, pincel2);
                }
            } while (pos != -1);
        }

        Rect rect = new Rect();
        for(int linea = 0; linea < getLineCount(); linea++){
            int lineaBase = getLineBounds(linea, rect);
            canvas.drawLine(rect.left, lineaBase+2,
                    rect.right, lineaBase+2, pincel);
            canvas.drawText(""+(linea+1), getWidth()-2, lineaBase, pincel);
        }


        for(int linea = 0; linea<getLineCount(); linea++){
            int lineaBase = getLineBounds(linea, rect);
            if (dibujarRayas) {
                canvas.drawLine(rect.left, lineaBase+2, rect.right,
                        lineaBase+2, pincel);
            }
            switch (posicionNumeros){
                case 0:
                    canvas.drawText(""+(linea+1), getWidth()-2, lineaBase, pincel);
                    break;
                case 1:
                    canvas.drawText(""+(linea+1), 2, lineaBase, pincel);
                    break;
            }
        }

        super.onDraw(canvas);
    }

    //Manejador de eventos
    @Override
    public boolean onTouchEvent(MotionEvent evento){
        final Layout layout = getLayout();
        final String texto = getText().toString();
        //Averiguamos la linea donde se ha pulsado
        int linea = layout.getLineForVertical((int)evento.getY());
        //Averiguamos la palabra donde se ha pulsado, offset = pos
        int offset = layout.getOffsetForHorizontal(linea,evento.getX())-1;
        //Si la palabra no esta en la lista de resultado la incluimos
        String s = sacaPalabra(texto, offset);
        if(s.length() != 0 && resaltar.indexOf(s) == -1){
            resaltar.add(s);
            //Indicamos que repinte la vista
            invalidate();
            return true;
        } else{
            //Para no perder la funcionalidad de un EditText si el evento no era para nosotros
            return super.onTouchEvent(evento);
        }
    }

    private String sacaPalabra(String texto, int pos) {
        int ini = pos;
        while (ini>0 && texto.charAt(ini)!=' ' && texto.charAt(ini)!='\n') {
            ini--;
        }
        int fin = pos;
        while (fin < texto.length() && texto.charAt(fin) != ' '
                && texto.charAt(fin) != '\n') {
            fin++;
        }
        return texto.substring(ini, fin).trim();
    }


}
