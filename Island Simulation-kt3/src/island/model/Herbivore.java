package island.model;

import island.config.AnimalType;

// Травоядное: в первую очередь ест растения. Некоторые травоядные (мышь, кабан, утка)
// по Таблице №1 едят ещё и мелких животных — если после растений осталось место, они охотятся.
public abstract class Herbivore extends Animal {

    protected Herbivore(AnimalType type) {
        super(type);
    }

    @Override
    public void eat(Location location) {
        eatPlants(location);
        hunt(location);
    }
}
