import java.util.*;

public class VehicleListStorage extends Storage<Vehicle> {

    @Override
    public void add(Vehicle vehicle) {
        items.add(vehicle);
    }

    @Override
    public void remove(Vehicle vehicle) {
        items.remove(vehicle);
    }

    @Override
    public void displayAll() {
        System.out.println("\nВсе транспортные средства");
        Iterator<Vehicle> iterator = items.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }

    public Vehicle findById(String id) {
        return items.stream()
                .filter(v -> v.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}