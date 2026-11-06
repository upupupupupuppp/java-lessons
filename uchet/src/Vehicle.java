import java.util.Objects;
import java.util.Collections;
import java.util.Comparator;

abstract class Vehicle implements Comparable<Vehicle> {
    protected String plateNumber;
    protected double dailyRate;
    abstract double calculateRentalCost(int days);

    @Override
    public int compareTo(Vehicle other) {
        return Double.compare(this.dailyRate, other.dailyRate);
    }

    public Vehicle(String plateNumber, double dailyRate) {
        this.plateNumber = plateNumber;
        this.dailyRate = dailyRate;

//        this.insuranceFee = InsuranceFee;
    }

    @Override
    public int hashCode() {
        return Objects.hash(plateNumber);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Vehicle vehicle = (Vehicle) obj;

        return Objects.equals(this.plateNumber, vehicle.plateNumber);
    }

//    переопредилили метод
@Override
public String toString() {
    return "Гос. номер: " + plateNumber
            + ", стоимость аренды: " + dailyRate + " ₽/сутки";
}
//    @Override
//    public double getInsuranceFee() {
//        return insuranceFee;
//    }

}

class Car extends Vehicle implements Insurable {
    private int seats;
    @Override
    public double getInsuranceFee() {
        double insuranceFee = dailyRate * 0.05;
        return insuranceFee;
    }
    @Override
    public double calculateRentalCost(int days) {
       double cost = dailyRate * days;

       if (days > 7) {
           cost = cost * 0.9;
       }

        return cost;
    }
    public Car(String plateNumber, double dailyRate, int seats) {

        super(plateNumber, dailyRate);
        this.seats = seats;


    }
}
class Motorcycle extends Vehicle {
   private double engineVolume;

    @Override
    public double calculateRentalCost(int days) {
        double cost = dailyRate * days;
        return cost;
    }

    public Motorcycle (String plateNumber, double dailyRate, double engineVolume) {
        super(plateNumber, dailyRate);
        this.engineVolume = engineVolume;
    }
}

class Truck extends Vehicle implements Insurable {
    private double capacityTons;
    @Override
    public double calculateRentalCost(int days) {
        double cost = dailyRate * days + 500 * days;
        return cost;
    }
    @Override
    public double getInsuranceFee() {
       double insuranceFee = dailyRate * 0.08;
        return insuranceFee;
    }

    public Truck (String plateNumber, double dailyRate, double capacityTons) {
        super(plateNumber, dailyRate);
        this.capacityTons = capacityTons;
    }
}
class ByPlateComparator implements Comparator<Vehicle> {
    public int compare(Vehicle platenumber1, Vehicle platenumber2) {

        int result = platenumber1.plateNumber.compareTo(platenumber2.plateNumber);

        if (result == 0) {
            return platenumber1.compareTo(platenumber2);
        }

        return result;
    }


}




