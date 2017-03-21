package fabianleven.cristianmilapallas.valenbisi;

/**
 * Created by Fabi on 21.03.2017.
 */

public class Parte {
    public enum Status {
        OPEN,
        IN_PROGRESS,
        CLOSED
    }

    public enum Type {
        MECHANICAL,
        ELECTRICAL,
        PAINTING,
        CONSTRUCTION
    }

    private String name;
    private String description;
    private int stationId;
    private Status status;
    private Type type;

    public Parte(String name, String description, int stationId, Status status, Type type) {
        this.name = name;
        this.description = description;
        this.stationId = stationId;
        this.status = status;
        this.type = type;
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

    public Status getStatus() {
        return status;
    }

    public Type getType() {
        return type;
    }
}
