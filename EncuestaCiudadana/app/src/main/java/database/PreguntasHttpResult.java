package database;

import android.content.Context;
import android.content.SharedPreferences;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.*;
/**
 * Created by Sistemas on 14/04/2015.
 */
public class PreguntasHttpResult {
    Context context;
    OnCompleteJSON completeJSON;
    public interface OnCompleteJSON{
        void onCompleteJSON(int status);
    }



    public PreguntasHttpResult(Context context){
        this.context = context;
    }

    public void setOnCompleteJSON(OnCompleteJSON onCompleteJSON){
        this.completeJSON = onCompleteJSON;
    }
    public void get(){
        AsyncHttpClient client = new AsyncHttpClient();
        SharedPreferences preferences = context.getSharedPreferences("encuesta", Context.MODE_PRIVATE);
        long id = preferences.getLong("id_encuesta", 0);
        PreguntasDs preguntasDs = new PreguntasDs(context);
        RespuestasDs respuestasDs = new RespuestasDs(context);

        if(id > 0) {
            respuestasDs.openDatabase();
            respuestasDs.dropTable();
            respuestasDs.close();
            preguntasDs.openDatabase();
            preguntasDs.dropTable();

            RequestParams params = new RequestParams();
            params.put("id_encuesta", id);
            client.post(Constantes.SERVIDOR + "get_preguntas_encuesta", params, new JsonHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    completeJSON.onCompleteJSON(0);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    try {
                        Log.i("TAGDEBUG", response.length()+"");
                        PreguntasDs data = new PreguntasDs(context);
                        data.openDatabase();
                        for (int x = 0; x < response.length(); x++) {
                            data.insertPreguntas(jsonToPregunta(response.getJSONObject(x)));
                        }
                        data.close();
                        completeJSON.onCompleteJSON(1);
                    } catch (Exception ex) {

                    }
                }
            });
        }
    }

    public Pregunta jsonToPregunta(JSONObject jsonObject){
        Pregunta pregunta = new Pregunta();
        try {
            pregunta.setId(jsonObject.getInt("id"));
            pregunta.setTexto(jsonObject.getString("pregunta"));
            pregunta.setTipo(jsonObject.getInt("tipo"));
            pregunta.setIdPreguntaPadre(jsonObject.getInt("id_pregunta_padre"));
            pregunta.setIdRespuestaPadre(jsonObject.getInt("id_respuesta_padre"));
            pregunta.setIndice(jsonObject.getInt("index"));
            Log.i("TAGDEBUG", pregunta.getTexto());
            RespuestasDs respuestasDs = new RespuestasDs(context);
            respuestasDs.openDatabase();
            JSONArray respuestas = jsonObject.getJSONArray("respuestas");
            for(int x = 0; x < respuestas.length(); x++){
                respuestasDs.insertRespuestas(jsonToRespuesta(respuestas.getJSONObject(x)));
            }
            respuestasDs.close();
        }catch (JSONException ex){
            Log.i("TAGDEBUG", ex.getMessage());
            return  pregunta;
        }
        return pregunta;
    }

    public Respuesta jsonToRespuesta(JSONObject jsonObject){
        Respuesta respuesta = new Respuesta();
        try {
            respuesta.setId(jsonObject.getInt("id"));
            respuesta.setTexto(jsonObject.getString("respuesta"));
            respuesta.setIdPregunta(jsonObject.getInt("id_pregunta"));
            respuesta.setValor(jsonObject.getInt("valor"));
            respuesta.setIndice(jsonObject.getInt("indice"));
        }catch (JSONException ex){
            return  respuesta;
        }
        return respuesta;
    }
}
