package diffusvio.encuestaciudadana;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

import database.EncuestaRespuestas;
import database.EncuestaRespuestasDs;
import database.Pregunta;
import database.PreguntasDs;
import database.Respuesta;
import database.RespuestasDs;


public class PreguntaActivity extends ActionBarActivity {


    ArrayList<Integer> selecteds = new ArrayList<>();
    ArrayList<Pregunta> preguntas = new ArrayList<>();
    HashMap<Integer, ArrayList<Integer>> hashMap = new HashMap<>();
    int uniqueId;
    int id = 0;
    int indice = 0;
    PreguntaFragment preguntaFragment;
    Button btnSiguiente, btnAtras;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pregunta);
        Bundle bundle = getIntent().getExtras();

        PreguntasDs preguntasDs = new PreguntasDs(this);
        preguntasDs.openDatabase();
        preguntas = preguntasDs.getPreguntas();
        preguntasDs.close();

        btnSiguiente = (Button)findViewById(R.id.btnSiguiente);
        btnAtras = (Button)findViewById(R.id.btnAtras);
        btnAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                indice--;
                id = preguntas.get(indice).getId();
                setFragmentData(id);
            }
        });
        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(insertSelecteds()) {
                    indice++;
                    if (preguntas.size() > indice) {
                        Pregunta preg  = preguntas.get(indice);


                        if(preg.getIdPreguntaPadre()!= 0){
                            ArrayList<Integer> values = hashMap.get(preg.getIdPreguntaPadre());
                            if(values.contains(preg.getIdRespuestaPadre())){
                                setFragmentData(preg.getId());
                            }else{
                                v.performClick();
                            }
                        }else {
                            setFragmentData(preg.getId());
                        }
                    } else {
                        finishEncuesta();
                    }
                }else{
                    Toast.makeText(PreguntaActivity.this, "Aun no selecciona alguna respuesta", Toast.LENGTH_LONG).show();
                }
            }
        });

        uniqueId = bundle.getInt("unique_id");
        if(preguntas.size() > 0){
            id = preguntas.get(indice).getId();
            setFragmentData(id);
        }
    }
    void setFragmentData(int id){
        this.id = id;
        btnAtras.setEnabled(true);
        if(indice == 0){
            btnAtras.setEnabled(false);
        }
        preguntaFragment = (PreguntaFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_pregunta);
        if(preguntaFragment != null){
            ArrayList<Integer> values = null;
            if(hashMap.containsKey(id)) {
                values = hashMap.get(id);
                Log.i("TAGDEBUG", "La contuve " + values.size());
            }
            preguntaFragment.updateViewContent(id, values);
        }
    }
    void finishEncuesta(){
        EncuestaRespuestasDs ds = new EncuestaRespuestasDs(this);
        ds.openDatabase();
        for (Integer integer: hashMap.keySet()){
            selecteds = hashMap.get(integer);
            for(Integer value: selecteds) {
                EncuestaRespuestas encuestaRespuestas = new EncuestaRespuestas();
                encuestaRespuestas.setIdRespuesta(value);
                encuestaRespuestas.setIdPregunta(integer);
                encuestaRespuestas.setUnique_id(uniqueId);
                long inserted = ds.insertEncuestaRespuestas(encuestaRespuestas);
            }
        }
        ds.close();
        finish();
    }
    boolean insertSelecteds(){
        if(preguntaFragment != null) {
            selecteds = preguntaFragment.getSelecteds();
            if (selecteds.size() > 0) {
                hashMap.put(id, selecteds);
                return true;
            } else {
                return false;
            }
        }else{
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        return ;
    }


}
