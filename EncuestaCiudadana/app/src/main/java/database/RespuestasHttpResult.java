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
public class RespuestasHttpResult {
    Context context;
    OnCompleteJSON completeJSON;
    public interface OnCompleteJSON{
        void onCompleteJSON(int status);
    }
    public RespuestasHttpResult(Context context){
        this.context = context;
    }

    public void setOnCompleteJSON(OnCompleteJSON onCompleteJSON){
        this.completeJSON = onCompleteJSON;
    }
    public void get(){

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Constantes.SERVIDOR+"getRespuestas.php", new JsonHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                completeJSON.onCompleteJSON(0);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                try{
                    RespuestasDs data = new RespuestasDs(context);
                    data.openDatabase();
                    for(int x = 0; x < response.length(); x++){
                        data.insertRespuestas(jsonToRespuesta(response.getJSONObject(x)));
                    }
                    data.close();
                    completeJSON.onCompleteJSON(1);
                }catch(Exception ex){

                }
            }
        });
    }

    public Respuesta jsonToRespuesta(JSONObject jsonObject){
        Respuesta respuesta = new Respuesta();
        try {
            respuesta.setId(jsonObject.getInt("id"));
            respuesta.setTexto(jsonObject.getString("texto"));
            respuesta.setIdPregunta(jsonObject.getInt("id_pregunta"));
            respuesta.setValor(jsonObject.getInt("valor"));
            respuesta.setIndice(jsonObject.getInt("indice"));
        }catch (JSONException ex){
            return  respuesta;
        }
        return respuesta;
    }
}
