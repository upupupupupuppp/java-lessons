package island.simulation;

import island.config.AnimalType;
import island.config.Settings;
import island.model.Island;
import island.model.LifeEvents;
import island.model.Location;

import java.util.EnumMap;
import java.util.Map;

// Сбор и вывод статистики по острову.
public class Statistics {
    private final Island island;

    public Statistics(Island island) {
        this.island = island;
    }

    // Снимок численности: каждая клетка читается под своим замком.
    public Snapshot collect() {
        Map<AnimalType, Integer> totals = new EnumMap<>(AnimalType.class);
        for (AnimalType type : AnimalType.values()) {
            totals.put(type, 0);
        }
        long plants = 0;
        String[][] map = Settings.SHOW_MAP ? new String[island.getHeight()][island.getWidth()] : null;
        for (Location location : island.getAllLocations()) {
            location.lock();
            try {
                Map<AnimalType, Integer> counts = location.getCounts();
                counts.forEach((type, count) -> totals.merge(type, count, Integer::sum));
                plants += location.plantCount();
                if (map != null) {
                    map[location.getY()][location.getX()] = cellIcon(counts, location.plantCount());
                }
            } finally {
                location.unlock();
            }
        }
        return new Snapshot(totals, plants, map);
    }

    // На карте показываем вид с наибольшей биомассой на клетке.
    private static String cellIcon(Map<AnimalType, Integer> counts, int plants) {
        AnimalType dominant = null;
        double best = 0;
        for (Map.Entry<AnimalType, Integer> entry : counts.entrySet()) {
            double mass = entry.getValue() * entry.getKey().getWeight();
            if (mass > best) {
                best = mass;
                dominant = entry.getKey();
            }
        }
        if (dominant != null) {
            return dominant.getIcon();
        }
        return plants > 0 ? "🌿" : "🟫";
    }

    public void print(int tick, Snapshot snapshot) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%n══════════ Такт %d ══════════  животных: %d  (хищников %d, травоядных %d)  🌿 растений: %d%n",
                tick, snapshot.totalAnimals(), snapshot.predators(), snapshot.herbivores(), snapshot.getPlants()));
        for (AnimalType type : AnimalType.values()) {
            int count = snapshot.count(type);
            sb.append(String.format("%s %-9s %7d  %s%n", type.getIcon(), type.getTitle(), count, bar(count, type.getInitialCount())));
        }
        sb.append(String.format("За такт: родилось %d, съедено %d, умерло от голода %d, переместилось %d%n",
                LifeEvents.BORN.getAndSet(0), LifeEvents.EATEN.getAndSet(0),
                LifeEvents.STARVED.getAndSet(0), LifeEvents.MOVED.getAndSet(0)));
        if (snapshot.getMap() != null) {
            for (String[] row : snapshot.getMap()) {
                sb.append(String.join("", row)).append(System.lineSeparator());
            }
        }
        System.out.print(sb);
    }

    // Полоска относительно стартовой численности: полная ширина = 100% от старта.
    private static String bar(int count, int initial) {
        if (initial <= 0) {
            return "";
        }
        int full = Settings.STAT_BAR_WIDTH;
        int length = (int) Math.round((double) count * full / initial);
        if (length > full) {
            return "█".repeat(full) + "▶";
        }
        if (length == 0 && count > 0) {
            return "▏";
        }
        return "█".repeat(length);
    }

    public static class Snapshot {
        private final Map<AnimalType, Integer> counts;
        private final long plants;
        private final String[][] map;

        Snapshot(Map<AnimalType, Integer> counts, long plants, String[][] map) {
            this.counts = counts;
            this.plants = plants;
            this.map = map;
        }

        public int count(AnimalType type) {
            return counts.get(type);
        }

        public long getPlants() {
            return plants;
        }

        String[][] getMap() {
            return map;
        }

        public long predators() {
            return counts.entrySet().stream().filter(e -> e.getKey().isPredator()).mapToLong(Map.Entry::getValue).sum();
        }

        public long herbivores() {
            return totalAnimals() - predators();
        }

        public long totalAnimals() {
            return counts.values().stream().mapToLong(Integer::longValue).sum();
        }
    }
}
