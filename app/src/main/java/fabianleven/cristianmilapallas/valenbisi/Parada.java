package fabianleven.cristianmilapallas.valenbisi;

/**
 * Created by Fabi on 21.02.2017.
 */

public class Parada {
    private String name;
    private int number;
    private String address;

    public String getName() {
        return name;
    }

    public int getNumber() {
        return number;
    }

    public String getAddress() {
        return address;
    }

    public Parada(String name, int number, String address) {
        this.name = name;
        this.number = number;
        this.address = address;
    }
}
