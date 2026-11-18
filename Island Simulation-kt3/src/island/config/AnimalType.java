package island.config;

// Виды животных и их характеристики (Таблица №2) + параметры размножения и стартовая численность.
// Порядок констант совпадает с порядком строк/столбцов Таблицы №1 (см. Settings.EAT_PROBABILITY).
public enum AnimalType {
    //            название     иконка  хищник  вес    макс/клетка скорость еда(кг) старт  приплод шанс размножения
    WOLF       ("Волк",     "🐺", true,  50,    30,   3, 8,     600,   3, 0.25),
    BOA        ("Удав",     "🐍", true,  15,    30,   1, 3,     600,   3, 0.20),
    FOX        ("Лиса",     "🦊", true,  8,     30,   2, 2,     600,   3, 0.25),
    BEAR       ("Медведь",  "🐻", true,  500,   5,    2, 80,    100,   1, 0.15),
    EAGLE      ("Орел",     "🦅", true,  6,     20,   3, 1,     400,   2, 0.20),
    HORSE      ("Лошадь",   "🐎", false, 400,   20,   4, 60,    400,   1, 0.20),
    DEER       ("Олень",    "🦌", false, 300,   20,   4, 50,    400,   1, 0.20),
    RABBIT     ("Кролик",   "🐇", false, 2,     150,  2, 0.45,  6000,  3, 0.25),
    MOUSE      ("Мышь",     "🐁", false, 0.05,  500,  1, 0.01,  20000, 4, 0.25),
    GOAT       ("Коза",     "🐐", false, 60,    140,  3, 10,    3000,  2, 0.25),
    SHEEP      ("Овца",     "🐑", false, 70,    140,  3, 15,    3000,  2, 0.25),
    BOAR       ("Кабан",    "🐗", false, 400,   50,   2, 50,    1000,  3, 0.20),
    BUFFALO    ("Буйвол",   "🐃", false, 700,   10,   3, 100,   200,   1, 0.15),
    DUCK       ("Утка",     "🦆", false, 1,     200,  4, 0.15,  4000,  3, 0.20),
    CATERPILLAR("Гусеница", "🐛", false, 0.01,  1000, 0, 0,     50000, 10, 0.50);

    private final String title;
    private final String icon;
    private final boolean predator;
    private final double weight;
    private final int maxPerCell;
    private final int speed;
    private final double foodNeeded;
    private final int initialCount;
    private final int maxOffspring;
    private final double reproductionChance;

    AnimalType(String title, String icon, boolean predator, double weight, int maxPerCell, int speed,
               double foodNeeded, int initialCount, int maxOffspring, double reproductionChance) {
        this.title = title;
        this.icon = icon;
        this.predator = predator;
        this.weight = weight;
        this.maxPerCell = maxPerCell;
        this.speed = speed;
        this.foodNeeded = foodNeeded;
        this.initialCount = initialCount;
        this.maxOffspring = maxOffspring;
        this.reproductionChance = reproductionChance;
    }

    public String getTitle() { return title; }
    public String getIcon() { return icon; }
    public boolean isPredator() { return predator; }
    public double getWeight() { return weight; }
    public int getMaxPerCell() { return maxPerCell; }
    public int getSpeed() { return speed; }
    public double getFoodNeeded() { return foodNeeded; }
    public int getInitialCount() { return initialCount; }
    public int getMaxOffspring() { return maxOffspring; }
    public double getReproductionChance() { return reproductionChance; }
}
