package island.model;

import island.config.AnimalType;

// Хищник: питается только другими животными.
public abstract class Predator extends Animal {

    protected Predator(AnimalType type) {
        super(type);
    }

    @Override
    public void eat(Location location) {
        hunt(location);
    }
}
