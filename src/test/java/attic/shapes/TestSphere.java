package attic.shapes;

import ebrt.interactions.SurfaceInteraction;
import attic.math.Normal3d;
import attic.math.Point3d;
import attic.math.Ray;
import attic.math.Transform;
import attic.math.Vector3d;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class TestSphere {

    @Test
    public void testFrontHit() {

        Sphere s = new Sphere(Point3d.ORIGIN, 3.0);

        Ray r = new Ray(new Point3d(0, 0, -10), new Vector3d(0, 0, 1));
        SurfaceInteraction i = s.intersect(r, Double.MAX_VALUE, true);
        assertNotNull(i);
        assertEquals(7, i.t, 1e-8);
        assertEquals(new Point3d(0, 0, -3), i.point);
        assertEquals(new Normal3d(0, 0, -1), i.normal);
        assertEquals(0, i.u, 1e-8);
        assertEquals(0, i.v, 1e-8);
    }

    @Test
    public void testBackHit() {

        Sphere s = new Sphere(Point3d.ORIGIN, 3.0);

        Ray r = new Ray(new Point3d(0, 0, 0), new Vector3d(0, 0, 1));
        SurfaceInteraction i = s.intersect(r, Double.MAX_VALUE, true);
        assertNotNull(i);
        assertEquals(3, i.t, 1e-8);
        assertEquals(new Point3d(0, 0, 3), i.point);
        assertEquals(new Normal3d(0, 0, 1), i.normal);
        assertEquals(0, i.u, 1e-8);
        assertEquals(1, i.v, 1e-8);
    }

    @Test
    public void testTransform() {

        Sphere s = new Sphere(Transform.translate(0, 0, 10), false, 3.0);

        Ray r = new Ray(new Point3d(0, 0, 0), new Vector3d(0, 0, 1));
        SurfaceInteraction i = s.intersect(r, Double.MAX_VALUE, true);
        assertNotNull(i);
        assertEquals(7, i.t, 1e-8);
        assertEquals(new Point3d(0, 0, 7), i.point);
        assertEquals(new Normal3d(0, 0, -1), i.normal);
        assertEquals(0, i.u, 1e-8);
        assertEquals(0, i.v, 1e-8);
    }

    @Test
    public void testMiss() {
        Sphere s = new Sphere(new Point3d(0, -100.5, -1), 100);
        Ray r = new Ray(Point3d.ORIGIN, new Vector3d(0, 10, -1));
        SurfaceInteraction i = s.intersect(r, Double.MAX_VALUE, true);
        assertNull(i);
    }
}
