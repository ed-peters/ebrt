package ebrt.math;

import ebrt.interactions.Ray;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class TestBounds3d {

    @Test
    public void testIntersectSuccess() {

        Bounds3d bounds = new Bounds3d(
                new Point3d(-1, -1, -1),
                new Point3d(1, 1, -2));
        Ray ray = new Ray(Point3d.ORIGIN, Vector3d.Z.negate());

        Hits hits = bounds.intersects(ray);
        assertNotNull(hits);
        assertEquals(1.0, hits.t0(), 1e-8);
        assertEquals(2.0, hits.t1(), 1e-8);
    }

    @Test
    public void testIntersectFailure() {

        Bounds3d bounds = new Bounds3d(
                new Point3d(-1, -1, -1),
                new Point3d(1, 1, -2));
        Ray ray = new Ray(Point3d.ORIGIN, Vector3d.X);

        Hits hits = bounds.intersects(ray);
        assertNull(hits);
    }
}
