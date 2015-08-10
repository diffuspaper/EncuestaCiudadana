package diffusvio.encuestaciudadana;

import android.app.SharedElementCallback;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import database.EncuestaDs;
import database.EncuestaRespuestasDs;
import database.PreguntasHttpResult;
import database.RespuestasHttpResult;


public class SplashScreen extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getSupportActionBar().hide();
       /*final ArrayList<Integer> configure = new ArrayList<>();

        if(!isConfigured()) {
            if (isNetworkConnected()) {
                EncuestaDs encuestaDs = new EncuestaDs(SplashScreen.this);
                encuestaDs.openDatabase();
                encuestaDs.dropTable();
                encuestaDs.close();

                EncuestaRespuestasDs encuestaRespuestasDs = new EncuestaRespuestasDs(SplashScreen.this);
                encuestaRespuestasDs.openDatabase();
                encuestaRespuestasDs.dropTable();
                encuestaRespuestasDs.close();

                final ExecutorService es = Executors.newCachedThreadPool();
                final ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
                ses.scheduleAtFixedRate(new Runnable() {
                    @Override
                    public void run() {
                        if (configure.size() == 2) {
                            Intent intent= new Intent(SplashScreen.this, Login.class);
                            startActivity(intent);
                            register();
                            Toast.makeText(SplashScreen.this, "Carga completa", Toast.LENGTH_LONG).show();
                            ses.shutdown();
                            finish();
                        }
                    }
                }, 0, 1, TimeUnit.SECONDS);

                PreguntasHttpResult preguntasHttpResult = new PreguntasHttpResult(this);
                    preguntasHttpResult.setOnCompleteJSON(new PreguntasHttpResult.OnCompleteJSON() {
                        @Override
                        public void onCompleteJSON(int status) {
                            if(status == 1) {
                                configure.add(1);
                            }else {
                                Toast.makeText(SplashScreen.this, "Hubo un error al descargar lo necesario. Vuelva abrir la aplicación", Toast.LENGTH_LONG).show();
                                ses.shutdown();
                                finish();
                            }
                        }
                    });
                    preguntasHttpResult.get();

                RespuestasHttpResult respuestasHttpResult = new RespuestasHttpResult(this);
                respuestasHttpResult.setOnCompleteJSON(new RespuestasHttpResult.OnCompleteJSON() {
                    @Override
                    public void onCompleteJSON(int status) {
                            if(status == 1)
                                configure.add(1);
                            else {
                                Toast.makeText(SplashScreen.this, "Hubo un error al descargar lo necesario. Vuelva abrir la aplicación", Toast.LENGTH_LONG).show();
                                ses.shutdown();
                                finish();
                            }
                        }
                    });
                    respuestasHttpResult.get();
            } else {
                Log.i("Entro","no conectado");
                Toast.makeText(this, "No se encuentra conectado a internet.", Toast.LENGTH_LONG).show();
                finish();
            }
        }else{
            Intent intent= new Intent(SplashScreen.this, Login.class);
            startActivity(intent);
            finish();
        }*/

        Intent intent= new Intent(SplashScreen.this, Login.class);
        startActivity(intent);
        finish();
    }

    private boolean isConfigured(){
        SharedPreferences sharedPreferences = getSharedPreferences("register", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("registered", false);
    }

    private void register(){
        SharedPreferences sharedPreferences = getSharedPreferences("register", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("registered", true);
        editor.commit();
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            return false;
        } else
            return true;
    }
}
