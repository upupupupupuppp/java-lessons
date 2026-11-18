package island.model.herbivores;

import island.config.AnimalType;
import island.model.Herbivore;
import island.model.Location;

// Утка. Травоядное, но в первую очередь ловит гусениц, а растениями добирает остальное.
public class Duck extends Herbivore {

    public Duck() {
        super(AnimalType.DUCK);
    }

    @Override
    public void eat(Location location) {
        hunt(location);
        eatPlants(location);
    }
}
