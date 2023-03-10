package attic.filters;

import attic.math.Point2d;

public class GaussianFilter extends Filter {

    private final double alpha;
    private final double expX;
    private final double expY;

    public GaussianFilter(Point2d radius, double alpha) {
        super(radius);
        this.alpha = alpha;
        this.expX = Math.exp(-alpha * radius.x() * radius.x());
        this.expY = Math.exp(-alpha * radius.y() * radius.y());
    }

    @Override
    public double evaluate(Point2d point) {
        return gaussian(point.x(), expX) * gaussian(point.y(), expY);
    }

    private double gaussian(double p, double e) {
        return Math.max(0, Math.exp(-alpha * p * p) - e);
    }
}
