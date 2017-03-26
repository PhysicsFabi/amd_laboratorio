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

    public void setId(String id) {
        this.id = id;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStationId(int stationId) {
        this.stationId = stationId;
    }

    public void setStatus(STATUS status) {
        this.status = status;
    }

    public void setType(TYPE type) {
        this.type = type;
    }
}
