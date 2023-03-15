package ebrt.math;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestBounds2d {

    @Test
    public void testHeightWidthArea() {
        Bounds2d bounds = new Bounds2d(new Point2d(-1, 3), new Point2d(4, -10));
        assertEquals(5, bounds.width(), 1e-6);
        assertEquals(13, bounds.height(), 1e-6);
        assertEquals(65, bounds.area(), 1e-6);
    }

    @Test
    public void testAspectRatio() {
        assertEquals(1.3333333, new Bounds2d(Point2d.ORIGIN, new Point2d(4, 3)).aspectRatio(), 1e-6);
        assertEquals(1.7777777, new Bounds2d(Point2d.ORIGIN, new Point2d(16, 9)).aspectRatio(), 1e-6);
    }
}
