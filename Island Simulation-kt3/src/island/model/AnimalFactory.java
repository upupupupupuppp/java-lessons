package island.model;

import island.config.AnimalType;
import island.model.herbivores.Boar;
import island.model.herbivores.Buffalo;
import island.model.herbivores.Caterpillar;
import island.model.herbivores.Deer;
import island.model.herbivores.Duck;
import island.model.herbivores.Goat;
import island.model.herbivores.Horse;
import island.model.herbivores.Mouse;
import island.model.herbivores.Rabbit;
import island.model.herbivores.Sheep;
import island.model.predators.Bear;
import island.model.predators.Boa;
import island.model.predators.Eagle;
import island.model.predators.Fox;
import island.model.predators.Wolf;

public final class AnimalFactory {
    private AnimalFactory() {
    }

    public static Animal create(AnimalType type) {
        switch (type) {
            case WOLF: return new Wolf();
            case BOA: return new Boa();
            case FOX: return new Fox();
            case BEAR: return new Bear();
            case EAGLE: return new Eagle();
            case HORSE: return new Horse();
            case DEER: return new Deer();
            case RABBIT: return new Rabbit();
            case MOUSE: return new Mouse();
            case GOAT: return new Goat();
            case SHEEP: return new Sheep();
            case BOAR: return new Boar();
            case BUFFALO: return new Buffalo();
            case DUCK: return new Duck();
            case CATERPILLAR: return new Caterpillar();
            default: throw new IllegalArgumentException("Неизвестный вид: " + type);
        }
    }
}
