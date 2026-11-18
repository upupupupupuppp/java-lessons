package island.model.herbivores;

import island.config.AnimalType;
import island.model.Herbivore;

// Кабан (ест ещё мышей и гусениц).
public class Boar extends Herbivore {

    public Boar() {
        super(AnimalType.BOAR);
    }
}
