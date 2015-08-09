package diffusvio.encuestaciudadana;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import java.lang.reflect.Array;


public class Information extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);



        Spinner spinner = (Spinner)findViewById(R.id.spinnerSexo);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new String[]{"Hombre","Mujer"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Spinner spinnerPartidos = (Spinner)findViewById(R.id.spinnerPartidos);
        ArrayAdapter<String> adapterPartidos = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new String[]{"PAS","PRI","PAN","PRD"});
        adapterPartidos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPartidos.setAdapter(adapter);
    }

    public void continuar(View view){


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_information, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
