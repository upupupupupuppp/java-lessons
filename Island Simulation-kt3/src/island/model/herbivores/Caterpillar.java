package island.model.herbivores;

import island.config.AnimalType;
import island.model.Herbivore;
import island.model.Location;

// Гусеница. Не передвигается (скорость 0) и не нуждается в пище, поэтому не умирает от голода.
public class Caterpillar extends Herbivore {

    public Caterpillar() {
        super(AnimalType.CATERPILLAR);
    }

    @Override
    public void eat(Location location) {
        // Норма еды — 0 кг: гусенице достаточно того, что она сидит на листе.
    }

    @Override
    public boolean starve() {
        return false;
    }
}
