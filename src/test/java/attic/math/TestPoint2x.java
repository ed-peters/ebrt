package attic.math;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestPoint2x {

    @Test
    public void testCeilFloor() {
        Point2d p = new Point2d(0.3, 0.5);
        assertEquals(new Point2i(1, 1), p.ceil());
        assertEquals(new Point2i(0, 0), p.floor());
    }

    @Test
    public void testConcentricSampleDisk() {
        double sr2 = Math.sqrt(2) / 2.0;
        Point2d [] points = {
            new Point2d(0, 0),
            new Point2d(0, 1),
            new Point2d(1, 1),
            new Point2d(1, 0),
        };
        Point2d [] expected = {
                new Point2d(-sr2, -sr2),
                new Point2d(-sr2, sr2),
                new Point2d(sr2, sr2),
                new Point2d(sr2, -sr2),
        };
        for (int i=0; i<points.length; i++) {
            Point2d a = points[i].concentricSampleDisk();
            Point2d e = expected[i];
            assertEquals(a.x(), e.x(), 1e-8);
            assertEquals(a.y(), e.y(), 1e-8);
        }
    }
}
