// VehicleDecorator.java
public abstract class VehicleDecorator implements VehicleComponent {
    protected VehicleComponent decoratedVehicle;

    public VehicleDecorator(VehicleComponent decoratedVehicle) {
        this.decoratedVehicle = decoratedVehicle;
    }

    @Override
    public String getId() {
        return decoratedVehicle.getId();
    }

    @Override
    public String getDescription() {
        return decoratedVehicle.getDescription();
    }

    @Override
    public double getPrice() {
        return decoratedVehicle.getPrice();
    }

    @Override
    public double getEnginePower() {
        return decoratedVehicle.getEnginePower();
    }

    @Override
    public int getMaxSpeed() {
        return decoratedVehicle.getMaxSpeed();
    }

    @Override
    public String getModel() {
        return decoratedVehicle.getModel();
    }

    @Override
    public String getType() {
        return decoratedVehicle.getType();
    }
}