package island.model.predators;

import island.config.AnimalType;
import island.model.Predator;

// Удав. Медленно переваривает добычу — голодает вдвое медленнее остальных.
public class Boa extends Predator {

    public Boa() {
        super(AnimalType.BOA);
    }

    @Override
    protected double hungerRate() {
        return super.hungerRate() / 2;
    }
}
