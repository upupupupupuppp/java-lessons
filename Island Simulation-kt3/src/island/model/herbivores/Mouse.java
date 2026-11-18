package island.model.herbivores;

import island.config.AnimalType;
import island.model.Herbivore;

// Мышь (ест ещё и гусениц).
public class Mouse extends Herbivore {

    public Mouse() {
        super(AnimalType.MOUSE);
    }
}
