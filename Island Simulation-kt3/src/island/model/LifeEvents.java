package island.model;

import java.util.concurrent.atomic.AtomicLong;

// Счётчики событий за такт. Обновляются из разных потоков, сбрасываются при выводе статистики.
public final class LifeEvents {
    public static final AtomicLong BORN = new AtomicLong();
    public static final AtomicLong EATEN = new AtomicLong();
    public static final AtomicLong STARVED = new AtomicLong();
    public static final AtomicLong MOVED = new AtomicLong();

    private LifeEvents() {
    }
}
