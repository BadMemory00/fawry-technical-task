package cam.fawry.task.entities;

public enum VehicleTypes {
    CAR("Car"),
    MOTORBIKE("Motorbike"),
    TRACTOR("Tractor"),
    EMERGENCY("Emergency"),
    DIPLOMAT("Diplomat"),
    FOREIGN("Foreign"),
    MILITARY("Military");

    private final String type;

    VehicleTypes(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
