package ebrt.math;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestPoint3d {

    @Test
    public void testPlusMinus() {
        Point3d p1 = new Point3d(1, 2, 3);
        Point3d p2 = new Point3d(3, 4, 5);
        Vector3d v = new Vector3d(4, 5, 6);
        assertEquals(new Vector3d(2, 2, 2), p2.minus(p1));
        assertEquals(new Point3d(5, 7, 9), p1.plus(v));
    }

    @Test
    public void testMinMax() {
        Point3d p1 = new Point3d(-99, 11, 99);
        Point3d p2 = new Point3d(99, -23, 0);
        Point3d p3 = new Point3d(23, 33, 27);
        assertEquals(new Point3d(-99, -23, 0), Point3d.min(p1, p2, p3));
        assertEquals(new Point3d(99, 33, 99), Point3d.max(p1, p2, p3));
    }
}
