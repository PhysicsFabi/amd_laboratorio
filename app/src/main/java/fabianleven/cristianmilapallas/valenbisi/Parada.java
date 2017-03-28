package fabianleven.cristianmilapallas.valenbisi;

import java.io.Serializable;

public class Parada implements Serializable {
    private static final long serialVersionUID = -2004949158290397639L;

    public class Coordinates implements Serializable {
        private static final long serialVersionUID = 3222357328377646295L;
        public final double longitude;
        public final double latitude;

        public Coordinates(double longitude, double latitude) {
            this.longitude = longitude;
            this.latitude = latitude;
        }
    }
    public final String name;
    public final int number;
    public final String address;

    /**
     * amount of total bike slots
     *
     * a value of -1 indicates that there is no data available
     */
    public final int totalSlots;

    /**
     * amount of free bike slots
     *
     * a value of -1 indicates that there is no data available
     */
    public final int freeSlots;

    /**
     * amount of available bike slots
     *
     * a value of -1 indicates that there is no data available
     */
    public final int availableBikes;

    public final Coordinates coordinates;

    public int partes;

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
        this.partes = 0;
    }
}
