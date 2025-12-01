// ArmorDecorator.java
public class ArmorDecorator extends VehicleDecorator {
    private String armorLevel;
    private double cost;
    private double weightPenalty;

    public ArmorDecorator(VehicleComponent vehicle, String armorLevel, double cost, double weightPenalty) {
        super(vehicle);
        this.armorLevel = armorLevel;
        this.cost = cost;
        this.weightPenalty = weightPenalty;
    }

    @Override
    public String getDescription() {
        return decoratedVehicle.getDescription() +
                String.format(", Бронирование: %s (-%.0f л.с.) (+%.2f $)",
                        armorLevel, weightPenalty, cost);
    }

    @Override
    public double getPrice() {
        return decoratedVehicle.getPrice() + cost;
    }

    @Override
    public double getEnginePower() {
        // Броня уменьшает эффективную мощность из-за веса
        return Math.max(0, decoratedVehicle.getEnginePower() - weightPenalty);
    }
}