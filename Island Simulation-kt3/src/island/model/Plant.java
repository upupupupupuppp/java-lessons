package island.model;

import island.config.Settings;

// Растение. Его можно съесть частично — тогда уменьшается вес.
public class Plant extends Organism {

    public Plant() {
        super(Settings.PLANT_WEIGHT);
    }

    // Откусить не больше amount кг. Возвращает сколько реально съедено.
    public double bite(double amount) {
        double eaten = Math.min(weight, amount);
        weight -= eaten;
        return eaten;
    }

    public boolean isEaten() {
        return weight <= 1e-9;
    }
}
