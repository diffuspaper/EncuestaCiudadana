package diffusvio.encuestaciudadana;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import java.lang.reflect.Array;
import java.util.ArrayList;

import database.Respuesta;

/**
 * Created by Sistemas on 14/04/2015.
 */
public class RespuestasCheckbox extends LinearLayout{
    private Context context;
    private ArrayList<Respuesta> respuestas;

    public RespuestasCheckbox(Context context) {
        super(context);
        this.context = context;
        inicializar();
    }

    public RespuestasCheckbox(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        inicializar();
    }

    public void inicializar(){
        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.respuestas_checkbox, this, true);
    }

    public ArrayList<Respuesta> getRespuestas() {
        return respuestas;
    }

    public void setRespuestas(ArrayList<Respuesta> respuestas) {
        this.respuestas = respuestas;
    }
}
