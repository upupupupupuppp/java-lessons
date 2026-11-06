import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.Queue;
import java.util.LinkedList;


public static void main(String[] args) {
    List<Vehicle> fleet = new ArrayList<>();
    fleet.add(new Car("A123BC77", 1500, 5));
    fleet.add(new Car("A123BC77", 2000, 4));
    fleet.add(new Car("J879UI74", 3500, 8));
    fleet.add(new Motorcycle("B456CD77", 800, 600.0));
    fleet.add(new Truck("C789DE77", 2500, 5.0));
    fleet.add(new Truck("F745QU42", 3500, 7.0));

    Set<String> uniquePlates = new HashSet<>();
    for (Vehicle vehicle : fleet) {
        System.out.println(vehicle);
        uniquePlates.add(vehicle.plateNumber);
    }
    System.out.println(fleet.size());
    System.out.println(uniquePlates.size());

    if (fleet.size() == uniquePlates.size()) {
        System.out.println("каждый номер уникален");
    } else {
        System.out.println("в списке есть одинаковые номера");
    }

    Map<String, Vehicle> byPlate = new HashMap<>();
    for (Vehicle vehicle : fleet) {
        byPlate.put(vehicle.plateNumber, vehicle);
    }

    Vehicle found = byPlate.get("A123BC77");
    System.out.println(found);

    Set<Vehicle> uniqueVehicles = new HashSet<>();

    Vehicle car1 = new Car("A123BC77", 1500, 5);
    Vehicle car2 = new Car("A123BC77", 2000, 4);

    uniqueVehicles.add(new Car("A123BC77", 1500, 5));
    uniqueVehicles.add(new Car("A123BC77", 2000, 4));

    System.out.println(uniqueVehicles.size());

    Queue<Vehicle> serviceQueue = new LinkedList<>();
    serviceQueue.offer(new Truck("A127BC77", 1200, 400));
    serviceQueue.offer(new Car("A753BC77", 1700, 10));

    while (!serviceQueue.isEmpty()) {
        Vehicle vehicle = serviceQueue.poll();
        System.out.println(vehicle);
    }


    Collections.sort(fleet);
    for (Vehicle vehicle : fleet) {
        System.out.println(" sort : " + vehicle);
    }

    Collections.sort(fleet, new ByPlateComparator());
    for (Vehicle vehicle : fleet) {
        System.out.println(" !! sort : " + vehicle);
    }




}

