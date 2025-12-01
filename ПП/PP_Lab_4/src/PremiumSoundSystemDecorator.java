// PremiumSoundSystemDecorator.java
public class PremiumSoundSystemDecorator extends VehicleDecorator {
    private String systemBrand;
    private double cost;

    public PremiumSoundSystemDecorator(VehicleComponent vehicle, String systemBrand, double cost) {
        super(vehicle);
        this.systemBrand = systemBrand;
        this.cost = cost;
    }

    @Override
    public String getDescription() {
        return decoratedVehicle.getDescription() +
                String.format(", Премиальная аудиосистема: %s (+%.2f $)", systemBrand, cost);
    }

    @Override
    public double getPrice() {
        return decoratedVehicle.getPrice() + cost;
    }
}


