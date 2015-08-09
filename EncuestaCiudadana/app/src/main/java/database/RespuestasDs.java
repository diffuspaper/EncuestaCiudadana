package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by Sistemas on 14/04/2015.
 */
public class RespuestasDs {
    Context context;
    EncuestaCiudadanaDbHelper helper;
    SQLiteDatabase database;
    String[] seleccion = new String[]{
            EncuestaCiudadanaContract.Respuestas._ID,
            EncuestaCiudadanaContract.Respuestas.COLUMN_ID,
            EncuestaCiudadanaContract.Respuestas.COLUMN_ID_PREGUNTA,
            EncuestaCiudadanaContract.Respuestas.COLUMN_TEXTO,
            EncuestaCiudadanaContract.Respuestas.COLUMN_VALOR,
            EncuestaCiudadanaContract.Respuestas.COLUMN_INDICE
    };

    public RespuestasDs(Context context) {
        this.context = context;
        helper = new EncuestaCiudadanaDbHelper(context);
    }

    public void openDatabase(){
        database = helper.getWritableDatabase();
    }

    public void close(){
        database.close();
    }

    public long insertRespuestas(Respuesta object){
        ContentValues contentValues = new ContentValues();
        contentValues.put(EncuestaCiudadanaContract.Respuestas.COLUMN_ID,object.getId());
        contentValues.put(EncuestaCiudadanaContract.Respuestas.COLUMN_ID_PREGUNTA,object.getIdPregunta());
        contentValues.put(EncuestaCiudadanaContract.Respuestas.COLUMN_TEXTO,object.getTexto());
        contentValues.put(EncuestaCiudadanaContract.Respuestas.COLUMN_VALOR,object.getValor());
        contentValues.put(EncuestaCiudadanaContract.Respuestas.COLUMN_INDICE,object.getIndice());
        long id = database.insert(EncuestaCiudadanaContract.Respuestas.TABLE_NAME, null, contentValues);
        return id;
    }
    public ArrayList<Respuesta> getRespuestasByPregunta(int idPregunta){
        SQLiteDatabase database1 = helper.getReadableDatabase();
        ArrayList<Respuesta> respuestas = new ArrayList<>();
        Cursor cursor = database1.query(EncuestaCiudadanaContract.Respuestas.TABLE_NAME, seleccion, EncuestaCiudadanaContract.Respuestas.COLUMN_ID_PREGUNTA + " = ?", new String[]{String.valueOf(idPregunta)}, null, null, null, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            Respuesta respuesta = cursorToRespuesta(cursor);
            respuestas.add(respuesta);
            cursor.moveToNext();
        }
        return respuestas;
    }

    private Respuesta cursorToRespuesta(Cursor cursor){
        Respuesta respuesta = new Respuesta();
        respuesta.setId(cursor.getInt(1));
        respuesta.setIdPregunta(cursor.getInt(2));
        respuesta.setTexto(cursor.getString(3));
        respuesta.setValor(cursor.getInt(4));
        respuesta.setIndice(cursor.getInt(5));
        return  respuesta;
    }
}
