// TurboChargerDecorator.java
public class TurboChargerDecorator extends VehicleDecorator {
    private double powerIncrease;
    private double cost;

    public TurboChargerDecorator(VehicleComponent vehicle, double powerIncrease, double cost) {
        super(vehicle);
        this.powerIncrease = powerIncrease;
        this.cost = cost;
    }

    @Override
    public String getDescription() {
        return decoratedVehicle.getDescription() +
                String.format(", Покрытие из стали: +%.0f л.с. (+%.2f $)", powerIncrease, cost);
    }

    @Override
    public double getPrice() {
        return decoratedVehicle.getPrice() + cost;
    }

    @Override
    public double getEnginePower() {
        return decoratedVehicle.getEnginePower() + powerIncrease;
    }
}

