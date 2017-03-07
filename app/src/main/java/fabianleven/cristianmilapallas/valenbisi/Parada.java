package fabianleven.cristianmilapallas.valenbisi;

import java.io.Serializable;

/**
 * Created by Fabi on 21.02.2017.
 */

public class Parada implements Serializable {
    private static final long serialVersionUID = -2004949158290397639L;

    public class Coordinates implements Serializable {
        private static final long serialVersionUID = 3222357328377646295L;
        public double longitude;
        public double latitude;

        public Coordinates(double longitude, double latitude) {
            this.longitude = longitude;
            this.latitude = latitude;
        }
    }
    public String name;
    public int number;
    public String address;
    public String totalSlots;
    public String freeSlots;
    public String availableBikes;
    public Coordinates coordinates;

    public Parada(
            String name,
            int number,
            String address,
            String totalSlots,
            String freeSlots,
            String availableBikes,
            double latitude,
            double longitude) {
        this.name = name;
        this.number = number;
        this.address = address;
        this.totalSlots = totalSlots;
        this.freeSlots = freeSlots;
        this.availableBikes = availableBikes;
        this.coordinates = new Coordinates(latitude, longitude);
    }
}
