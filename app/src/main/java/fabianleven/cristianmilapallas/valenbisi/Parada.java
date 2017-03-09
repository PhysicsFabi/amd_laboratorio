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

    /**
     * amount of total bike slots
     *
     * a value of -1 indicates that there is no data available
     */
    public int totalSlots;

    /**
     * amount of free bike slots
     *
     * a value of -1 indicates that there is no data available
     */
    public int freeSlots;

    /**
     * amount of available bike slots
     *
     * a value of -1 indicates that there is no data available
     */
    public int availableBikes;

    public Coordinates coordinates;

    public Parada(
            String name,
            int number,
            String address,
            int totalSlots,
            int freeSlots,
            int availableBikes,
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
