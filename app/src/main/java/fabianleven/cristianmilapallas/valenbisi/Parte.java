package fabianleven.cristianmilapallas.valenbisi;

/**
 * Created by Fabi on 21.03.2017.
 */

public class Parte {
    public enum STATUS {
        OPEN(0),
        IN_PROGRESS(1),
        CLOSED(2);
        private int val;

        STATUS(int val) {
            this.val = val;
        }

        public int getVal() {
            return this.val;
        }
    }

    public enum TYPE {
        MECHANICAL(0),
        ELECTRICAL(1),
        PAINTING(2),
        CONSTRUCTION(3);

        private int val;

        TYPE(int val) {
            this.val = val;
        }

        public int getVal() {
            return this.val;
        }

    }

    private String id;
    private String name;
    private String description;
    private int stationId;
    private STATUS status;
    private TYPE type;

    public Parte(String id, String name, String description, int stationId, STATUS status, TYPE type) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.stationId = stationId;
        this.status = status;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getStationId() {
        return stationId;
    }

    public STATUS getStatus() {
        return status;
    }

    public TYPE getType() {
        return type;
    }
}
