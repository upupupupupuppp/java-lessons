package island.model.predators;

import island.config.AnimalType;
import island.model.Animal;
import island.model.Location;
import island.model.Predator;

// Волк. Охотится стаей: каждый сородич на клетке повышает шанс поймать добычу.
public class Wolf extends Predator {
    private static final int PACK_BONUS_PER_WOLF = 5;
    private static final int MAX_PACK_BONUS = 20;

    public Wolf() {
        super(AnimalType.WOLF);
    }

    @Override
    protected int huntChance(Animal victim, Location location) {
        int base = super.huntChance(victim, location);
        if (base <= 0) {
            return base;
        }
        int packmates = location.count(AnimalType.WOLF) - 1;
        return Math.min(100, base + Math.min(MAX_PACK_BONUS, packmates * PACK_BONUS_PER_WOLF));
    }
}
