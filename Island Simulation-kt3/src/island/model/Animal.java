package island.model;

import island.config.AnimalType;
import island.config.Settings;

import java.util.concurrent.ThreadLocalRandom;

// Общее поведение всех животных: питание, размножение, голод, выбор направления.
// Все методы, меняющие состояние, вызываются под замком клетки, в которой находится животное.
public abstract class Animal extends Organism {
    private final AnimalType type;
    protected double satiety;
    private boolean alive = true;
    private int lastMoveTick = -1;
    private int indexInCell = -1;

    protected Animal(AnimalType type) {
        super(type.getWeight());
        this.type = type;
        this.satiety = type.getFoodNeeded() * Settings.INITIAL_SATIETY;
    }

    public AnimalType getType() {
        return type;
    }

    public boolean isAlive() {
        return alive;
    }

    public void die() {
        alive = false;
    }

    // ---------- Питание ----------

    // Покушать. Хищники и травоядные переопределяют.
    public abstract void eat(Location location);

    // Попытаться поймать и съесть кого-то на клетке (по Таблице №1).
    protected void hunt(Location location) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        for (int attempt = 0; attempt < Settings.MAX_HUNT_ATTEMPTS && !isFull(); attempt++) {
            Animal victim = pickVictim(location, random);
            if (victim == null) {
                return;
            }
            if (random.nextInt(100) < huntChance(victim, location)) {
                victim.die();
                location.removeAnimal(victim);
                feed(victim.getWeight());
                LifeEvents.EATEN.incrementAndGet();
            }
        }
    }

    // Случайная потенциальная жертва среди тех, кого этот вид в принципе может съесть.
    protected Animal pickVictim(Location location, ThreadLocalRandom random) {
        int total = 0;
        for (AnimalType prey : AnimalType.values()) {
            if (Settings.eatProbability(type, prey) > 0) {
                total += location.count(prey);
            }
        }
        if (total == 0) {
            return null;
        }
        int index = random.nextInt(total);
        for (AnimalType prey : AnimalType.values()) {
            if (Settings.eatProbability(type, prey) > 0) {
                int count = location.count(prey);
                if (index < count) {
                    return location.get(prey, index);
                }
                index -= count;
            }
        }
        return null;
    }

    // Вероятность (в %) съесть жертву. Можно переопределить (например, охота стаей).
    protected int huntChance(Animal victim, Location location) {
        return Settings.eatProbability(type, victim.getType());
    }

    protected void eatPlants(Location location) {
        int chance = Settings.plantProbability(type);
        if (chance <= 0 || isFull() || ThreadLocalRandom.current().nextInt(100) >= chance) {
            return;
        }
        feed(location.eatPlants(hunger()));
    }

    protected void feed(double kg) {
        satiety = Math.min(type.getFoodNeeded(), satiety + kg);
    }

    // Сколько ещё кг нужно до полного насыщения.
    protected double hunger() {
        return type.getFoodNeeded() - satiety;
    }

    protected boolean isFull() {
        return hunger() <= 1e-9;
    }

    // Потратить энергию за такт. Возвращает true, если животное умерло от голода.
    public boolean starve() {
        satiety -= type.getFoodNeeded() * hungerRate();
        if (satiety <= 0) {
            die();
            return true;
        }
        return false;
    }

    protected double hungerRate() {
        return Settings.HUNGER_PER_TICK;
    }

    // ---------- Размножение ----------

    // Размножиться, если на клетке есть пара и место для детёнышей.
    public void reproduce(Location location) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        if (!canReproduce(location) || random.nextDouble() >= type.getReproductionChance()) {
            return;
        }
        int babies = 1 + random.nextInt(type.getMaxOffspring());
        for (int i = 0; i < babies; i++) {
            if (!location.addAnimal(AnimalFactory.create(type))) {
                break;
            }
            LifeEvents.BORN.incrementAndGet();
        }
    }

    protected boolean canReproduce(Location location) {
        return location.count(type) >= 2
                && satiety >= type.getFoodNeeded() * Settings.REPRODUCTION_SATIETY;
    }

    // ---------- Передвижение ----------

    public Direction chooseDirection() {
        Direction[] directions = Direction.values();
        return directions[ThreadLocalRandom.current().nextInt(directions.length)];
    }

    // Куда животное хочет прийти за этот такт: до speed шагов в случайных направлениях.
    public Location chooseDestination(Island island, Location from) {
        int steps = ThreadLocalRandom.current().nextInt(type.getSpeed() + 1);
        Location current = from;
        for (int i = 0; i < steps; i++) {
            Location next = island.neighbor(current, chooseDirection());
            if (next != null) {
                current = next;
            }
        }
        return current;
    }

    int getIndexInCell() {
        return indexInCell;
    }

    void setIndexInCell(int indexInCell) {
        this.indexInCell = indexInCell;
    }

    boolean hasMovedOn(int tick) {
        return lastMoveTick == tick;
    }

    void markMoved(int tick) {
        lastMoveTick = tick;
    }

    @Override
    public String toString() {
        return type.getIcon();
    }
}
