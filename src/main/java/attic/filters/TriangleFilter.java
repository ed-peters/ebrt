package attic.filters;

import attic.math.Point2d;

public class TriangleFilter extends Filter {

    public TriangleFilter(Point2d radius) {
        super(radius);
    }

    @Override
    public double evaluate(Point2d point) {
        double x = Math.max(0, radius().x() - Math.abs(point.x()));
        double y = Math.max(0, radius().y() - Math.abs(point.y()));
        return x * y;
    }
}
