package ebrt.math;

public class Matrix {

    public static Vector3d multiply(double [][] m, Vector3d v) {
        double x = m[0][0] * v.x() + m[0][1] * v.y() + m[0][2] * v.z();
        double y = m[1][0] * v.x() + m[1][1] * v.y() + m[1][2] * v.z();
        double z = m[2][0] * v.x() + m[2][1] * v.y() + m[2][2] * v.z();
        return new Vector3d(x, y, z);
    }

    public static Point3d multiply(double [][] m, Point3d p) {
        double x = m[0][0] * p.x() + m[0][1] * p.y() + m[0][2] * p.z() + m[0][3];
        double y = m[1][0] * p.x() + m[1][1] * p.y() + m[1][2] * p.z() + m[1][3];
        double z = m[2][0] * p.x() + m[2][1] * p.y() + m[2][2] * p.z() + m[2][3];
        double w = m[3][0] * p.x() + m[3][1] * p.y() + m[3][2] * p.z() + m[3][3];
        return new Point3d(x / w, y / w, z / w);
    }

    public static double [][] multiply(double [][] lhs, double [][] rhs) {
        double [][] prod = new double[4][4];
        for (int r=0; r<4; r++) {
            for (int c=0; c<4; c++) {
                for (int i=0; i<4; i++) {
                    prod[r][c] += lhs[r][i] * rhs[i][c];
                }
            }
        }
        return prod;
    }

    public static Vector3d column(double [][] m, int idx) {
        return new Vector3d(m[0][idx], m[1][idx], m[2][idx]);
    }

    public static double [][] invert(double [][] m) {

        Vector3d a = column(m, 0);
        Vector3d b = column(m, 1);
        Vector3d c = column(m, 2);
        Vector3d d = column(m, 3);

        double x = m[3][0];
        double y = m[3][1];
        double z = m[3][2];
        double w = m[3][3];

        Vector3d s = a.cross(b);
        Vector3d t = c.cross(d);
        Vector3d u = a.mul(y).minus(b.mul(x));
        Vector3d v = c.mul(w).minus(d.mul(z));

        double det = s.dot(v) + t.dot(u);
        s = s.div(det);
        t = t.div(det);
        u = u.div(det);
        v = v.div(det);

        Vector3d r0 = b.cross(v).plus(t.mul(y));
        Vector3d r1 = v.cross(a).minus(t.mul(x));
        Vector3d r2 = d.cross(u).plus(s.mul(w));
        Vector3d r3 = u.cross(c).minus(s.mul(z));

        return new double[][] {
                { r0.x(), r0.y(), r0.z(), -b.dot(t) },
                { r1.x(), r1.y(), r1.z(), a.dot(t) },
                { r2.x(), r2.y(), r2.z(), -d.dot(s) },
                { r3.x(), r3.y(), r3.z(), c.dot(s) }
        };
    }

    public static double [][] transpose(double [][] m) {
        double [][] t = new double[4][4];
        for (int i=0; i<4; i++) {
            for (int j=0; j<4; j++) {
                t[i][j] = m[j][i];
            }
        }
        return t;
    }
}
