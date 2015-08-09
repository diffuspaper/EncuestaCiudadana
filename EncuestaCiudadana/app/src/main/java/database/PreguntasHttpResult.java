package database;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

        client.get(Constantes.SERVIDOR+"getPreguntas.php", new JsonHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        completeJSON.onCompleteJSON(0);
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        try{
                            PreguntasDs data = new PreguntasDs(context);
                            data.openDatabase();
                            for(int x = 0; x < response.length(); x++){
                                data.insertPreguntas(jsonToPregunta(response.getJSONObject(x)));
                            }
                            data.close();
                            completeJSON.onCompleteJSON(1);
                        }catch(Exception ex){

                        }
                    }
                });
    }

    public Pregunta jsonToPregunta(JSONObject jsonObject){
        Pregunta pregunta = new Pregunta();
        try {
            pregunta.setId(jsonObject.getInt("id"));
            pregunta.setTexto(jsonObject.getString("texto"));
            pregunta.setTipo(jsonObject.getInt("tipo"));
            pregunta.setIdPreguntaPadre(jsonObject.getInt("id_pregunta_padre"));
            pregunta.setIdRespuestaPadre(jsonObject.getInt("id_respuesta_padre"));
            pregunta.setIndice(jsonObject.getInt("indice"));
        }catch (JSONException ex){
            return  pregunta;
        }
        return pregunta;
    }
}
