package database;

/**
 * Created by Sistemas on 09/08/2015.
 */
public class EncuestaToDo {
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombreEncuesta() {
        return nombreEncuesta;
    }

    public void setNombreEncuesta(String nombreEncuesta) {
        this.nombreEncuesta = nombreEncuesta;
    }

    long id;

    public EncuestaToDo(long id, String nombreEncuesta) {
        this.id = id;
        this.nombreEncuesta = nombreEncuesta;
    }

    String nombreEncuesta;

}
