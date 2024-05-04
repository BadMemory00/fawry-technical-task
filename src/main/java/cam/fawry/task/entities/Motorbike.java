package cam.fawry.task.entities;

public class Motorbike implements Vehicle {
  @Override
  public VehicleTypes type() {
    return VehicleTypes.MOTORBIKE;
  }
}
