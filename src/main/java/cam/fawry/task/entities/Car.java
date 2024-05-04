package cam.fawry.task.entities;

public class Car implements Vehicle {
  @Override
  public VehicleTypes type() {
    return VehicleTypes.CAR;
  }
}
