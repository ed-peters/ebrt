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

public class TestCylinder {

    @Test
    public void testFrontHit() {
        Cylinder c = new Cylinder(Transform.IDENT, false, 3.0, 5.0);
        Ray r = new Ray(new Point3d(0, 10, 0), new Vector3d(0, -1, 0));
        SurfaceInteraction i = c.intersect(r, Double.MAX_VALUE, true);
        assertNotNull(i);
        assertEquals(7, i.t, 1e-8);
        assertEquals(new Point3d(0, 3, 0), i.point);
        assertEquals(new Normal3d(0, 1, 0), i.normal);
        assertEquals(0.25, i.u, 1e-8);
        assertEquals(0, i.v, 1e-8);
    }

    @Test
    public void testBackHit() {
        Cylinder c = new Cylinder(Transform.IDENT, false, 3.0, 5.0);
        Ray r = new Ray(new Point3d(0,  0, 0), new Vector3d(0, -1, 0));
        SurfaceInteraction i = c.intersect(r, Double.MAX_VALUE, true);
        assertNotNull(i);
        assertEquals(3, i.t, 1e-8);
        assertEquals(new Point3d(0, -3, 0), i.point);
        assertEquals(new Normal3d(0, -1, 0), i.normal);
        assertEquals(0.75, i.u, 1e-8);
        assertEquals(0, i.v, 1e-8);
    }

    @Test
    public void testTransform() {
        Cylinder c = new Cylinder(Transform.translate(10, 0, 0), false, 3.0, 5.0);
        Ray r = new Ray(new Point3d(0,  0, 0), new Vector3d(1, 0, 0));
        SurfaceInteraction i = c.intersect(r, Double.MAX_VALUE, true);
        assertNotNull(i);
        assertEquals(7, i.t, 1e-8);
        assertEquals(new Point3d(7, 0, 0), i.point);
        assertEquals(new Normal3d(-1, 0, 0), i.normal);
        assertEquals(0.5, i.u, 1e-8);
        assertEquals(0, i.v, 1e-8);
    }
}
