package ebrt.math;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public record Grid<T>(int width, int height, Point2i offset, List<T> items) {

    public Grid(Bounds2i bounds) {
        this(bounds.width(), bounds.height(), bounds.min(), new ArrayList<>(bounds.area()));
    }

    public Grid(Bounds2i bounds, Supplier<T> supplier) {
        this(bounds);
        for (int i=0; i<bounds.area(); i++) {
            items.add(supplier.get());
        }
    }

    protected int index(int x, int y) {
        int offsetX = x - offset.x();
        int offsetY = y - offset.y();
        return width * offsetY + offsetX;
    }

    public void set(int x, int y, T item) {
        int index = index(x, y);
        items.set(index, item);
    }

    public T get(int x, int y) {
        int index = index(x, y);
        return items.get(index);
    }
}
