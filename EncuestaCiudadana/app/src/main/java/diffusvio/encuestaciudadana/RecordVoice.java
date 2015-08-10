package diffusvio.encuestaciudadana;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import database.Encuesta;
import database.EncuestaDs;


public class RecordVoice extends ActionBarActivity {

    private MediaRecorder myAudioRecorder;
    private String outputFile = null;
    private Button start, finish;
    private TextView lblTime;
    int counter = 5;
    int uniqueId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_voice);
        uniqueId = getIntent().getExtras().getInt("unique_id");

        start = (Button)findViewById(R.id.btnStart);
        finish = (Button)findViewById(R.id.btnFinish);
        finish.setEnabled(false);
        File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/encuesta/voices");
        dir.mkdirs();

        File voice = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/encuesta/voices/"+uniqueId+".3gp");
        try {
            voice.createNewFile();
        }catch (IOException ex){
            Log.i("ERRORM", ex.getMessage());
        }
        outputFile = Environment.getExternalStorageDirectory().
                getAbsolutePath() + "/encuesta/voices/"+uniqueId+".3gp";
        try {
            myAudioRecorder = new MediaRecorder();
            myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
            myAudioRecorder.setOutputFile(outputFile);
        }catch(Exception ex){

        }

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    myAudioRecorder.prepare();
                    myAudioRecorder.start();
                    EncuestaDs encuestaDs = new EncuestaDs(RecordVoice.this);
                    encuestaDs.openDatabase();
                    Encuesta encuesta = encuestaDs.getEncuesta(uniqueId);
                    encuesta.setArchivoVoz(outputFile);
                    encuestaDs.updateEncuesta(encuesta);
                    encuestaDs.close();

                    Intent intent = new Intent(RecordVoice.this, PreguntaActivity.class);
                    intent.putExtra("indice", 1);
                    intent.putExtra("unique_id", uniqueId);


                   /* Intent intent = new Intent(RecordVoice.this, PreguntaActivity.class);*/
                    startActivity(intent);

                } catch (IllegalStateException e) {
                    Log.i("ERRORM", e.getMessage());
                } catch (IOException e) {
                    Log.i("ERRORM", e.getMessage());
                }
                start.setEnabled(false);
                finish.setEnabled(true);
                Toast.makeText(getApplicationContext(), "Comenzo la encuesta", Toast.LENGTH_LONG).show();
            }
        });
    }
    public void stop(View view){
        try {
            myAudioRecorder.stop();
            myAudioRecorder.release();
            myAudioRecorder = null;
            finish.setEnabled(false);
        }catch(Exception ex){

        }
        Toast.makeText(getApplicationContext(), "Ha terminado la encuesta. Gracias.",Toast.LENGTH_LONG).show();
        EncuestaDs encuestaDs = new EncuestaDs(this);
        encuestaDs.openDatabase();
        Encuesta encuesta = encuestaDs.getEncuesta(uniqueId);
        encuesta.setStatus(2);
        encuestaDs.updateEncuesta(encuesta);
        encuestaDs.close();
        Log.i("Finalizo", encuesta.getPosicionLat() + " - " +encuesta.getPosicionLgt());
        finish();
    }

    private void startCounter(){
    }

    @Override
    public void onBackPressed() {
        return ;
    }
}
