package diffusvio.encuestaciudadana;

import android.content.Intent;
import android.graphics.Color;
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

import java.util.ArrayList;
import database.EncuestaRespuestas;
import database.EncuestaRespuestasDs;
import database.Pregunta;
import database.PreguntasDs;
import database.Respuesta;
import database.RespuestasDs;


public class PreguntaActivity extends ActionBarActivity {


    ArrayList<Integer> selecteds = new ArrayList<>();
    Pregunta mPregunta;
    int uniqueId;
    int indice = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pregunta);
        Bundle bundle = getIntent().getExtras();

        indice = bundle.getInt("indice");
        uniqueId = bundle.getInt("unique_id");
        Log.i("UniqueId", ""+uniqueId);
        Log.i("Indice", ""+ indice);

        final PreguntasDs preguntasDs = new PreguntasDs(this);
        preguntasDs.openDatabase();
        mPregunta = preguntasDs.getPreguntaByIndice(indice);
        Pregunta nextPregunta = preguntasDs.getPreguntaByIndice(indice + 1);
        preguntasDs.close();


        Button btnSiguiente = (Button) findViewById(R.id.btnSiguiente);
        Button btnFinish = (Button) findViewById(R.id.btnFinish);

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (insertSelecteds()) {
                    finishEncuesta();
                } else {
                    Toast.makeText(PreguntaActivity.this, "Por favor seleccione por lo menos una respuesta", Toast.LENGTH_LONG).show();
                }
            }
        });

        if (nextPregunta != null){

            btnFinish.setVisibility(View.GONE);
            btnSiguiente.setVisibility(View.VISIBLE);

            btnSiguiente.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (insertSelecteds()) {
                        PreguntasDs preguntasDs1 = new PreguntasDs(PreguntaActivity.this);
                        preguntasDs1.openDatabase();

                        EncuestaRespuestasDs encuestaRespuestasDs = new EncuestaRespuestasDs(PreguntaActivity.this);
                        encuestaRespuestasDs.openDatabase();
                        boolean takeDown = false;
                        while(true){
                            indice++;
                            Pregunta pregunta = preguntasDs1.getPreguntaByIndice(indice);
                            if(pregunta != null) {
                                if (pregunta.getIdPreguntaPadre() > 0) {
                                    EncuestaRespuestas encuestaRespuestas = encuestaRespuestasDs.getEncuestaRespuestaRequired(uniqueId, pregunta.getIdPreguntaPadre(), pregunta.getIdRespuestaPadre());
                                    if (encuestaRespuestas != null) {
                                        break;
                                    }
                                } else {
                                    break;
                                }
                            }else{
                                takeDown = true;
                                break;
                            }
                        }
                        if(takeDown){
                            finishEncuesta();
                        }else {
                            Intent intent = new Intent(PreguntaActivity.this, PreguntaActivity.class);
                            intent.putExtra("indice", indice);
                            intent.putExtra("unique_id", uniqueId);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        Toast.makeText(PreguntaActivity.this, "Por favor seleccione por lo menos una respuesta", Toast.LENGTH_LONG).show();
                    }
                }
            });

        }else{
            btnFinish.setVisibility(View.VISIBLE);
            btnSiguiente.setVisibility(View.GONE);
        }

        TextView lblPregunta = (TextView)findViewById(R.id.lblPregunta);
        lblPregunta.setText(mPregunta.getTexto());
        RespuestasDs respuestasDs = new RespuestasDs(this);

        respuestasDs.openDatabase();
        ArrayList<Respuesta> respuestas = respuestasDs.getRespuestasByPregunta(mPregunta.getId());

        LinearLayout view = (LinearLayout)findViewById(R.id.viewRespuestas);
        if(mPregunta.getTipo() == 1){

            RadioGroup radioGroup = new RadioGroup(this);
            for (Respuesta respuesta : respuestas) {
                RadioButton radioButton = new RadioButton(this);
                radioButton.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                radioButton.setText(respuesta.getTexto());
                radioButton.setTag(respuesta.getId());
                radioButton.setTextSize(12);
                radioButton.setTextColor(Color.WHITE);
                radioGroup.addView(radioButton);
            }
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    selecteds.clear();
                    RadioButton button = (RadioButton)group.findViewById(checkedId);
                    selecteds.add((Integer)button.getTag());
                }
            });
            view.addView(radioGroup);
        }else{
            for (Respuesta respuesta : respuestas) {
                CheckBox checkBox = new CheckBox(this);
                checkBox.setText(respuesta.getTexto());
                checkBox.setTextSize(12);
                checkBox.setTextColor(Color.WHITE);
                checkBox.setTag(respuesta.getId());
                checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CheckBox mCheckBox = (CheckBox)v;
                        if(mCheckBox.isChecked()){
                            selecteds.add((Integer)mCheckBox.getTag());
                        }else {
                            selecteds.remove((Integer)mCheckBox.getTag());
                        }
                    }
                });
                view.addView(checkBox);
            }
        }


    }
    void finishEncuesta(){
        finish();
    }
    boolean insertSelecteds(){
        if(selecteds.size() > 0) {
            EncuestaRespuestasDs ds = new EncuestaRespuestasDs(this);
            ds.openDatabase();
            long inserted = 0;
            for (Integer value : selecteds) {
                EncuestaRespuestas encuestaRespuestas = new EncuestaRespuestas();
                encuestaRespuestas.setIdRespuesta(value);
                encuestaRespuestas.setIdPregunta(mPregunta.getId());
                encuestaRespuestas.setUnique_id(uniqueId);
                inserted = ds.insertEncuestaRespuestas(encuestaRespuestas);
                Log.i("Inserted respuesta" , ""+inserted);
            }
            ds.close();
            return true;
        }else{
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        return ;
    }


}
