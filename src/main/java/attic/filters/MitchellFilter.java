package attic.filters;

import attic.math.Point2d;

public class MitchellFilter extends Filter {

    private final double b;
    private final double c;

    public MitchellFilter(Point2d radius, double b, double c) {
        super(radius);
        this.b = b;
        this.c = c;
    }

    @Override
    public double evaluate(Point2d point) {
        double mx = mitchell(point.x() * inverseRadius().x());
        double my = mitchell(point.y() * inverseRadius().y());
        return mx * my;
    }

    private double mitchell(double x) {
        x = Math.abs(2 * x);
        if (x > 1) {
            return ((-b - 6 * c) * x * x * x + (6 * b + 30 * c) * x * x + (-12 * b - 48 * c) * x + (8 * b + 24 * c)) * (1.0 / 6.0);
        } else {
            return ((12 - 9 * b - 6 * c) * x * x * x + (-18 + 12*b + 6*c) * x * x + (6 - 2 * b)) * (1.f/6.f);
        }
    }
}
