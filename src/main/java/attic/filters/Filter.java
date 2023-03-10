package attic.filters;

import attic.math.Point2d;

public abstract class Filter {

    public static final int TABLE_WIDTH = 16;

    private final Point2d radius;
    private final Point2d inverseRadius;

    public Filter(Point2d radius) {
        this.radius = radius;
        this.inverseRadius = radius.invert();
    }

    public Point2d radius() {
        return radius;
    }

    public Point2d inverseRadius() {
        return inverseRadius;
    }

    public abstract double evaluate(Point2d point);

    public double [] makeTable() {
        double [] table = new double[TABLE_WIDTH * TABLE_WIDTH];
        int offset = 0;
        for (int y = 0; y<TABLE_WIDTH; ++y) {
            for (int x = 0; x<TABLE_WIDTH; ++x) {
                Point2d point = new Point2d(x, y).plus(Point2d.HALVSIES).mul(radius).div(TABLE_WIDTH);
                table[offset++] = evaluate(point);
            }
        }
        return table;
    }

    public static Filter box(Point2d radius) {
        return new Filter(radius) {
            @Override
            public double evaluate(Point2d point) {
                return 1;
            }
        };
    }

    public static Filter triangle(Point2d radius) {
        return new Filter(radius) {
            @Override
            public double evaluate(Point2d point) {
                double x = Math.max(0, radius().x() - Math.abs(point.x()));
                double y = Math.max(0, radius().y() - Math.abs(point.y()));
                return x * y;
            }
        };
    }

    public static Filter gaussian(Point2d radius, double alpha) {
        double expX = Math.exp(-alpha * radius.x() * radius.x());
        double expY = Math.exp(-alpha * radius.y() * radius.y());
        return new Filter(radius) {
            @Override
            public double evaluate(Point2d point) {
                double gx = Math.max(0, Math.exp(-alpha * point.x() * point.x()) - expX);
                double gy = Math.max(0, Math.exp(-alpha * point.y() * point.y()) - expY);
                return gx * gy;
            }
        };
    }
}
