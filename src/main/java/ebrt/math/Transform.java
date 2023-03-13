package ebrt.math;

import java.util.Arrays;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toRadians;

public record Transform(double [][] matrix, double [][] inverse) {

    public static final Transform IDENT = Transform.from(new double [][]{
            { 1, 0, 0, 0 },
            { 0, 1, 0, 0 },
            { 0, 0, 1, 0 },
            { 0, 0, 0, 1 }
    });

    public boolean swapsHandedness() {
        double det = matrix[0][0] * (matrix[1][1] * matrix[2][2] - matrix[1][2] * matrix[2][1]) -
                        matrix[0][1] * (matrix[1][0] * matrix[2][2] - matrix[1][2] * matrix[2][0]) +
                        matrix[0][2] * (matrix[1][0] * matrix[2][1] - matrix[1][1] * matrix[2][0]);
        return det < 0;
    }

    public Transform invert() {
        return new Transform(inverse, matrix);
    }

    public Transform compose(Transform other) {
        double [][] mm = Matrix.multiply(matrix, other.matrix);
        double [][] mi = Matrix.multiply(other.inverse, inverse);
        return new Transform(mm, mi);
    }

    public Transform andThen(Transform other) {
        return other.compose(this);
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i=0; i<4; i++) {
            if (i > 0) builder.append("\n");
            builder.append(Arrays.toString(matrix[i]));
        }
        return builder.toString();
    }

    public static Transform translate(double dx, double dy, double dz) {
        double [][] mm = {
                { 1, 0, 0, dx },
                { 0, 1, 0, dy },
                { 0, 0, 1, dz },
                { 0, 0, 0, 1 }
        };
        double [][] mi = {
                { 1, 0, 0, -dx },
                { 0, 1, 0, -dy },
                { 0, 0, 1, -dz },
                { 0, 0, 0, 1 }
        };
        return new Transform(mm, mi);
    }

    public static Transform scale(double dx, double dy, double dz) {
        double [][] mm = {
                { dx, 0, 0, 0 },
                { 0, dy, 0, 0 },
                { 0, 0, dz, 0 },
                { 0, 0, 0, 1 }
        };
        double [][] mi = {
                { 1 / dx, 0, 0, 0 },
                { 0, 1 / dy, 0, 0 },
                { 0, 0, 1 / dz, 0 },
                { 0, 0, 0, 1 }
        };
        return new Transform(mm, mi);
    }

    public static Transform rotate(Vector3d axis, double degrees) {

        Vector3d a = axis.normalize();
        double s = sin(toRadians(degrees));
        double c = cos(toRadians(degrees));

        double [][] d = new double[4][4];

        d[0][0] = a.x() * a.x() + (1 - a.x() * a.x()) * c;
        d[0][1] = a.x() * a.y() * (1 - c) - a.z() * s;
        d[0][2] = a.x() * a.z() * (1 - c) + a.y() * s;

        d[1][0] = a.x() * a.y() * (1 - c) + a.z() * s;
        d[1][1] = a.y() * a.y() + (1 - a.y() * a.y()) * c;
        d[1][2] = a.y() * a.z() * (1 - c) - a.x() * s;

        d[2][0] = a.x() * a.z() * (1 - c) - a.y() * s;
        d[2][1] = a.y() * a.z() * (1 - c) + a.x() * s;
        d[2][2] = a.z() * a.z() + (1 - a.z() * a.z()) * c;

        d[3][3] = 1;

        return from(d);
    }

    public static Transform lookAt(Point3d position, Point3d target, Vector3d up) {

        double [][] d = new double[4][4];

        d[0][3] = position.x();
        d[1][3] = position.y();
        d[2][3] = position.z();
        d[3][3] = 1;

        Vector3d dir = target.minus(position).normalize();
        Vector3d right = up.normalize().cross(dir).normalize();
        Vector3d newup = dir.cross(right);

        d[0][0] = right.x();
        d[1][0] = right.y();
        d[2][0] = right.z();

        d[0][1] = newup.x();
        d[1][1] = newup.y();
        d[2][1] = newup.z();

        d[0][2] = dir.x();
        d[1][2] = dir.y();
        d[2][2] = dir.z();

        d[0][3] = position.x();
        d[1][3] = position.y();
        d[2][3] = position.z();
        d[3][3] = 1;

        return from(d);
    }

    public static Transform from(double [][] matrix) {
        return new Transform(matrix, Matrix.invert(matrix));
    }

    public static void main(String [] args) {
        Transform t = lookAt(Point3d.ORIGIN, new Point3d(0, 0, -1), Vector3d.Y);
        System.err.println(t);
    }
}
