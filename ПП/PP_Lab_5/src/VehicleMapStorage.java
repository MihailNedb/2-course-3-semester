import java.util.*;

public class VehicleMapStorage extends Storage<Vehicle> {
    private Map<String, Vehicle> vehicleMap;

    public VehicleMapStorage() {
        super();
        this.vehicleMap = new HashMap<>();
    }

    @Override
    public void add(Vehicle vehicle) {
        items.add(vehicle);
        vehicleMap.put(vehicle.getId(), vehicle);
    }

    @Override
    public void remove(Vehicle vehicle) {
        items.remove(vehicle);
        vehicleMap.remove(vehicle.getId());
    }

    @Override
    public void displayAll() {
        System.out.println("\n=== Все транспортные средства (Map) ===");
        Iterator<Vehicle> iterator = items.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }

    public Vehicle getById(String id) {
        return vehicleMap.get(id);
    }

    public Map<String, Vehicle> getSortedMap() {
        return new TreeMap<>(vehicleMap);
    }
}