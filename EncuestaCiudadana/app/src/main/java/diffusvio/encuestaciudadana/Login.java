package diffusvio.encuestaciudadana;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import database.Constantes;


public class Login extends ActionBarActivity {

    Button btnIniciar;
    EditText edtUsuario, edtPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        btnIniciar = (Button)findViewById(R.id.btnIniciar);
        edtUsuario = (EditText)findViewById(R.id.edtUser);
        edtPassword = (EditText)findViewById(R.id.edtPassword);
    }


    public void iniciarSesion(View view){
        if(isNetworkConnected()) {
            String usuario = edtUsuario.getText().toString();
            String password = edtPassword.getText().toString();
            if (usuario.trim().equals("")) {
                Toast.makeText(this, "Por favor ingrese el usuario", Toast.LENGTH_LONG).show();
                return;
            }
            if (password.trim().equals("")) {
                Toast.makeText(this, "Por favor ingrese la contraseña", Toast.LENGTH_LONG).show();
                return;
            }

            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            params.add("usuario", usuario);
            params.add("password", password);
            final ProgressDialog dialog = ProgressDialog.show(this, "",
                    "Inicianso sesión, espere...", true);
            dialog.show();
            client.post(Constantes.SERVIDOR + "login.php", params, new JsonHttpResponseHandler() {

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    dialog.dismiss();
                    Toast.makeText(Login.this, "Vuelva a intentarlo", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    dialog.dismiss();
                    Toast.makeText(Login.this, "Vuelva a intentarlo", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    dialog.dismiss();
                    Toast.makeText(Login.this, "Vuelva a intentarlo", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        String res = response.getString("status");
                        dialog.dismiss();
                        if (res.equals("success")) {
                            Toast.makeText(Login.this, "Bienvenido " + response.getString("usuario"), Toast.LENGTH_LONG).show();
                            Intent intent= new Intent(Login.this, MainActivity.class);
                            intent.putExtra("id_usuario", response.getInt("id_usuario"));
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(Login.this, "Usuario o contraseña incorrectos", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException ex) {
                        Toast.makeText(Login.this, "Vuelva a intentarlo", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                }
            });
        }else{
            Toast.makeText(this, "No esta conectado a internet", Toast.LENGTH_LONG).show();
        }
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
