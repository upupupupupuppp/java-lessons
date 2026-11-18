package island.model;

import island.config.AnimalType;
import island.config.Settings;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

// Клетка острова. Сама по себе не потокобезопасна: любой доступ к содержимому выполняется
// под lock() / unlock().
public class Location {
    private final int x;
    private final int y;
    private final ReentrantLock lock = new ReentrantLock();

    // Животные по видам. Удаление за O(1): последний элемент переставляется на место удалённого.
    private final Map<AnimalType, List<Animal>> animals = new EnumMap<>(AnimalType.class);
    private final List<Plant> plants = new ArrayList<>();

    public Location(int x, int y) {
        this.x = x;
        this.y = y;
        for (AnimalType type : AnimalType.values()) {
            animals.put(type, new ArrayList<>());
        }
    }

    public int getX() { return x; }
    public int getY() { return y; }

    public void lock() { lock.lock(); }
    public void unlock() { lock.unlock(); }

    // ---------- Животные ----------

    // Добавляет животное, если на клетке есть место для его вида.
    public boolean addAnimal(Animal animal) {
        List<Animal> list = animals.get(animal.getType());
        if (list.size() >= animal.getType().getMaxPerCell()) {
            return false;
        }
        animal.setIndexInCell(list.size());
        list.add(animal);
        return true;
    }

    public boolean hasRoomFor(AnimalType type) {
        return count(type) < type.getMaxPerCell();
    }

    public void removeAnimal(Animal animal) {
        if (!contains(animal)) {
            return;
        }
        List<Animal> list = animals.get(animal.getType());
        int index = animal.getIndexInCell();
        Animal last = list.remove(list.size() - 1);
        if (last != animal) {
            list.set(index, last);
            last.setIndexInCell(index);
        }
        animal.setIndexInCell(-1);
    }

    public boolean contains(Animal animal) {
        List<Animal> list = animals.get(animal.getType());
        int index = animal.getIndexInCell();
        return index >= 0 && index < list.size() && list.get(index) == animal;
    }

    public int count(AnimalType type) {
        return animals.get(type).size();
    }

    public Animal get(AnimalType type, int index) {
        return animals.get(type).get(index);
    }

    // Копия списка всех животных — по ней можно итерироваться, меняя саму клетку.
    public List<Animal> getAnimals() {
        List<Animal> result = new ArrayList<>();
        for (List<Animal> list : animals.values()) {
            result.addAll(list);
        }
        return result;
    }

    public Map<AnimalType, Integer> getCounts() {
        Map<AnimalType, Integer> counts = new EnumMap<>(AnimalType.class);
        animals.forEach((type, list) -> counts.put(type, list.size()));
        return counts;
    }

    // ---------- Растения ----------

    public int growPlants(int amount) {
        int grown = 0;
        while (grown < amount && plants.size() < Settings.PLANT_MAX_PER_CELL) {
            plants.add(new Plant());
            grown++;
        }
        return grown;
    }

    // Съесть до amount кг растений. Возвращает сколько съедено.
    public double eatPlants(double amount) {
        double eaten = 0;
        while (eaten < amount && !plants.isEmpty()) {
            Plant plant = plants.get(plants.size() - 1);
            eaten += plant.bite(amount - eaten);
            if (plant.isEaten()) {
                plants.remove(plants.size() - 1);
            }
        }
        return eaten;
    }

    public int plantCount() {
        return plants.size();
    }
}
