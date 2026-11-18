package island.config;

// Все параметры симуляции в одном месте.
// Характеристики конкретных видов (вес, скорость, приплод, стартовое количество...) — в AnimalType.
public final class Settings {
    private Settings() {
    }

    // ---------- Остров ----------
    public static final int WIDTH = 100;
    public static final int HEIGHT = 20;

    // ---------- Время ----------
    // Длительность такта, мс.
    public static final long TICK_MILLIS = 1000;
    // Максимум тактов (0 — без ограничения).
    public static final int MAX_TICKS = 100;
    public static final StopCondition STOP_CONDITION = StopCondition.ALL_ANIMALS_DEAD;

    // ---------- Растения ----------
    public static final double PLANT_WEIGHT = 1.0;
    public static final int PLANT_MAX_PER_CELL = 200;
    public static final int PLANT_INITIAL_PER_CELL = 100;
    public static final int PLANT_GROWTH_PER_TICK = 60;

    // ---------- Животные ----------
    // Какую долю от нормы еды животное "сжигает" за такт.
    public static final double HUNGER_PER_TICK = 0.25;
    // Сытость при рождении/старте, доля от нормы еды.
    public static final double INITIAL_SATIETY = 0.5;
    // Минимальная сытость для размножения, доля от нормы еды.
    public static final double REPRODUCTION_SATIETY = 0.5;
    // Сколько попыток охоты может сделать животное за такт.
    public static final int MAX_HUNT_ATTEMPTS = 3;

    // ---------- Потоки и вывод ----------
    public static final int WORKER_THREADS = Runtime.getRuntime().availableProcessors();
    // Рисовать карту острова псевдографикой (имеет смысл для небольших островов).
    public static final boolean SHOW_MAP = false;
    public static final int STAT_BAR_WIDTH = 30;

    public static final int PLANTS = AnimalType.values().length;

    public static final int[][] EAT_PROBABILITY = {
            //Вол Уда Лис Мед Орё Лош Оле Кро Мыш Коз Овц Каб Буй Утк Гус Раст
            {0,  0,  0,  0,  0,  10, 15, 60, 80, 60, 70, 15, 10, 40, 0,  0},   // Волк
            {0,  0,  15, 0,  0,  0,  0,  20, 40, 0,  0,  0,  0,  10, 0,  0},   // Удав
            {0,  0,  0,  0,  0,  0,  0,  70, 90, 0,  0,  0,  0,  60, 40, 0},   // Лиса
            {0,  80, 0,  0,  0,  40, 80, 80, 90, 70, 70, 50, 20, 10, 0,  0},   // Медведь
            {0,  0,  10, 0,  0,  0,  0,  90, 90, 0,  0,  0,  0,  80, 0,  0},   // Орел
            {0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  100}, // Лошадь
            {0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  100}, // Олень
            {0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  100}, // Кролик
            {0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  90, 100}, // Мышь
            {0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  100}, // Коза
            {0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  100}, // Овца
            {0,  0,  0,  0,  0,  0,  0,  0,  50, 0,  0,  0,  0,  0,  90, 100}, // Кабан
            {0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  100}, // Буйвол
            {0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  90, 100}, // Утка
            {0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  100}, // Гусеница
    };

    public static int eatProbability(AnimalType eater, AnimalType food) {
        return EAT_PROBABILITY[eater.ordinal()][food.ordinal()];
    }

    public static int plantProbability(AnimalType eater) {
        return EAT_PROBABILITY[eater.ordinal()][PLANTS];
    }
}
