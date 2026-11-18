package island.simulation;

import island.config.Settings;
import island.model.Animal;
import island.model.Island;
import island.model.LifeEvents;
import island.model.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

// Управление симуляцией.
// - scheduled-пул запускает по расписанию два задания: рост растений и жизненный цикл животных
//   (в конце которого выводится статистика);
// - внутри жизненного цикла обычный пул обрабатывает остров параллельно — по строкам клеток.
// Согласованность данных обеспечивают замки клеток (Location.lock()).
public class Simulation {
    private final Island island = new Island(Settings.WIDTH, Settings.HEIGHT);
    private final Statistics statistics = new Statistics(island);
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
    private final ExecutorService workers = Executors.newFixedThreadPool(Settings.WORKER_THREADS);
    private final AtomicInteger tick = new AtomicInteger();
    private final CountDownLatch finished = new CountDownLatch(1);

    public void run() throws InterruptedException {
        island.populate();
        System.out.printf("Остров %dx%d, такт %d мс, потоков в пуле: %d%n",
                Settings.WIDTH, Settings.HEIGHT, Settings.TICK_MILLIS, Settings.WORKER_THREADS);
        statistics.print(0, statistics.collect());

        long period = Settings.TICK_MILLIS;
        scheduler.scheduleAtFixedRate(safe(this::growPlants), period, period, TimeUnit.MILLISECONDS);
        scheduler.scheduleAtFixedRate(safe(this::lifeCycle), period, period, TimeUnit.MILLISECONDS);

        finished.await();
        scheduler.shutdown();
        workers.shutdown();
        scheduler.awaitTermination(5, TimeUnit.SECONDS);
        workers.awaitTermination(5, TimeUnit.SECONDS);
    }

    // ---------- Задания ----------

    private void growPlants() {
        for (Location location : island.getAllLocations()) {
            location.lock();
            try {
                location.growPlants(Settings.PLANT_GROWTH_PER_TICK);
            } finally {
                location.unlock();
            }
        }
    }

    private void lifeCycle() {
        int currentTick = tick.incrementAndGet();
        runInParallel(this::feedReproduceStarve);
        runInParallel(location -> moveAnimals(location, currentTick));

        Statistics.Snapshot snapshot = statistics.collect();
        statistics.print(currentTick, snapshot);
        String reason = stopReason(currentTick, snapshot);
        if (reason != null) {
            System.out.println("\nСимуляция остановлена: " + reason);
            finished.countDown();
        }
    }

    // Фаза 1: каждое животное на клетке ест, размножается и тратит энергию.
    private void feedReproduceStarve(Location location) {
        location.lock();
        try {
            for (Animal animal : location.getAnimals()) {
                if (!animal.isAlive()) {
                    continue; // съели раньше в этом же такте
                }
                animal.eat(location);
                animal.reproduce(location);
                if (animal.starve()) {
                    location.removeAnimal(animal);
                    LifeEvents.STARVED.incrementAndGet();
                }
            }
        } finally {
            location.unlock();
        }
    }

    // Фаза 2: животные расходятся по соседним клеткам.
    private void moveAnimals(Location location, int currentTick) {
        List<Animal> animals;
        location.lock();
        try {
            animals = location.getAnimals();
        } finally {
            location.unlock();
        }
        for (Animal animal : animals) {
            if (animal.getType().getSpeed() == 0) {
                continue;
            }
            Location destination = animal.chooseDestination(island, location);
            island.move(animal, location, destination, currentTick);
        }
    }

    // Отдаёт обработку строк острова обычному пулу и ждёт завершения всех задач.
    private void runInParallel(Consumer<Location> action) {
        List<Callable<Void>> tasks = new ArrayList<>();
        for (int y = 0; y < island.getHeight(); y++) {
            Location[] row = island.getRow(y);
            tasks.add(() -> {
                for (Location location : row) {
                    action.accept(location);
                }
                return null;
            });
        }
        try {
            for (Future<Void> future : workers.invokeAll(tasks)) {
                future.get();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            throw new IllegalStateException(e.getCause());
        }
    }

    private String stopReason(int currentTick, Statistics.Snapshot snapshot) {
        if (Settings.MAX_TICKS > 0 && currentTick >= Settings.MAX_TICKS) {
            return "достигнут лимит тактов (" + Settings.MAX_TICKS + ")";
        }
        switch (Settings.STOP_CONDITION) {
            case ALL_ANIMALS_DEAD:
                return snapshot.totalAnimals() == 0 ? "все животные умерли" : null;
            case ALL_PREDATORS_DEAD:
                return snapshot.predators() == 0 ? "все хищники умерли" : null;
            case ALL_HERBIVORES_DEAD:
                return snapshot.herbivores() == 0 ? "все травоядные умерли" : null;
            default:
                return null;
        }
    }

    // Исключение в scheduled-задаче молча отменяет её — поэтому печатаем и останавливаемся.
    private Runnable safe(Runnable task) {
        return () -> {
            try {
                task.run();
            } catch (Throwable t) {
                t.printStackTrace();
                finished.countDown();
            }
        };
    }
}
