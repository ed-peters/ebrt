package ebrt.math;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TestBounds2i {

    @Test
    public void testIntersectOverlap() {
        Bounds2i b1 = new Bounds2i(new Point2i(-3, 4), new Point2i(5, -6));
        Bounds2i b2 = new Bounds2i(new Point2i(-1, 2), new Point2i(3, -4));
        assertEquals(b1.intersect(b2), b2);
        assertEquals(b2.intersect(b1), b2);
    }

    @Test
    public void testIntersectNonOverlap() {
        Bounds2i b1 = new Bounds2i(new Point2i(-3, 4), new Point2i(5, -6));
        Bounds2i b2 = new Bounds2i(new Point2i(-7, 2), new Point2i(9, -4));
        Bounds2i b3 = new Bounds2i(new Point2i(-3, 2), new Point2i(5, -4));
        assertEquals(b1.intersect(b2), b3);
        assertEquals(b2.intersect(b1), b3);
    }

    @Test
    public void testIterator() {

        Bounds2i b = new Bounds2i(new Point2i(-1, 1), new Point2i(1, -2));

        List<Point2i> p = new ArrayList<>();
        b.iterator().forEachRemaining(p::add);

        System.err.println(p);
        assertEquals(12, p.size());
    }
}
