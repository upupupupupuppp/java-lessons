package island.model;

import island.config.AnimalType;
import island.config.Settings;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

// Остров — двумерный массив локаций.
public class Island {
    private final int width;
    private final int height;
    private final Location[][] locations;

    public Island(int width, int height) {
        this.width = width;
        this.height = height;
        this.locations = new Location[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                locations[y][x] = new Location(x, y);
            }
        }
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }

    public Location getLocation(int x, int y) {
        return locations[y][x];
    }

    public Location[] getRow(int y) {
        return locations[y];
    }

    public List<Location> getAllLocations() {
        List<Location> result = new ArrayList<>(width * height);
        for (Location[] row : locations) {
            for (Location location : row) {
                result.add(location);
            }
        }
        return result;
    }

    // Соседняя клетка в направлении или null, если там край острова.
    public Location neighbor(Location from, Direction direction) {
        int x = from.getX() + direction.getDx();
        int y = from.getY() + direction.getDy();
        if (x < 0 || y < 0 || x >= width || y >= height) {
            return null;
        }
        return locations[y][x];
    }

    // Заселение острова растениями и стартовым количеством животных каждого вида.
    public void populate() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        for (Location location : getAllLocations()) {
            location.growPlants(Settings.PLANT_INITIAL_PER_CELL);
        }
        for (AnimalType type : AnimalType.values()) {
            int placed = 0;
            int attempts = 0;
            int maxAttempts = type.getInitialCount() * 10;
            while (placed < type.getInitialCount() && attempts++ < maxAttempts) {
                Location location = locations[random.nextInt(height)][random.nextInt(width)];
                if (location.addAnimal(AnimalFactory.create(type))) {
                    placed++;
                }
            }
        }
    }

    // Переместить животное между клетками. Обе клетки блокируются в едином порядке (по координатам),
    // чтобы два потока, двигающие животных навстречу друг другу, не попали в дедлок.
    public void move(Animal animal, Location from, Location to, int tick) {
        Location first = order(from) <= order(to) ? from : to;
        Location second = first == from ? to : from;
        first.lock();
        try {
            if (second != first) {
                second.lock();
            }
            try {
                if (!animal.isAlive() || animal.hasMovedOn(tick) || !from.contains(animal)) {
                    return;
                }
                animal.markMoved(tick);
                if (from != to && to.hasRoomFor(animal.getType())) {
                    from.removeAnimal(animal);
                    to.addAnimal(animal);
                    LifeEvents.MOVED.incrementAndGet();
                }
            } finally {
                if (second != first) {
                    second.unlock();
                }
            }
        } finally {
            first.unlock();
        }
    }

    private int order(Location location) {
        return location.getY() * width + location.getX();
    }
}
