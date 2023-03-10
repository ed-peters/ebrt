package attic.math;

import org.junit.Test;

import static java.lang.Math.sqrt;

import static org.junit.Assert.assertEquals;

public class TestVector3d {

    @Test
    public void testBasics() {
        Vector3d v1 = new Vector3d(1, 2, 3);
        Vector3d v2 = new Vector3d(2, 3, 4);
        assertEquals(new Vector3d(3, 5, 7), v1.plus(v2));
        assertEquals(new Vector3d(-1, -1, -1), v1.minus(v2));
        assertEquals(new Vector3d(2, 4, 6), v1.mul(2));

    }

    @Test
    public void testDots() {
        Vector3d v1 = new Vector3d(1, 2, 3);
        Vector3d v2 = new Vector3d(2, 3, 4);
        assertEquals(20, v1.dot(v2), 1e-8);
        assertEquals(sqrt(14), v1.length(), 1e-8);
        assertEquals(new Vector3d(1 / sqrt(14), 2 / sqrt(14), 3 / sqrt(14)), v1.normalize());
    }

    @Test
    public void testCross() {
        Vector3d v1 = new Vector3d(3, -3, 1);
        Vector3d v2 = new Vector3d(4, 9, 2);
        assertEquals(new Vector3d(-15, -2, 39), v1.cross(v2));
    }
}
