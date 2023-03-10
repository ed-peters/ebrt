package attic.math;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TestMatrix {

    @Test
    public void testMultiply() {

        double [][] lhs = {
                { 5, 7, 9, 10 },
                { 2, 3, 3, 8 },
                { 8, 10, 2, 3 },
                { 3, 3, 4, 8 }
        };

        double [][] rhs = {
                { 3, 10, 12, 8 },
                { 12, 1, 4, 9 },
                { 9, 10, 12, 2 },
                { 3, 12, 4, 10 }
        };

        double [][] exp = {
                { 210, 267, 236, 221 },
                { 93, 149, 104, 129 },
                { 171, 146, 172, 188 },
                { 105, 169, 128, 139 }
        };

        double [][] act = Matrix.multiply(lhs, rhs);
        assertNotNull(act);
        for (int i=0; i<4; i++) {
            for (int j=0; j<4; j++) {
                assertEquals(exp[i][j], act[i][j], 1e-8);
            }
        }
    }

    @Test
    public void testInvert() {

        double [][] m = new double[][]{
                { 1, 1, 1, -1 },
                { 1, 1, -1, 1 },
                { 1, -1, 1, 1 },
                { -1, 1, 1, 1 }
        };

        double [][] exp = new double[][]{
                { 0.25, 0.25, 0.25, -0.25 },
                { 0.25, 0.25, -0.25, 0.25 },
                { 0.25, -0.25, 0.25, 0.25 },
                { -0.25, 0.25, 0.25, 0.25 }
        };

        double [][] act = Matrix.invert(m);
        assertNotNull(act);
        for (int i=0; i<4; i++) {
            for (int j=0; j<4; j++) {
                assertEquals(exp[i][j], act[i][j], 1e-8);
            }
        }
    }

    @Test
    public void testTranspose() {

        double [][] m = new double[4][4];
        for (int i=0; i<4; i++) {
            for (int j=0; j<4; j++) {
                m[i][j] = i * j;
            }
        }

        double [][] t = Matrix.transpose(m);
        assertNotNull(t);
        for (int i=0; i<4; i++) {
            for (int j=0; j<4; j++) {
                assertEquals(t[i][j], m[j][i], 1e-8);
            }
        }
    }
}
