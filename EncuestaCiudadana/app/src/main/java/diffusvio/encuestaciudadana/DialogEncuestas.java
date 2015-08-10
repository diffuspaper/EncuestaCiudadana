package diffusvio.encuestaciudadana;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import database.EncuestaCiudadanaContract;
import database.EncuestaToDo;
import android.util.*;
/**
 * Created by Sistemas on 09/08/2015.
 */
public class DialogEncuestas extends DialogFragment {

    public void setEncuestaToDos(ArrayList<EncuestaToDo> encuestaToDos) {
        this.encuestaToDos = encuestaToDos;
    }
    public interface OnDialogSelection{
        void onDialogSelectionComplete(long id);
    }
    ArrayList<EncuestaToDo> encuestaToDos;
    int which = 0;
    public OnDialogSelection getSelection() {
        return selection;
    }

    public void setSelection(OnDialogSelection selection) {
        this.selection = selection;
    }

    private OnDialogSelection selection;
    public DialogEncuestas(){

    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        CharSequence[] sequence = new CharSequence[encuestaToDos.size()];

        for(int x = 0; x < encuestaToDos.size();x++){
            sequence[x] = encuestaToDos.get(x).getNombreEncuesta();
            Log.i("DEBUGENCUESTA", ""+x);
        }
        builder.setTitle("Seleccione una encuesta");
        builder.setSingleChoiceItems(sequence,0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i("WHICH", which +"");
                DialogEncuestas.this.which = which;
            }
        });
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i("WHICH", which +"");
                selection.onDialogSelectionComplete(encuestaToDos.get(DialogEncuestas.this.which).getId());
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        return builder.create();
    }

}
