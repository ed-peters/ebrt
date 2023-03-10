package attic.math;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestProjections {

    @Test
    public void testOrthographic() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testPerspective() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testScreenRasterCamera() {
        Point2i resolution = new Point2i(640, 480);
        Bounds2d screenWindow = new Bounds2d(new Point2d(-100, -100), new Point2d(100, 100));
        Transform screenToRaster = Projections.screenToRaster(resolution, screenWindow);
        Point3d actual0 = screenToRaster.forward(new Point3d(65, 65, 10));
        Point3d expected0 = new Point3d(528, 84, 10);
        assertEquals(expected0, actual0);
    }
}
