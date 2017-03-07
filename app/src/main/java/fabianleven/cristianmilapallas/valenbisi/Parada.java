package fabianleven.cristianmilapallas.valenbisi;

import java.io.Serializable;

/**
 * Created by Fabi on 21.02.2017.
 */

public class Parada implements Serializable {
    private static final long serialVersionUID = -2004949158290397639L;
    public String name;
    public int number;
    public String address;

    public Parada(String name, int number, String address) {
        this.name = name;
        this.number = number;
        this.address = address;
    }
}
