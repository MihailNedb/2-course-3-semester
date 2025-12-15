// NavigationSystemDecorator.java
public class NavigationSystemDecorator extends VehicleDecorator {
    private String systemName;
    private double cost;

    public NavigationSystemDecorator(VehicleComponent vehicle, String systemName, double cost) {
        super(vehicle);
        this.systemName = systemName;
        this.cost = cost;
    }

    @Override
    public String getDescription() {
        return decoratedVehicle.getDescription() +
                String.format(", Навигационная система: %s (+%.2f $)", systemName, cost);
    }

    @Override
    public double getPrice() {
        return decoratedVehicle.getPrice() + cost;
    }
}