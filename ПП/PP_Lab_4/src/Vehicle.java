// Vehicle.java
import java.util.*;
import java.text.SimpleDateFormat;

public class Vehicle implements VehicleComponent {
    private String id;
    private String type;
    private String model;
    private double enginePower;
    private int maxSpeed;
    private Date manufactureDate;
    private double price;
    private String additionalDescription;

    public Vehicle(String id, String type, String model, double enginePower,
                   int maxSpeed, Date manufactureDate, double price) {
        this.id = id;
        this.type = type;
        this.model = model;
        this.enginePower = enginePower;
        this.maxSpeed = maxSpeed;
        this.manufactureDate = manufactureDate;
        this.price = price;
        this.additionalDescription = "";
    }

    @Override
    public String getId() { return id; }

    @Override
    public String getDescription() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        return String.format("ID: %s, Тип: %s, Модель: %s, Мощность: %.1f л.с., " +
                        "Макс. скорость: %d км/ч, Дата выпуска: %s, Базовая цена: %.2f $%s",
                id, type, model, enginePower, maxSpeed,
                sdf.format(manufactureDate), price,
                additionalDescription.isEmpty() ? "" : ", " + additionalDescription);
    }

    @Override
    public double getPrice() { return price; }

    @Override
    public double getEnginePower() { return enginePower; }

    @Override
    public int getMaxSpeed() { return maxSpeed; }

    @Override
    public String getModel() { return model; }

    @Override
    public String getType() { return type; }

    public Date getManufactureDate() { return manufactureDate; }

    public void setId(String id) { this.id = id; }
    public void setType(String type) { this.type = type; }
    public void setModel(String model) { this.model = model; }
    public void setEnginePower(double enginePower) { this.enginePower = enginePower; }
    public void setMaxSpeed(int maxSpeed) { this.maxSpeed = maxSpeed; }
    public void setManufactureDate(Date manufactureDate) { this.manufactureDate = manufactureDate; }
    public void setPrice(double price) { this.price = price; }
    public void setAdditionalDescription(String desc) { this.additionalDescription = desc; }

    @Override
    public String toString() {
        return getDescription();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Vehicle vehicle = (Vehicle) obj;
        return Objects.equals(id, vehicle.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}