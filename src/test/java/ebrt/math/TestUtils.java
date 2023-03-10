package ebrt.math;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TestUtils {

    @Test
    public void testQuadratic() {

        Hits h = Utils.quadratic(5, 6, 1);
        assertNotNull(h);
        assertEquals(-1.0, h.t0(), 1e-8);
        assertEquals(-0.2, h.t1(), 1e-8);

        Hits h2 = Utils.quadratic(5, 2, 1);
        assertNotNull(h2);
        assertTrue(Double.isNaN(h2.t0()));
        assertTrue(Double.isNaN(h2.t1()));

        Hits h3 = Utils.quadratic(-4, 12, -9);
        assertNotNull(h3);
        assertEquals(1.5, h3.t0(), 1e-8);
        assertEquals(1.5, h3.t1(), 1e-8);
    }
}
