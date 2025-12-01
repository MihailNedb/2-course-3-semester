// SpoilerDecorator.java
public class SpoilerDecorator extends VehicleDecorator {
    private String material;
    private double cost;
    private int speedIncrease;

    public SpoilerDecorator(VehicleComponent vehicle, String material, double cost, int speedIncrease) {
        super(vehicle);
        this.material = material;
        this.cost = cost;
        this.speedIncrease = speedIncrease;
    }

    @Override
    public String getDescription() {
        return decoratedVehicle.getDescription() +
                String.format(", Спойлер: %s (+%d км/ч) (+%.2f $)", material, speedIncrease, cost);
    }

    @Override
    public double getPrice() {
        return decoratedVehicle.getPrice() + cost;
    }

    @Override
    public int getMaxSpeed() {
        return decoratedVehicle.getMaxSpeed() + speedIncrease;
    }
}