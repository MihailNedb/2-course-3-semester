import javax.swing.table.AbstractTableModel;
import java.util.List;
import java.util.ArrayList;
import java.text.SimpleDateFormat;

public class VehicleTableModel extends AbstractTableModel {
    private final String[] columnNames = {
            "ID", "Тип", "Модель", "Мощность (л.с.)",
            "Макс. скорость (км/ч)", "Дата выпуска", "Цена ($)", "Улучшения"
    };

    private List<Vehicle> vehicles;
    private List<VehicleComponent> decoratedVehicles;

    public VehicleTableModel() {
        this.vehicles = new ArrayList<>();
        this.decoratedVehicles = new ArrayList<>();
    }

    public void setVehicles(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
        fireTableDataChanged();
    }

    public void setDecoratedVehicles(List<VehicleComponent> decorated) {
        this.decoratedVehicles = decorated;
        fireTableDataChanged();
    }

    public void addVehicle(Vehicle vehicle) {
        vehicles.add(vehicle);
        fireTableRowsInserted(vehicles.size() - 1, vehicles.size() - 1);
    }

    public void removeVehicle(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < vehicles.size()) {
            vehicles.remove(rowIndex);
            fireTableRowsDeleted(rowIndex, rowIndex);
        }
    }

    public void updateVehicle(int rowIndex, Vehicle vehicle) {
        if (rowIndex >= 0 && rowIndex < vehicles.size()) {
            vehicles.set(rowIndex, vehicle);
            fireTableRowsUpdated(rowIndex, rowIndex);
        }
    }

    public Vehicle getVehicleAt(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < vehicles.size()) {
            return vehicles.get(rowIndex);
        }
        return null;
    }

    public VehicleComponent getDecoratedVehicle(String id) {
        return decoratedVehicles.stream()
                .filter(v -> v.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public boolean hasDecorations(String id) {
        return decoratedVehicles.stream()
                .anyMatch(v -> v.getId().equals(id));
    }

    public void addDecoratedVehicle(VehicleComponent decorated) {
        decoratedVehicles.removeIf(v -> v.getId().equals(decorated.getId())); // заменяем старое
        decoratedVehicles.add(decorated);
        fireTableDataChanged();
    }

    public void removeDecoratedVehicle(String id) {
        decoratedVehicles.removeIf(v -> v.getId().equals(id));
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return vehicles.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Vehicle vehicle = vehicles.get(rowIndex);
        String vehicleId = vehicle.getId();
        VehicleComponent decorated = getDecoratedVehicle(vehicleId);

        switch (columnIndex) {
            case 0: return vehicle.getId();
            case 1: return vehicle.getType();
            case 2: return vehicle.getModel();
            case 3: return (decorated != null) ? decorated.getEnginePower() : vehicle.getEnginePower();
            case 4: return (decorated != null) ? decorated.getMaxSpeed() : vehicle.getMaxSpeed();
            case 5: return new SimpleDateFormat("dd.MM.yyyy").format(vehicle.getManufactureDate());
            case 6: return (decorated != null) ? decorated.getPrice() : vehicle.getPrice();
            case 7:
                if (decorated != null) {
                    String baseDesc = vehicle.getDescription();
                    String fullDesc = decorated.getDescription();
                    String improvements = fullDesc.substring(baseDesc.length());
                    if (improvements.startsWith(", ")) {
                        improvements = improvements.substring(2);
                    }
                    return improvements.isEmpty() ? "Нет улучшений" : improvements;
                }
                return "Нет улучшений";
            default: return null;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false; // Редактирование только через диалог
    }

    public List<Vehicle> getAllVehicles() {
        return new ArrayList<>(vehicles);
    }

    public List<VehicleComponent> getAllDecoratedVehicles() {
        return new ArrayList<>(decoratedVehicles);
    }
}
