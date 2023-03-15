package ebrt.math;

import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class TestGrid {

    @Test
    public void testStartingAtOrigin() {
        test(new Bounds2i(Point2i.ORIGIN, new Point2i(10, 20)));
    }

    @Test
    public void testWithNegativeOffset() {
        test(new Bounds2i(new Point2i(-1, -3), new Point2i(10, 20)));
    }

    @Test
    public void testWithPositiveOffset() {
        test(new Bounds2i(new Point2i(2, 6), new Point2i(10, 20)));
    }

    protected void test(Bounds2i bounds) {
        Map<Point2i,UUID> data = new LinkedHashMap<>();
        for (int y=bounds.min().y(); y <bounds.max().y(); y++) {
            for (int x=bounds.min().x(); x<bounds.max().x(); x++) {
                data.put(new Point2i(x, y), UUID.randomUUID());
            }
        }
        Grid<UUID> grid = new Grid<>(bounds, data.values().iterator()::next);
        for (int x=bounds.min().x(); x<bounds.max().x(); x++) {
            for (int y=bounds.min().y(); y <bounds.max().y(); y++) {
                UUID e = data.get(new Point2i(x, y));
                UUID a = grid.get(x, y);
                assertEquals(e, a);
            }
        }
    }
}
