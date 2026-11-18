package island.model;

// Общий предок всего живого на острове.
public abstract class Organism {
    protected double weight;

    protected Organism(double weight) {
        this.weight = weight;
    }

    public double getWeight() {
        return weight;
    }
}
