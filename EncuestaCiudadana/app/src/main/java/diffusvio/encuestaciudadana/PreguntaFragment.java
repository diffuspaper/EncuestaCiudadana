package diffusvio.encuestaciudadana;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.util.*;
import java.util.ArrayList;

import database.Pregunta;
import database.PreguntasDs;
import database.Respuesta;
import database.RespuestasDs;

/**
 * Created by Sistemas on 09/08/2015.
 */
public class PreguntaFragment extends Fragment {
    ArrayList<Integer> selecteds = new ArrayList<>();
    Pregunta mPregunta;

    static PreguntaFragment newInstance(int id){
        PreguntaFragment preguntaFragment = new PreguntaFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("idPregunta", id);
        preguntaFragment.setArguments(bundle);
        return preguntaFragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.pregunta, container, false);
    }
    public ArrayList<Integer>  getSelecteds(){
        return selecteds;
    }
    public void updateViewContent(int id, ArrayList<Integer> values){
        Activity root = getActivity();
        selecteds = new ArrayList<>();
        if(values != null) {
            selecteds = values;
            Log.i("TAGDEBUG", "SE REEMPLAZARON LOS VALORES");
        }
        PreguntasDs preguntasDs = new PreguntasDs(root);
        preguntasDs.openDatabase();
        mPregunta = preguntasDs.getPreguntaById(id);
        preguntasDs.close();

        TextView lblPregunta = (TextView)root.findViewById(R.id.lblPregunta);
        lblPregunta.setTypeface(null, Typeface.BOLD);
        lblPregunta.setText(mPregunta.getTexto());
        RespuestasDs respuestasDs = new RespuestasDs(root);

        respuestasDs.openDatabase();
        ArrayList<Respuesta> respuestas = respuestasDs.getRespuestasByPregunta(mPregunta.getId());

        LinearLayout view = (LinearLayout)root.findViewById(R.id.viewRespuestas);
        view.removeAllViews();
        for(Integer vals: selecteds){
            Log.i("TAGDEBUG","VALORES PASADOS : "+ vals);
        }
        if(mPregunta.getTipo() == 1){
            final RadioGroup radioGroup = new RadioGroup(root);
            RadioButton radioSelected = null;
            for (Respuesta respuesta : respuestas) {
                RadioButton radioButton = new RadioButton(root);
                radioButton.setTextColor(getResources().getColor(R.color.colorTexto));
                radioButton.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                radioButton.setText(respuesta.getTexto());
                radioButton.setTag(respuesta.getId());
                radioButton.setTextSize(14);
                radioButton.setTypeface(null, Typeface.BOLD);
                if(selecteds.contains(respuesta.getId())){
                    radioSelected = radioButton;
                }
                radioGroup.addView(radioButton);
            }
            view.addView(radioGroup);
            if(radioSelected != null)
                radioGroup.check(radioSelected.getId());
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    selecteds.clear();
                    RadioButton button = (RadioButton) group.findViewById(checkedId);
                    selecteds.add((Integer) button.getTag());
                }
            });

        }else{
            for (Respuesta respuesta : respuestas) {
                CheckBox checkBox = new CheckBox(root);
                checkBox.setText(respuesta.getTexto());
                checkBox.setTextSize(14);
                checkBox.setTextColor(getResources().getColor(R.color.colorTexto));
                checkBox.setTag(respuesta.getId());
                checkBox.setTypeface(null, Typeface.BOLD);
                if(selecteds.contains(respuesta.getId())){
                    checkBox.setChecked(true);
                }
                checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CheckBox mCheckBox = (CheckBox) v;
                        if (mCheckBox.isChecked()) {
                            selecteds.add((Integer) mCheckBox.getTag());
                        } else {
                            selecteds.remove((Integer)mCheckBox.getTag());
                        }
                    }
                });
                view.addView(checkBox);
            }
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getActivity().getIntent().getExtras();
    }
}
